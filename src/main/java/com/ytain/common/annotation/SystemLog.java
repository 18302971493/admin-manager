package com.ytain.common.annotation;

import com.ytain.common.enums.LogType;

import java.lang.annotation.*;

/**
 * @Author: 胡国栋
 * @Description:
 * @Date:Create：in 2019/3/23 22:24
 * @Modified By：
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})//作用于参数或方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SystemLog {
    /**
     * 日志名称
     * @return
     */
    String description() default "";

    /**
     * 日志类型
     * @return
     */
    LogType type() default LogType.OPERATION;
}
