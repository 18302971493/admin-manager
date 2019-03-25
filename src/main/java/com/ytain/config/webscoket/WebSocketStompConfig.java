package com.ytain.config.webscoket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @author 胡国栋
 * @version 1.0.0
 * @description
 * @date 2019-03-25 17:37
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/admin/ws").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 订阅Broker名称 user点对点 topic广播即群发
        registry.enableSimpleBroker("/user","/topic");
        // 全局(客户端)使用的消息前缀
        registry.setApplicationDestinationPrefixes("/app");
        // 点对点使用的前缀 无需配置 默认/user
        registry.setUserDestinationPrefix("/user");
    }
}
