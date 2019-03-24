package com.ytain.config.jwt;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.ytain.common.annotation.SystemLog;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.constant.SecurityConstant;
import com.ytain.common.enums.LogType;
import com.ytain.common.vo.TokenUser;
import com.ytain.config.security.SecurityUserDetails;
import com.ytain.util.ResponseUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/23 21:15
 * @Modified By：
 */
@Slf4j
@Component
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Value("${admin.token.redis}")
    private Boolean tokenRedis;

    @Value("${admin.tokenExpireTime}")
    private Integer tokenExpireTime;

    @Value("${admin.saveLoginTime}")
    private Integer saveLoginTime;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    @SystemLog(description = "登陆成功",type = LogType.LOGIN)
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String saveLogin=request.getParameter(SecurityConstant.SAVE_LOGIN);
        Boolean saved = false;
        if(StrUtil.isNotBlank(saveLogin) && Boolean.valueOf(saveLogin)){
            saved = true;
            if(!tokenRedis){
                tokenExpireTime = saveLoginTime * 60 * 24;
            }
        }
        //获取当前登陆用户名
        String username= ((UserDetails)authentication.getPrincipal()).getUsername();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) ((UserDetails) authentication.getPrincipal()).getAuthorities();
        List<String> list = Lists.newArrayList();
        authorities.forEach(item->{
            list.add(item.getAuthority());
        });
        // 登陆成功生成token
        String token;
        if(tokenRedis){
            token= UUID.randomUUID().toString().replace("-","");
            TokenUser user=new TokenUser(username,list,saved);
            String oldToken=redisTemplate.opsForValue().get(SecurityConstant.USER_TOKEN+username);
            if(StrUtil.isNotBlank(oldToken)){
                redisTemplate.delete(SecurityConstant.TOKEN_PRE + oldToken);
            }
            if(saved){
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, token, saveLoginTime, TimeUnit.DAYS);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), saveLoginTime, TimeUnit.DAYS);
            }else{
                redisTemplate.opsForValue().set(SecurityConstant.USER_TOKEN + username, token, tokenExpireTime, TimeUnit.MINUTES);
                redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + token, new Gson().toJson(user), tokenExpireTime, TimeUnit.MINUTES);
            }
        }else{
            // jwt
            token = SecurityConstant.TOKEN_SPLIT + Jwts.builder()
                    //主题 放入用户名
                    .setSubject(username)
                    //自定义属性 放入用户拥有请求权限
                    .claim(SecurityConstant.AUTHORITIES, new Gson().toJson(list))
                    //失效时间
                    .setExpiration(new Date(System.currentTimeMillis() + tokenExpireTime * 60 * 1000))
                    //签名算法和密钥
                    .signWith(SignatureAlgorithm.HS512, SecurityConstant.JWT_SIGN_KEY)
                    .compact();
        }
        ResponseUtil.out(response,ResponseUtil.resultMap(true,200,"登陆成功",token));
    }
}
