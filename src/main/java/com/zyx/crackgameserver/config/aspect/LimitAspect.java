package com.zyx.crackgameserver.config.aspect;


import cn.hutool.core.util.ObjectUtil;
import com.zyx.crackgameserver.config.anno.AccessLimit;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.utils.JwtUtils;
import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.response.ResultCode;
import com.zyx.crackgameserver.utils.UserinfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Slf4j
@Component
public class LimitAspect {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${accesslimit.limitheader}")
    private String limitheader;

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
        log.info("access annotation thread name : " + Thread.currentThread().getName());
        String token = request.getHeader("auth");
        if(StringUtils.isBlank(token)){
            return Result.error(ResultCode.ACCESS_LIMIT);
        }
        String username = UserinfoUtils.getCurrentUser();//jwtUtils.extractUsername(token);
        UserinfoUtils.remove();
        String url = request.getRequestURI();
        // 不存在该键 第一次访问 (或已经重置)
        Integer count = (Integer) redisUtils.get(limitheader+":"+url+":"+username);
        if(ObjectUtil.isNull(count)){
            redisUtils.set(limitheader+":"+url+":"+username,1,second);
            return joinPoint.proceed();
        }

        // 访问一次 ++
        if( count < maxcount){
            redisUtils.incr(limitheader+":"+url+":"+username,1);
            return joinPoint.proceed();
        }else {
            return Result.error(ResultCode.ACCESS_LIMIT);
        }
    }


}
