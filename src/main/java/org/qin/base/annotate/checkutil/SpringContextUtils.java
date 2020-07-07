package org.qin.base.annotate.checkutil;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author LiuQin
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringContextUtils() {
    }

    /**
     * 获取applicationContext对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }


    /**
     * 根据 bean 的 name 来查找对象
     *
     * @param beanName bean name
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanByName(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }


    /**
     * 根据 bean 的 类型 来查找对象
     *
     * @param requiredType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBeanByClass(Class<?> requiredType) {
        return (T) applicationContext.getBean(requiredType);
    }

}
