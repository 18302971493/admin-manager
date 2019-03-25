package com.ytain.modules.sys.service;

import com.ytain.base.BaseService;
import com.ytain.modules.sys.dao.SettingDao;
import com.ytain.modules.sys.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 胡国栋
 * @version 1.0.0
 * @description
 * @date 2019-03-25 18:55
 */
@Service
public class SettingService extends BaseService<SettingDao, Setting> {
    @Autowired
    private SettingDao settingDao;

    public Setting findByType(Integer type){
        return settingDao.findByType(type);
    }
}
