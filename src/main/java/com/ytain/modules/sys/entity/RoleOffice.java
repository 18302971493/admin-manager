package com.ytain.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.ytain.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("sys_role_office")
public class RoleOffice extends BaseEntity<RoleOffice> {


    @ApiModelProperty(value = "角色id")
    private String roleId;

    @ApiModelProperty(value = "部门id")
    private String officeId;
}