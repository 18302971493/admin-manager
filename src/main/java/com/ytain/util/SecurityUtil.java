package com.ytain.util;

import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.constant.SecurityConstant;
import com.ytain.common.vo.TokenUser;
import com.ytain.modules.sys.entity.Menu;
import com.ytain.modules.sys.entity.Role;
import com.ytain.modules.sys.entity.User;
import com.ytain.modules.sys.service.UserRoleService;
import com.ytain.modules.sys.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class SecurityUtil {

    @Value("${admin.token.redis}")
    private Boolean tokenRedis;

    @Value("${admin.saveLoginTime}")
    private Integer saveLoginTime;

    @Value("${admin.tokenExpireTime}")
    private Integer tokenExpireTime;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getToken(String username, Boolean saveLogin){

        Boolean saved = false;
        if(saveLogin==null||saveLogin){
            saved = true;
            if(!tokenRedis){
                tokenExpireTime = saveLoginTime * 60 * 24;
            }
        }
        // 生成token
        User u = userService.findByUsername(username);
        List<String> list = new ArrayList<>();
        for(Menu menu : u.getMenuList()){
            if(CommonConstant.PERMISSION_OPERATION.equals(menu.getType())
                    && StrUtil.isNotBlank(menu.getTitle())
                    && StrUtil.isNotBlank(menu.getPath())) {
                list.add(menu.getTitle());
            }
        }
        for(Role r : u.getRoleList()){
            list.add(r.getName());
        }
        // 登陆成功生成token
        String token;
        if(tokenRedis){
            // redis
            token = UUID.randomUUID().toString().replace("-", "");
            TokenUser user = new TokenUser(u.getUsername(), list, saved);
            // 单点登录 之前的token失效
            String oldToken = redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN + u.getUsername());
            if(StrUtil.isNotBlank(oldToken)){
                redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
            }
            if(saved){
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + u.getUsername(), token, saveLoginTime, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), saveLoginTime, TimeUnit.DAYS);
            }else{
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + u.getUsername(), token, tokenExpireTime, TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenExpireTime, TimeUnit.MINUTES);
            }
        }else{
            // jwt
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(u.getUsername())
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        return token;
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public User getCurrUser(){

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(user.getUsername());
    }

    /**
     * 获取当前用户数据权限 null代表具有所有权限
     */
    public List<String> getDeparmentIds(){

        List<String> deparmentIds = new ArrayList<>();
        User u = getCurrUser();
        // 用户角色
        List<Role> userRoleList = userRoleService.findByUserId(u.getId());
        // 判断有无全部数据的角色
        Boolean flagAll = false;
        for(Role r : userRoleList){
            if(r.getDataType()==null||r.getDataType().equals(CommonConstant.DATA_TYPE_ALL)){
                flagAll = true;
                break;
            }
        }
        if(flagAll){
            return null;
        }
        // 查找自定义
        return userRoleService.findDepIdsByUserId(u.getId());
    }
}
