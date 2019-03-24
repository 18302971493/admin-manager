package com.ytain.modules.sys.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import com.ytain.base.BaseEntity;
import com.ytain.common.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Transient;
import java.util.List;

@Data
@TableName("sys_user")
public class User extends BaseEntity<User> {


    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮件")
    private String email;

    @ApiModelProperty(value = "省市县地址")
    private String address;

    @ApiModelProperty(value = "街道地址")
    private String street;

    @ApiModelProperty(value = "0女 1男 2保密")
    private Integer sex;

    @ApiModelProperty(value = "密码强度")
    private String passStrength;

    @ApiModelProperty(value = "用户头像")
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    @ApiModelProperty(value = "状态 默认0正常 -1拉黑")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @ApiModelProperty(value = "所属部门id")
    private String officeId;

    @Transient
    @ApiModelProperty(value = "所属部门名称")
    private String officeTitle;

    @Transient
    @ApiModelProperty(value = "用户拥有角色")
    private List<Role> roleList;

    @Transient
    @ApiModelProperty(value = "用户拥有的权限")
    private List<Menu> menuList;
    @Transient
    private List<String> roles;
}
