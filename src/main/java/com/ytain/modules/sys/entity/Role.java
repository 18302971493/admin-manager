package com.ytain.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.ytain.base.BaseEntity;
import com.ytain.common.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Transient;
import java.util.List;

@Data
@TableName("sys_role")
public class Role extends BaseEntity<Role> {


    @ApiModelProperty(value = "角色名 以ROLE_开头")
    private String name;

    @ApiModelProperty(value = "是否为注册默认角色")
    private Boolean defaultRole;

    @ApiModelProperty(value = "数据权限类型 0全部默认 1自定义")
    private Integer dataType = CommonConstant.DATA_TYPE_ALL;

    @ApiModelProperty(value = "备注")
    private String description;

    @Transient
    @ApiModelProperty(value = "拥有权限")
    private List<RoleMenu> menuList;

    @Transient
    @ApiModelProperty(value = "拥有数据权限")
    private List<RoleOffice> officeList;
}
