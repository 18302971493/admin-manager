package com.ytain.modules.sys.dao;

import com.ytain.base.BaseDao;
import com.ytain.modules.sys.entity.User;

import java.util.List;

/**
 * 用户数据处理层
 * @author Exrickx
 */
public interface UserDao extends BaseDao<User> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    List<User> findByUsername(String username);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    List<User> findByMobile(String mobile);

    /**
     * 通过邮件获取用户
     * @param email
     * @return
     */
    List<User> findByEmail(String email);

    /**
     * 通过部门id获取
     * @param officeId
     * @return
     */
    List<User> findByOfficeId(String officeId);
}
