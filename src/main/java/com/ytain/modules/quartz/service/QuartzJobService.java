package com.ytain.modules.quartz.service;

import com.ytain.base.BaseService;
import com.ytain.config.exception.BaseException;
import com.ytain.modules.quartz.dao.QuartzJobDao;
import com.ytain.modules.quartz.entity.QuartzJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/24 12:53
 * @Modified By：
 */
@Service
@Slf4j
public class QuartzJobService extends BaseService<QuartzJobDao, QuartzJob> {
     @Autowired
     private Scheduler scheduler;
    /**
     * 添加定时任务
     * @param jobClassName
     * @param cronExpression
     * @param parameter
     */
    @Transactional(readOnly = false)
    public void add(String jobClassName, String cronExpression, String parameter){

        try {
            // 启动调度器
            scheduler.start();

            //构建job信息
            JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                    .withIdentity(jobClassName)
                    .usingJobData("parameter",parameter)
                    .build();

            //表达式调度构建器(即任务执行的时间)
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            //按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName)
                    .withSchedule(scheduleBuilder).build();

            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error(e.toString());
            throw new BaseException("创建定时任务失败");
        } catch (Exception e){
            throw new BaseException("后台找不到该类名任务");
        }
    }

    /**
     * 删除定时任务
     * @param jobClassName
     */
    @Transactional(readOnly = false)
    public void deleteJob(String jobClassName){

        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName));
            scheduler.deleteJob(JobKey.jobKey(jobClassName));
        } catch (Exception e) {
            throw new BaseException("删除定时任务失败");
        }
    }

    public static Job getClass(String classname) throws Exception {
        Class<?> class1 = Class.forName(classname);
        return (Job)class1.newInstance();
    }

    /**
     * 暂停任务
     * @param jobClassName
     */
    @Transactional(readOnly = false)
    public void pauseJob(String jobClassName){
        try{
            scheduler.pauseJob(JobKey.jobKey(jobClassName));
        }catch (Exception e){
            throw new BaseException("暂停定时任务失败");
        }
    }

    /**
     * 恢复任务
     * @param jobClassName
     */
    @Transactional(readOnly = false)
    public void resumeJob(String jobClassName){
        try{
            scheduler.resumeJob(JobKey.jobKey(jobClassName));
        }catch (Exception e){
            throw new BaseException("恢复定时任务失败");
        }
    }
}
