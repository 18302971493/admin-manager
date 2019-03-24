package com.ytain.modules.sys.service;
import com.ytain.base.BaseService;
import com.ytain.modules.sys.dao.MenuDao;
import com.ytain.modules.sys.entity.Menu;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限接口实现
 */
@Slf4j
@Service
@Transactional
public class MenuService extends BaseService<MenuDao, Menu> {

    @Autowired
    private MenuDao menuDao;


    public List<Menu> findByLevelOrderBySortOrder(Integer level) {

        return menuDao.findByLevelOrderBySortOrder(level);
    }

    public List<Menu> findByParentIdOrderBySortOrder(String parentId) {

        return menuDao.findByParentIdOrderBySortOrder(parentId);
    }

    public List<Menu> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {

        return menuDao.findByTypeAndStatusOrderBySortOrder(type, status);
    }

    public List<Menu> findByTitle(String title) {

        return menuDao.findByTitle(title);
    }

    public List<Menu> findByTitleLikeOrderBySortOrder(String title) {

        return menuDao.findByTitleLikeOrderBySortOrder(title);
    }

    public List<Menu> findByUserId(String userId){
        return menuDao.findByUserId(userId);
    }
}