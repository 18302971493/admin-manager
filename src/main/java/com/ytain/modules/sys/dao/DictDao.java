package com.ytain.modules.sys.dao;

import com.ytain.base.BaseDao;
import com.ytain.modules.sys.entity.Dict;

import java.util.List;

public interface DictDao extends BaseDao<Dict> {
    /**
     * 根据字典类型查询
     * @param type
     * @return
     */
    List<Dict> findByType(String type);
}
