package com.ytain.config.listener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/24 1:04
 * @Modified By：
 */
@Configuration
public class RequestListener {
    @Bean
    public ServletListenerRegistrationBean listenerRegistrationBean(){
        ServletListenerRegistrationBean bean=new ServletListenerRegistrationBean();
        bean.setListener(new RequestContextListener());
        bean.setOrder(1);
        return bean;
    }
}
