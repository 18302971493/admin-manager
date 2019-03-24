package com.ytain.base;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.ytain.common.vo.PageVo;
import com.ytain.common.vo.SearchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/22 21:37
 * @Modified By：
 */
@Transactional(readOnly = true)
public abstract class BaseService<D extends BaseDao<T>, T extends BaseEntity<T>>  {
    @Autowired
    protected D dao;
    /**
     * 日志对象
     */

    @Transactional(readOnly = false)
    public Integer insert(T entity) {
        return dao.insert(entity);
    }

    @Transactional(readOnly = false)
    public Integer delete(String id) {
         Integer i=dao.deleteById(id);
         return i;
    }
    @Transactional(readOnly = false)
    public void delete(T t) {
        dao.delete(t);
    }

    @Transactional(readOnly = false)
    public void deleteAll() {
        dao.deleteAll();
    }

    @Transactional(readOnly = false)
    public Integer update(T entity) {
        return dao.update(entity);
    }

    public T get(String id) {
        return dao.get(id);
    }

    /**
     * 全量列表
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return dao.findList(entity);
    }

    public Page<T> findPage(Page<T> page, T t){
        return page.setRecords(dao.findList(page, t));
    }
}
