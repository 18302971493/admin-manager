package com.ytain.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.ytain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 胡国栋
 * @version 1.0.0
 * @description
 * @date 2019-03-25 18:52
 */
@Data
@TableName("sys_setting")
public class Setting extends BaseEntity<Setting> {
    @ApiModelProperty(value = "参数JSON字符串")
    private String paramter;

    @ApiModelProperty(value = "数据类型")
    private Integer type;
}
