package com.ytain.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.plugins.Page;
import com.ytain.common.annotation.SystemLog;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.enums.LogType;
import com.ytain.common.vo.Result;
import com.ytain.modules.sys.entity.Office;
import com.ytain.modules.sys.entity.Role;
import com.ytain.modules.sys.entity.User;
import com.ytain.modules.sys.entity.UserRole;
import com.ytain.modules.sys.service.OfficeService;
import com.ytain.modules.sys.service.RoleService;
import com.ytain.modules.sys.service.UserRoleService;
import com.ytain.modules.sys.service.UserService;
import com.ytain.util.ResultUtil;
import com.ytain.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Exrickx
 */
@Slf4j
@RestController
@Api(description = "用户接口")
@RequestMapping("/admin/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ApiOperation(value = "注册用户")
    public Result<Object> register(@ModelAttribute User u,
                                   @RequestParam String verify,
                                   @RequestParam String captchaId){

        if(StrUtil.isBlank(verify)|| StrUtil.isBlank(u.getUsername())
                || StrUtil.isBlank(u.getPassword())){
            return new ResultUtil<Object>().setErrorMsg("缺少必需表单字段");
        }

        //验证码
        String code=redisTemplate.opsForValue().get(captchaId);
        if(StrUtil.isBlank(code)){
            return new ResultUtil<Object>().setErrorMsg("验证码已过期，请重新获取");
        }

        if(!verify.toLowerCase().equals(code.toLowerCase())) {
            log.error("注册失败，验证码错误：code:"+ verify +",redisCode:"+code.toLowerCase());
            return new ResultUtil<Object>().setErrorMsg("验证码输入错误");
        }

        if(userService.findByUsername(u.getUsername())!=null){
            return new ResultUtil<Object>().setErrorMsg("该用户名已被注册");
        }
        //删除缓存
        redisTemplate.delete("user::"+u.getUsername());

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        u.setType(CommonConstant.USER_TYPE_NORMAL);
        Integer id=userService.insert(u);
        if(id==null){
            return new ResultUtil<Object>().setErrorMsg("注册失败");
        }
        // 默认角色
        List<Role> roleList = roleService.findByDefaultRole(true);
        if(roleList!=null&&roleList.size()>0){
            for(Role role : roleList){
                UserRole ur = new UserRole();
                ur.setUserId(u.getId());
                ur.setRoleId(role.getId());
                userRoleService.insert(ur);
            }
        }

        return new ResultUtil<Object>().setData(u);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户接口")
    public Result<User> getUserInfo(){

        User u = securityUtil.getCurrUser();
        // 清除持久上下文环境 避免后面语句导致持久化
        u.setPassword(null);
        return new ResultUtil<User>().setData(u);
    }

    @RequestMapping(value = "/unlock",method = RequestMethod.POST)
    @ApiOperation(value = "解锁验证密码")
    public Result<Object> unLock(@RequestParam String password){

        User u = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, u.getPassword())){
            return new ResultUtil<Object>().setErrorMsg("密码不正确");
        }
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ApiOperation(value = "修改用户自己资料",notes = "用户名密码不会修改 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    public Result<Object> editOwn(@ModelAttribute User u){

        User old = securityUtil.getCurrUser();
        u.setUsername(old.getUsername());
        u.setPassword(old.getPassword());
        Integer id= userService.update(u);
        if(id==null){
            return new ResultUtil<Object>().setErrorMsg("修改失败");
        }
        return new ResultUtil<Object>().setSuccessMsg("修改成功");
    }

    /**
     * @param u
     * @return
     */
    @RequestMapping(value = "/admin/edit",method = RequestMethod.POST)
    @ApiOperation(value = "管理员修改资料",notes = "需要通过id获取原用户信息 需要username更新缓存")
    @CacheEvict(key = "#u.username")
    @SystemLog(description = "用户资料修改",type = LogType.OPERATION)
    public Result<Object> edit(@RequestBody User u){

        User old = userService.get(u.getId());
        //若修改了用户名
        if(!old.getUsername().equals(u.getUsername())){
            //若修改用户名删除原用户名缓存
            redisTemplate.delete("user::"+old.getUsername());
            //判断新用户名是否存在
            if(userService.findByUsername(u.getUsername())!=null){
                return new ResultUtil<Object>().setErrorMsg("该用户名已被存在");
            }
            //删除缓存
            redisTemplate.delete("user::"+u.getUsername());
        }

        // 若修改了手机和邮箱判断是否唯一
        if(!old.getMobile().equals(u.getMobile())&&userService.findByMobile(u.getMobile())!=null){
            return new ResultUtil<Object>().setErrorMsg("该手机号已绑定其他账户");
        }
        if(!old.getEmail().equals(u.getEmail())&&userService.findByMobile(u.getEmail())!=null){
            return new ResultUtil<Object>().setErrorMsg("该邮箱已绑定其他账户");
        }

        u.setPassword(old.getPassword());
        Integer id=userService.update(u);
        if(id==null){
            return new ResultUtil<Object>().setErrorMsg("修改失败");
        }
        //删除该用户角色
        userRoleService.deleteByUserId(u.getId());
        if(u.getRoles()!=null&&u.getRoles().size()>0){
            //新角色
            for(String roleId : u.getRoles()){
                UserRole ur = new UserRole();
                ur.setRoleId(roleId);
                ur.setUserId(u.getId());
                userRoleService.insert(ur);
            }
        }
        //手动删除缓存
        redisTemplate.delete("userRole::"+u.getId());
        redisTemplate.delete("userRole::depIds:"+u.getId());
        return new ResultUtil<Object>().setSuccessMsg("修改成功");
    }

    /**
     * 线上demo不允许测试账号改密码
     * @param password
     * @param newPass
     * @return
     */
    @RequestMapping(value = "/modifyPass",method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    @SystemLog(description = "修改密码",type = LogType.OPERATION)
    public Result<Object> modifyPass(@ApiParam("旧密码") @RequestParam String password,
                                     @ApiParam("新密码") @RequestParam String newPass){

        User user = securityUtil.getCurrUser();
        if(!new BCryptPasswordEncoder().matches(password, user.getPassword())){
            return new ResultUtil<Object>().setErrorMsg("旧密码不正确");
        }

        String newEncryptPass= new BCryptPasswordEncoder().encode(newPass);
        user.setPassword(newEncryptPass);
        userService.update(user);

        //手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());

        return new ResultUtil<Object>().setSuccessMsg("修改密码成功");
    }

    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    @SystemLog(description = "查询用户列表",type = LogType.OPERATION)
    public Result<Page<User>> getUserList(@ModelAttribute User user,
                                          Integer pageNo, Integer pageSize){

        Page<User> page = userService.findPage(new Page<>(pageNo,pageSize),user);
        for(User u: page.getRecords()){
            // 关联部门
            if(StrUtil.isNotBlank(u.getOfficeId())){
                Office office = officeService.get(u.getOfficeId());
                u.setOfficeTitle(office.getTitle());
            }
            // 关联角色
            List<Role> list = userRoleService.findByUserId(u.getId());
            u.setRoleList(list);
            // 清除持久上下文环境 避免后面语句导致持久化
            u.setPassword(null);
        }
        return new ResultUtil<Page<User>>().setData(page);
    }

    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    @ApiOperation(value = "获取全部用户数据")
    @SystemLog(description = "查询用户列表",type = LogType.OPERATION)
    public Result<List<User>> getByCondition(){

        List<User> list = userService.findList(new User());
        for(User u: list){
            // 关联部门
            if(StrUtil.isNotBlank(u.getOfficeId())){
                Office department = officeService.get(u.getOfficeId());
                u.setOfficeTitle(department.getTitle());
            }
            // 清除持久上下文环境 避免后面语句导致持久化
            u.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(list);
    }

    @RequestMapping(value = "/admin/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
    @SystemLog(description = "添加用户",type = LogType.OPERATION)
    public Result<Object> regist(@RequestBody User u){

        if(StrUtil.isBlank(u.getUsername()) || StrUtil.isBlank(u.getPassword())){
            return new ResultUtil<Object>().setErrorMsg("缺少必需表单字段");
        }

        if(userService.findByUsername(u.getUsername())!=null){
            return new ResultUtil<Object>().setErrorMsg("该用户名已被注册");
        }
        //删除缓存
        redisTemplate.delete("user::"+u.getUsername());

        String encryptPass = new BCryptPasswordEncoder().encode(u.getPassword());
        u.setPassword(encryptPass);
        Integer user=userService.insert(u);
        if(user==null){
            return new ResultUtil<Object>().setErrorMsg("添加失败");
        }
        if(u.getRoles()!=null&&u.getRoles().size()>0){
            //添加角色
            for(String roleId : u.getRoles()){
                UserRole ur = new UserRole();
                ur.setUserId(u.getId());
                ur.setRoleId(roleId);
                userRoleService.insert(ur);
            }
        }

        return new ResultUtil<Object>().setData(user);
    }

    @RequestMapping(value = "/admin/disable/{userId}",method = RequestMethod.POST)
    @ApiOperation(value = "后台禁用用户")
    public Result<Object> disable(@ApiParam("用户唯一id标识") @PathVariable String userId){

        User user=userService.get(userId);
        if(user==null){
            return new ResultUtil<Object>().setErrorMsg("通过userId获取用户失败");
        }
        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        userService.update(user);
        //手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/admin/enable/{userId}",method = RequestMethod.POST)
    @ApiOperation(value = "后台启用用户")
    public Result<Object> enable(@ApiParam("用户唯一id标识") @PathVariable String userId){

        User user=userService.get(userId);
        if(user==null){
            return new ResultUtil<Object>().setErrorMsg("通过userId获取用户失败");
        }
        user.setStatus(CommonConstant.USER_STATUS_NORMAL);
        userService.update(user);
        //手动更新缓存
        redisTemplate.delete("user::"+user.getUsername());
        return new ResultUtil<Object>().setData(null);
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delAllByIds(@PathVariable String[] ids){

        for(String id:ids){
            if(id.equals("1")){
                return new ResultUtil<Object>().setErrorMsg("不能删除超级管理员");
            }
            User u = userService.get(id);
            //删除缓存
            redisTemplate.delete("user::" + u.getUsername());
            redisTemplate.delete("userRole::" + u.getId());
            redisTemplate.delete("userRole::depIds:" + u.getId());
            redisTemplate.delete("menu::userMenuList:" + u.getId());
            userService.delete(id);
            //删除关联角色
            userRoleService.deleteByUserId(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

}
