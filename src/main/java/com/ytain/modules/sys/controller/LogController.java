package com.ytain.modules.sys.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ytain.common.vo.Result;
import com.ytain.modules.sys.entity.Log;
import com.ytain.modules.sys.service.LogService;
import com.ytain.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@Api(description = "日志管理接口")
@RequestMapping("/admin/log")
public class LogController{


    @Autowired
    private LogService logService;

    @RequestMapping(value = "/getAllByPage",method = RequestMethod.GET)
    @ApiOperation(value = "分页获取全部")
    public Result<Page<Log>> getAllByPage(@ModelAttribute Log log, Integer pageNo, Integer pageSize){
    Page<Log> logs = logService.findPage(new Page<Log>(pageNo,pageSize),log);
    return new ResultUtil<Page<Log>>().setData(logs);
    }

    @RequestMapping(value = "/delByIds/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "批量删除")
    public Result<Object> delByIds(@PathVariable String[] ids){
        for(String id : ids){
            logService.delete(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("删除成功");
    }

    @RequestMapping(value = "/delAll",method = RequestMethod.DELETE)
    @ApiOperation(value = "全部删除")
    public Result<Object> delAll(){
        logService.deleteAll();
        return new ResultUtil<Object>().setSuccessMsg("删除成功");
    }
}
