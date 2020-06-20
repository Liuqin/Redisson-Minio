package org.qin.base.util;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @title: ComponentScannerConfigurerWithTag
 * @decription:
 * @author: liuqin
 * @date: 2020/6/19 11:42
 */
public class ComponentScannerConfigurerWithTag implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        // 获取自定义注解 KryoRegister
        Class<? extends Annotation> annotationClass = KryoRegister.class;
        Map<String,Object> beanWhithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
        Set<Map.Entry<String,Object>> entitySet = beanWhithAnnotation.entrySet();
        for (Map.Entry<String,Object> entry :entitySet){
            Class<? extends Object> clazz = entry.getValue().getClass();//获取bean对象
            System.out.println("================"+clazz.getName());
//            KryoRegister componentDesc = AnnotationUtils.findAnnotation(clazz,KryoRegister.class);
//            System.out.println("==================="+componentDesc.channel());

        }
    }


}
