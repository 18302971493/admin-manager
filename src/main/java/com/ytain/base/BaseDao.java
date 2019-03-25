package com.ytain.base;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/22 21:34
 * @Modified By：
 */
public  interface BaseDao<T> extends BaseMapper<T> {
    /**
     * 根据id删除
     * @param id
     * @return
     */
    Integer deleteById(String id);

    /**
     * 根据T 删除
     * @param t
     */
    void delete(T t);

    /**
     * 全部删除
     */
    void deleteAll();

    /**
     * 插入
     * @param entity
     * @return
     */
    @Override
    Integer insert(T entity);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    T get(String id);

    /**
     * 全部查询
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 修改
     * @param entity
     * @return
     */
    Integer update(T entity);

    /**
     * 分页查询
     * @param page
     * @param t
     * @return
     */
    List<T> findList(Pagination page, T t);

}
