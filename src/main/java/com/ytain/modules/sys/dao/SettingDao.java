package com.ytain.modules.sys.dao;

import com.ytain.base.BaseDao;
import com.ytain.modules.sys.entity.Setting;
import org.apache.ibatis.annotations.Param;

/**
 * @author 胡国栋
 * @version 1.0.0
 * @description
 * @date 2019-03-25 18:55
 */
public interface SettingDao extends BaseDao<Setting> {
    /**
     * 根据配置类型查询
     * @param type
     * @return
     */
    Setting findByType(@Param("type") Integer type);
}
