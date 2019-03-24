package com.ytain.config.security;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.ytain.common.constant.CommonConstant;
import com.ytain.modules.sys.entity.Menu;
import com.ytain.modules.sys.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/23 20:51
 * @Modified By：
 */
@Slf4j
public class SecurityUserDetails extends User implements UserDetails {
    private static final long serialVersionUID = 1L;

    public SecurityUserDetails(User user) {
        if(user!=null) {
            this.setUsername(user.getUsername());
            this.setPassword(user.getPassword());
            this.setStatus(user.getStatus());
            this.setRoleList(user.getRoleList());
            this.setMenuList(user.getMenuList());
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //权限结果集
        List<GrantedAuthority> authorityList= Lists.newArrayList();
        List<Menu> menuList=this.getMenuList();
        menuList.forEach(item->{
           if(item.getType().equals(CommonConstant.PERMISSION_OPERATION)
                   && StrUtil.isNotBlank(item.getTitle())
                   &&StrUtil.isNotBlank(item.getPath())){
               authorityList.add(new SimpleGrantedAuthority(item.getTitle()));
           }
        });
        return authorityList;
    }
    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return CommonConstant.USER_STATUS_LOCK.equals(this.getStatus())?false:true;
    }
    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return CommonConstant.USER_STATUS_NORMAL.equals(this.getStatus())?true:false;
    }
}
