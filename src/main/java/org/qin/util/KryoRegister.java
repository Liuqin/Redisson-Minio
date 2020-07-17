package org.qin.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @title: KryoRegister
 * @decription:
 * @author: liuqin
 * @date: 2020/6/19 11:46
 */


@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE, ElementType.TYPE_PARAMETER})

@Retention(RetentionPolicy.RUNTIME)


public @interface KryoRegister {
    int seconds() default 3;// 自动注解
}
