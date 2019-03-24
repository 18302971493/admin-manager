package com.ytain.config.jwt;

import cn.hutool.core.util.StrUtil;
import com.ytain.common.constant.SecurityConstant;
import com.ytain.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author: 六娃
 * @description:
 * @create: 2018-12-20 16:06
 */
@Component
public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstant.HEADER);
        if(StrUtil.isBlank(header)){
            header = request.getParameter(SecurityConstant.HEADER);
        }
        String v = redisTemplate.opsForValue().get(SecurityConstant.TOKEN_PRE + header);
        redisTemplate.opsForValue().set(SecurityConstant.TOKEN_PRE + header, v, 1, TimeUnit.SECONDS);
        ResponseUtil.out(response, ResponseUtil.resultMap(false,401,"登录已失效，请重新登录"));
    }
}
