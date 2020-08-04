package com.sixj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要加密的字段
 * @author sixiaojie
 * @date 2020-08-04-15:52
 */
@Target(ElementType.FIELD)// 应用范围--字段
@Retention(RetentionPolicy.RUNTIME)// 注解的生命周期
public @interface NeedEncrypt {

    /**
     * 加密用到的密钥
     * @return
     */
    String secretKey();
}
