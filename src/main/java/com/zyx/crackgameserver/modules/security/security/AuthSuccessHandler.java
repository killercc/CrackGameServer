package com.zyx.crackgameserver.modules.security.security;

import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.service.MyUserDetailsService;
import com.zyx.crackgameserver.modules.security.utils.JwtUtils;
import com.zyx.crackgameserver.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("authSuccessHandler")
public class AuthSuccessHandler extends ResponseWriteJson implements AuthenticationSuccessHandler {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private RedisUtils redisUtilsl;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Result result = Result.ok().message("登录成功");
        String username = authentication.getName();
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
        String token = jwtUtils.generateToken(userDetails);
        //存入redis  7天时间过期
        redisUtilsl.set("UserToken_"+username,token,60*60);//7*24*60*60
        httpServletResponse.setHeader(jwtUtils.getHeader(),token);
        this.WriteJSON(httpServletRequest,httpServletResponse,result);
    }
}
