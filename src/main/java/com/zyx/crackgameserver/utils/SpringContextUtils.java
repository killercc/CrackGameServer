package com.zyx.crackgameserver.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 根据bean名称查找Bean
     * @param beanName
     * @return
     */
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    /**
     * 根据类型获取bean
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> requiredType){
        return applicationContext.getBean(requiredType);
    }

    /**
     * 根据name + type 获取bean
     * @param beanName
     * @param requiredType
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName, Class<T> requiredType){
        return applicationContext.getBean(beanName,requiredType);

    }

    /**
     * 根据名字判断某个bean是否存在
     * @param beanName
     * @return
     */
    public static boolean containBean(String beanName){
        return applicationContext.containsBean(beanName);
    }

    /**
     * 判断bean是否单例
     * @param beanName
     * @return
     */
    public static boolean isSingleton(String beanName){
        return applicationContext.isSingleton(beanName);
    }


    /**
     * 根据名字获取bean类型
     * @param beanName
     * @return
     */
    public static Class<?> getType(String beanName){
        return applicationContext.getType(beanName);
    }



}
