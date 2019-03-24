package com.ytain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/23 21:05
 * @Modified By：
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ignore")
public class IgnorePropertiesConfig {
    private List<String> urls;
}
