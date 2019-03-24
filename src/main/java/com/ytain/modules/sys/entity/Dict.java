package com.ytain.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.ytain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: 六娃
 * @description: 字典实体
 * @create: 2018-12-21 17:13
 */
@Data
@Accessors(chain = true)
@TableName("sys_dict")
public class Dict extends BaseEntity<Dict> {

    @ApiModelProperty(value = "字典数据值")
    private String value;

    @ApiModelProperty(value = "标签名")
    private String label;

    @ApiModelProperty(value = "字典类型")
    private String type;

    @ApiModelProperty(value = "字典描述")
    private String description;

    @ApiModelProperty(value = "排序")
    private Integer sortOrder;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "父ID")
    private String parentId;
}
