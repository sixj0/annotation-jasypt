package com.sixj.aspect;

import com.sixj.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author sixiaojie
 * @date 2020-08-04-16:04
 */
@Component
@Aspect
@Slf4j
public class SetEncryptAspect {


    @Autowired
    private BeanUtil beanUtil;

    @Around("@annotation(com.sixj.annotation.EnableEncrypt)")
    public Object doSetEncrypt(ProceedingJoinPoint point) throws Throwable{
        // 前置增强
        log.info("前置增强");

        // 执行被切面的方法，获取结果集
        Object proceed = point.proceed();

        // 后置增强 --对密码进行加密
        try {
            beanUtil.setValueForCollection((Collection) proceed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 将被设置好之后的值返回
        return proceed;
    }
}
