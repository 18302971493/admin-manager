package com.ytain;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.ytain.modules.sys.entity.Log;
import com.ytain.modules.sys.service.LogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminApplicationTests {
     @Autowired
     private LogService logService;
    @Test
    public void contextLoads() {
//        Page pageVo=new Page(1,2);
//        Log log=new Log();
//        log.setLogType(1);
//        Page page = logService.findPage(pageVo, log);
//        System.out.println(JSON.toJSONString(page));

        Log log = new Log();
        log.setLogType(1);
        log.setName("2");
        logService.insert(log);
    }

}
