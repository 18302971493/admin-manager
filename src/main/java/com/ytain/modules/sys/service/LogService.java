package com.ytain.modules.sys.service;

import com.ytain.base.BaseService;
import com.ytain.modules.sys.dao.LogDao;
import com.ytain.modules.sys.entity.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志接口实现
 */
@Slf4j
@Service
@Transactional
public class LogService extends BaseService<LogDao, Log>   {

}
