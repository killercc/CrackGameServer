package com.zyx.crackgameserver.modules.schedule.utils;

import com.zyx.crackgameserver.utils.SpringContextUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;


import java.lang.reflect.Method;
import java.util.Objects;


@Slf4j
public class ScheduleRunnable implements Runnable{

    private String params;
    private Object target;
    private Method method;

    public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException {

        this.target = SpringContextUtils.getBean(beanName);
        this.params = params;

        if(StringUtils.isNoneBlank(params)){
            this.method = target.getClass().getDeclaredMethod(methodName,String.class);
        }else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        ReflectionUtils.makeAccessible(method);
        if (StringUtils.isNotBlank(params)) {
            method.invoke(target,params);
        } else {
            method.invoke(target);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleRunnable that = (ScheduleRunnable) o;
        return Objects.equals(params, that.params) && Objects.equals(target, that.target) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params, target, method);
    }
}
