package com.ytain.modules.quartz.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.ytain.base.BaseEntity;
import com.ytain.common.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/24 12:51
 * @Modified By：
 */
@Data
@TableName("sys_quartz_job")
public class QuartzJob extends BaseEntity<QuartzJob> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务类名")
    private String jobClassName;

    @ApiModelProperty(value = "cron表达式")
    private String cronExpression;

    @ApiModelProperty(value = "参数")
    private String parameter;

    @ApiModelProperty(value = "备注")
    private String description;

    @ApiModelProperty(value = "状态 0正常 -1停止")
    private Integer status = CommonConstant.STATUS_NORMAL;
}
