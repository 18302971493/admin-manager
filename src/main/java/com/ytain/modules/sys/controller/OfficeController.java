package com.ytain.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.vo.Result;
import com.ytain.modules.sys.entity.Office;
import com.ytain.modules.sys.entity.User;
import com.ytain.modules.sys.service.OfficeService;
import com.ytain.modules.sys.service.RoleOfficeService;
import com.ytain.modules.sys.service.UserService;
import com.ytain.util.ResultUtil;
import com.ytain.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;


/**
 * @author Exrick
 */
@Slf4j
@RestController
@Api(description = "部门管理接口")
@RequestMapping("/admin/office")
@CacheConfig(cacheNames = "office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleOfficeService roleOfficeService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;

    @RequestMapping(value = "/getByParentId/{parentId}",method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public Result<List<Office>> getByParentId(@PathVariable String parentId,
                                              @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Office> list = Lists.newArrayList();
        User u = securityUtil.getCurrUser();
        String key = "office::"+parentId+":"+u.getId()+"_"+openDataFilter;
        String v = redisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(v)){
            list = new Gson().fromJson(v, new TypeToken<List<Office>>(){}.getType());
            return new ResultUtil<List<Office>>().setData(list);
        }
        list = officeService.findByParentIdOrderBySortOrder(parentId, openDataFilter);
        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){
                Office parent = officeService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级部门");
            }
        });
        redisTemplate.opsForValue().set(key, new Gson().toJson(list));
        return new ResultUtil<List<Office>>().setData(list);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public Result<Object> add(@ModelAttribute Office office){

         officeService.insert(office);
        // 同步该节点缓存
        User u = securityUtil.getCurrUser();
        Set<String> keys = redisTemplate.keys("office::"+office.getParentId()+":*");
        redisTemplate.delete(keys);
        // 如果不是添加的一级 判断设置上级为父节点标识
        if(!CommonConstant.PARENT_ID.equals(office.getParentId())){
            Office parent = officeService.get(office.getParentId());
            if(parent.getIsParent()==null||!parent.getIsParent()){
                parent.setIsParent(true);
                officeService.update(parent);
                // 更新上级节点的缓存
                Set<String> keysParent = redisTemplate.keys("office::"+parent.getParentId()+":*");
                redisTemplate.delete(keysParent);
            }
        }
        return new ResultUtil<Object>().setSuccessMsg("添加成功");
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public Result<Object> edit(@RequestBody Office office){

         officeService.update(office);
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplate.keys("office:" + "*");
        redisTemplate.delete(keys);
        // 删除所有用户缓存
        Set<String> keysUser = redisTemplate.keys("user:" + "*");
        redisTemplate.delete(keysUser);
        return new ResultUtil<Object>().setSuccessMsg("编辑成功");
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public Result<Object> delByIds(@PathVariable String[] ids){

        for(String id:ids){
            List<User> list = userService.findByOfficeId(id);
            if(list!=null&&list.size()>0){
                return new ResultUtil<Object>().setErrorMsg("删除失败，包含正被用户使用关联的部门");
            }
        }
        for(String id:ids){
            officeService.delete(id);
            // 删除关联数据权限
            roleOfficeService.deleteByOfficeId(id);
        }
        // 手动删除所有部门缓存
        Set<String> keys = redisTemplate.keys("office:" + "*");
        redisTemplate.delete(keys);
        // 删除数据权限缓存
        Set<String> keysUserRoleData = redisTemplate.keys("userRole::depIds:" + "*");
        redisTemplate.delete(keysUserRoleData);
        return new ResultUtil<Object>().setSuccessMsg("批量通过id删除数据成功");
    }

    @RequestMapping(value = "/search",method = RequestMethod.GET)
    @ApiOperation(value = "部门名模糊搜索")
    public Result<List<Office>> searchByTitle(@RequestParam String title,
                                                  @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter){

        List<Office> list = officeService.findByTitleLikeOrderBySortOrder("%"+title+"%", openDataFilter);
        // lambda表达式
        list.forEach(item -> {
            if(!CommonConstant.PARENT_ID.equals(item.getParentId())){
                Office parent = officeService.get(item.getParentId());
                item.setParentTitle(parent.getTitle());
            }else{
                item.setParentTitle("一级部门");
            }
        });
        return new ResultUtil<List<Office>>().setData(list);
    }
}
