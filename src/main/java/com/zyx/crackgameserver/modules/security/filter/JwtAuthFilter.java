package com.zyx.crackgameserver.modules.security.filter;


import com.alibaba.fastjson.JSONObject;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.utils.JwtUtils;
import com.zyx.crackgameserver.modules.security.service.MyUserDetailsService;
import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.response.ResultCode;
import io.jsonwebtoken.JwtException;

import org.apache.commons.lang3.StringUtils;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthFilter extends BasicAuthenticationFilter  {


    private final JwtUtils jwtUtils;

    private final MyUserDetailsService myUserDetailsService;

    private final RedisUtils redisUtils;




    public JwtAuthFilter(RedisUtils redisUtils, JwtUtils jwtUtils, MyUserDetailsService myUserDetailsService, AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.myUserDetailsService = myUserDetailsService;
        this.redisUtils = redisUtils;
        this.jwtUtils = jwtUtils;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {


        String token = request.getHeader(jwtUtils.getHeader());
        //System.out.println("jwtFilter------>Entry");
        if(StringUtils.isNotBlank(token)){

            String username = jwtUtils.extractUsername(token);

            UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

            if(!StringUtils.equals((String)redisUtils.get("UserToken_"+username),token)){
//                System.out.println("jwtFilter------>redis不匹配");
//                response.setStatus(200);
//                response.getWriter().write(JSONObject.toJSONString(Result.error(ResultCode.USER_ACCOUNT_EXPIRED)));
                chain.doFilter(request, response);
                return;
            }
            if (!redisUtils.hasKey("UserToken_"+username)){
//                System.out.println("jwtFilter------>redis未查询到");
//                response.setStatus(200);
//                response.getWriter().write(JSONObject.toJSONString(Result.error(ResultCode.USER_NOT_LOGIN)));
                chain.doFilter(request, response);
                return;
            }


            //System.out.println("jwtFilter ----- > " + token);
            if(jwtUtils.validateToken(token,userDetails)){
//                System.out.println("jwtFilter------>token 异常");
//                response.setStatus(200);
//                response.getWriter().write(JSONObject.toJSONString(Result.error(ResultCode.USER_TOKEN_ERROR)));
                chain.doFilter(request, response);
                return;
            }

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(username,null,userDetails.getAuthorities());


            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            chain.doFilter(request,response);

        }else {
            chain.doFilter(request, response);
        }
    }

}
