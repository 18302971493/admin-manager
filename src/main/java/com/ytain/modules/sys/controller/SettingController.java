package com.ytain.modules.sys.controller;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.vo.Result;
import com.ytain.modules.sys.entity.Office;
import com.ytain.modules.sys.entity.Setting;
import com.ytain.modules.sys.entity.User;
import com.ytain.modules.sys.service.SettingService;
import com.ytain.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author 胡国栋
 * @version 1.0.0
 * @description
 * @date 2019-03-25 18:55
 */
@Slf4j
@RestController
@Api(description = "系统配置接口")
@RequestMapping("/admin/setting")
@Transactional
public class SettingController {
    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "/findSettingByType/{type}",method = RequestMethod.GET)
    @ApiOperation(value = "通过类型查询系统配置")
    public Result<Setting> findSettingByType(@PathVariable Integer type){
        Setting setting = settingService.findByType(type);
        return new ResultUtil<Setting>().setData(setting);
    }
    @RequestMapping(value = "/addSetting",method = RequestMethod.POST)
    @ApiOperation(value = "新增系统配置")
    public Result<Setting> addSetting(@RequestBody Setting setting){
        settingService.insert(setting);
        return new ResultUtil<Setting>().setData(setting);
    }

    @RequestMapping(value = "/editSetting",method = RequestMethod.POST)
    @ApiOperation(value = "编辑系统配置")
    public Result<Setting> editSetting(@RequestBody Setting setting){
        settingService.update(setting);
        return new ResultUtil<Setting>().setData(setting);
    }
}
