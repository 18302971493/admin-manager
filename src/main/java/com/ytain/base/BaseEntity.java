package com.ytain.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ytain.common.constant.CommonConstant;
import com.ytain.util.SnowFlakeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
/**
 * @author: 六娃
 * @description:
 * @create: 2018-12-20 09:19
 */
@Data
public abstract class BaseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String id=String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime=new Date();

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime=new Date();

    @ApiModelProperty(value = "删除标志 默认0")
    private Integer delFlag = CommonConstant.STATUS_NORMAL;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序方式")
    private String order;

}
