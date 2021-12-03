package com.zyx.crackgameserver.config.aspect;


import cn.hutool.core.util.ObjectUtil;
import com.zyx.crackgameserver.config.anno.AccessLimit;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.response.ResultCode;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Pointcut("@annotation(com.zyx.crackgameserver.config.anno.AccessLimit)")
    public void pointcut(){

    }

    @Around("pointcut()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);

        int second = accessLimit.seconds();
        int maxcount = accessLimit.maxcount();

        String ip = request.getRemoteAddr();
        String url = request.getRequestURI();
        // 不存在该键 第一次访问 (或已经重置)
        Integer count = (Integer) redisUtils.get(url+":"+ip);
        if(ObjectUtil.isNull(count)){
            redisUtils.set(url+":"+ip,1,second);
            return joinPoint.proceed();
        }

        // 访问一次 ++
        if( count < maxcount){
            redisUtils.incr(url+":"+ip,1);
            return joinPoint.proceed();
        }else {
            return Result.error(ResultCode.ACCESS_LIMIT);
        }
    }


}
