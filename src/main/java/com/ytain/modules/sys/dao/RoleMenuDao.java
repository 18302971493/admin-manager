package com.ytain.modules.sys.dao;

import com.ytain.base.BaseDao;
import com.ytain.modules.sys.entity.RoleMenu;

import java.util.List;

/**
 * 角色权限数据处理层
 * @author Exrick
 */
public interface RoleMenuDao extends BaseDao<RoleMenu> {

    /**
     * 通过permissionId获取
     * @param menuId
     * @return
     */
    List<RoleMenu> findByMenuId(String menuId);

    /**
     * 通过roleId获取
     * @param roleId
     */
    List<RoleMenu> findByRoleId(String roleId);

    /**
     * 通过roleId删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

}