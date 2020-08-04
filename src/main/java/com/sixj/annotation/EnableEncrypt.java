package com.sixj.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用加密的方法
 * @author sixiaojie
 * @date 2020-08-04-16:00
 */
@Target(ElementType.METHOD)// 作用范围--方法上
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableEncrypt {// 对被这个注解修饰的方法切面
}
