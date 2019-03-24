package com.ytain.modules.sys.dao;

import com.ytain.base.BaseDao;
import com.ytain.modules.sys.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao extends BaseDao<Menu> {

    /**
     * 通过层级查找
     * 默认升序
     * @param level
     * @return
     */
    List<Menu> findByLevelOrderBySortOrder(Integer level);

    /**
     * 通过parendId查找
     * @param parentId
     * @return
     */
    List<Menu> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过类型和状态获取
     * @param type
     * @param status
     * @return
     */
    List<Menu> findByTypeAndStatusOrderBySortOrder(@Param("type") Integer type,@Param("status") Integer status);

    /**
     * 通过名称获取
     * @param title
     * @return
     */
    List<Menu> findByTitle(String title);

    /**
     * 模糊搜索
     * @param title
     * @return
     */
    List<Menu> findByTitleLikeOrderBySortOrder(String title);
    /**
     * 通过用户id获取
     * @param userId
     * @return
     */
    List<Menu> findByUserId(@Param("userId") String userId);
}