package com.zyx.crackgameserver.modules.security.config;


import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.filter.JwtAuthFilter;
import com.zyx.crackgameserver.modules.security.security.*;
import com.zyx.crackgameserver.modules.security.service.MyUserDetailsService;
import com.zyx.crackgameserver.modules.security.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled=true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    private final MyUserDetailsService myUserDetailsService;
    private final CorsFilter corsFilter;
    //private final  AuthSuccessHandler authSuccessHandler;
    //private final  AuthFailureHanlder authFailureHanlder;
    private final  AuthEntryPointHandler authEntryPointHandler;
    private final  AuthDeniedHandler authDeniedHandler;
    //private final  AuthLogoutHandler authLogoutHandler;
    //private final  AuthLogoutSuccessHandler authLogoutSuccessHandler;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    private static final String[] URL_WITHE = {
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/*/api-docs",
            "/user/login",
            "/user/register",
            "/user/captcha",
            "/spider/*"
    };


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 没有登录权限会跳转至登录界面
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/webSocket/**"
                ).permitAll()
                // URL白名单
                .antMatchers(URL_WITHE).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated()
                //登录功能
                .and()
                .formLogin()//loginProcessingUrl("/user/login")
                //.successHandler(authSuccessHandler)
                //.failureHandler(authFailureHanlder)
                //自定义认证  添加JWT 生成认证参数过滤
                .and()
                //跨域问题
                //.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtAuthFilter(redisUtils,jwtUtils, myUserDetailsService,authenticationManager()))
                .exceptionHandling()
                //权限不足
                .accessDeniedHandler(authDeniedHandler)
                //认证失败处理
                .authenticationEntryPoint(authEntryPointHandler)
                //退出登录 功能自定义
                // token 交给redis维护 不再使用内置登出方法
//                .and()
//                .logout()
//                .addLogoutHandler(authLogoutHandler)
//                .logoutSuccessHandler(authLogoutSuccessHandler)
//                .deleteCookies("auth")
                // 防止iframe 造成跨域
                .and()
                .headers()
                .frameOptions()
                .disable()
                // 不创建会话
                .and()
                //前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("root").password(new BCryptPasswordEncoder().encode("123")).roles("vip1")
//                .and()
//                .withUser("user").password(new BCryptPasswordEncoder().encode("123")).roles("vip0");

        auth.userDetailsService(myUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
