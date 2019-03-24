package com.ytain.modules.sys.service;

import cn.hutool.core.util.StrUtil;
import com.ytain.base.BaseService;
import com.ytain.modules.sys.dao.UserDao;
import com.ytain.modules.sys.entity.Menu;
import com.ytain.modules.sys.entity.Office;
import com.ytain.modules.sys.entity.Role;
import com.ytain.modules.sys.entity.User;
import com.ytain.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 用户接口实现
 */
@Slf4j
@Service
@Transactional
public class UserService extends BaseService<UserDao, User> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SecurityUtil securityUtil;

    public User findByUsername(String username) {
        
        List<User> list=userDao.findByUsername(username);
        if(list!=null&&list.size()>0){
            User user = list.get(0);
            // 关联部门
            if(StrUtil.isNotBlank(user.getOfficeId())){
                Office office = officeService.get(user.getOfficeId());
                user.setOfficeTitle(office.getTitle());
            }
            // 关联角色
            List<Role> roleList = userRoleService.findByUserId(user.getId());
            user.setRoleList(roleList);
            // 关联权限菜单
            List<Menu> permissionList = menuService.findByUserId(user.getId());
            user.setMenuList(permissionList);
            return user;
        }
        return null;
    }

    public User findByMobile(String mobile) {

        List<User> list = userDao.findByMobile(mobile);
        if(list!=null&&list.size()>0) {
            User user = list.get(0);
            return user;
        }
        return null;
    }

    public User findByEmail(String email) {

        List<User> list = userDao.findByEmail(email);
        if(list!=null&&list.size()>0) {
            User user = list.get(0);
            return user;
        }
        return null;
    }

    public List<User> findByOfficeId(String departmentId) {

        return userDao.findByOfficeId(departmentId);
    }
}
