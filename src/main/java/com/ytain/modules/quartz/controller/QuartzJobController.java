package com.ytain.modules.quartz.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ytain.common.constant.CommonConstant;
import com.ytain.common.vo.PageVo;
import com.ytain.common.vo.Result;
import com.ytain.config.exception.BaseException;
import com.ytain.modules.quartz.entity.QuartzJob;
import com.ytain.modules.quartz.service.QuartzJobService;
import com.ytain.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/24 12:54
 * @Modified By：
 */
@Slf4j
@RestController
@Api(description = "定时任务管理接口")
@RequestMapping("/admin/quartzJob")
public class QuartzJobController  {
    @Autowired
    private QuartzJobService quartzJobService;

    @Autowired
    private SimpMessagingTemplate messageConverter;

    @RequestMapping(value = "/findQuartzJobPage",method = RequestMethod.GET)
    @ApiOperation(value = "获取所有定时任务")
    public Result<Page<QuartzJob>> findQuartzJobPage(@ModelAttribute PageVo page, QuartzJob quartzJob){
        Page<QuartzJob> data = quartzJobService.findPage(new Page(page.getPageNumber(),page.getPageSize()),quartzJob);
        return new ResultUtil<Page<QuartzJob>>().setData(data);
    }
    @RequestMapping(value = "/addJob",method = RequestMethod.POST)
    @ApiOperation(value = "添加定时任务")
    public Result<Object> addJob(@RequestBody QuartzJob job){
        List<QuartzJob> list = quartzJobService.findList(job);
        if(list!=null&&list.size()>0){
            return new ResultUtil<Object>().setErrorMsg("该定时任务类名已存在");
        }
        quartzJobService.add(job.getJobClassName(),job.getCronExpression(),job.getParameter());
        quartzJobService.insert(job);
        messageConverter.convertAndSend("/topic/subscribe","您有新消息了");
        return new ResultUtil<Object>().setSuccessMsg("创建定时任务成功");
    }
    @RequestMapping(value = "/editJob",method = RequestMethod.POST)
    @ApiOperation(value = "更新定时任务")
    public Result<Object> editJob(@RequestBody QuartzJob job){

        quartzJobService.deleteJob(job.getJobClassName());
        quartzJobService.add(job.getJobClassName(),job.getCronExpression(),job.getParameter());
        job.setStatus(CommonConstant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("更新定时任务成功");
    }

    @RequestMapping(value = "/pauseJob",method = RequestMethod.POST)
    @ApiOperation(value = "暂停定时任务")
    public Result<Object> pauseJob(@RequestBody QuartzJob job){

        try {
            quartzJobService.pauseJob(job.getJobClassName());
        } catch (Exception e) {
            throw new BaseException("暂停定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_DISABLE);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("暂停定时任务成功");
    }

    @RequestMapping(value = "/resumeJob",method = RequestMethod.POST)
    @ApiOperation(value = "恢复定时任务")
    public Result<Object> resumeJob(@RequestBody QuartzJob job){
        try {
            quartzJobService.resumeJob(job.getJobClassName());
        } catch (Exception e) {
            throw new BaseException("恢复定时任务失败");
        }
        job.setStatus(CommonConstant.STATUS_NORMAL);
        quartzJobService.update(job);
        return new ResultUtil<Object>().setSuccessMsg("恢复定时任务成功");
    }

    @RequestMapping(value = "/deleteQuartz/{ids}",method = RequestMethod.DELETE)
    @ApiOperation(value = "删除定时任务")
    public Result<Object> deleteJob(@PathVariable String[] ids){

        for(String id:ids){
            QuartzJob job = quartzJobService.get(id);
            quartzJobService.deleteJob(job.getJobClassName());
            quartzJobService.delete(id);
        }
        return new ResultUtil<Object>().setSuccessMsg("删除定时任务成功");
    }
}
