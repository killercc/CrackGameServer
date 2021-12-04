package com.zyx.crackgameserver.modules.security.service;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.exception.CaptchaException;
import com.zyx.crackgameserver.modules.security.mapper.UserDetailMapper;
import com.zyx.crackgameserver.modules.security.model.SysUser;
import com.zyx.crackgameserver.modules.security.security.AuthFailureHanlder;
import com.zyx.crackgameserver.modules.security.security.ResponseWriteJson;
import com.zyx.crackgameserver.modules.security.utils.JwtUtils;
import com.zyx.crackgameserver.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService extends ResponseWriteJson {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final MyUserDetailsService myUserDetailsService;
    private final RedisUtils redisUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailMapper userDetailMapper;
    private final AuthFailureHanlder authFailureHanlder;

    @Value("${captcha.header}")
    private String header;

    @Value("${captcha.timeout}")
    private Integer timeout;


    private boolean verfycaptha(String Cuuid, String code){

        Object coderesult = redisUtils.get(header+Cuuid);

        return (!ObjectUtil.isNull(coderesult) && StringUtils.equalsIgnoreCase((String)coderesult,code));


    }

    public Result createcaptha() throws IOException {

        String Cuuid = UUID.randomUUID().toString();
        String code = RandomUtil.randomString(5);

        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(120, 40);
        Image image = lineCaptcha.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write((BufferedImage)image,"jpg",outputStream);


        String encode = Base64Encoder.encode(outputStream.toByteArray());
        String base64 = "data:image/jpeg;base64,"+ encode;


        log.info(code);
        log.info(Cuuid);

        Map<String,Object> resultmap = new HashMap<>();
        resultmap.put("id",Cuuid);
        resultmap.put("image",base64);

        redisUtils.set(header+Cuuid,code,timeout);

        return Result.ok().data(resultmap);

    }

    public void login(HttpServletRequest req, HttpServletResponse resp, SysUser sysUser) throws Exception {


        String code = sysUser.getCode();
        String codeuuid = sysUser.getCodeuuid();

        String username = sysUser.getUsername();
        String password = sysUser.getPassword();

        try {
            if (!verfycaptha(codeuuid,code)){
                throw new CaptchaException("error code");
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password)
            );

            final UserDetails userDetails= myUserDetailsService.loadUserByUsername(username);
            final String token = jwtUtils.generateToken(userDetails);
            //设置过期时间为 7天
            redisUtils.set("UserToken_"+username,token,7*24*60*60);
            resp.setHeader(jwtUtils.getHeader(),token);
            //登录验证成功后 删除验证码
            redisUtils.del(header+codeuuid);
            this.WriteJSON(req,resp,Result.ok().message("登录成功"));
        }
        catch (AuthenticationException e){
            authFailureHanlder.onAuthenticationFailure(req,resp,e);
        }



    }

    public Result register(SysUser sysUser){

        String username = sysUser.getUsername();
        String password = sysUser.getPassword();

        String code = sysUser.getCode();
        String codeuuid = sysUser.getCodeuuid();

        if (!verfycaptha(codeuuid,code)){
            return Result.error().message("验证码错误");
        }

        SysUser sysUsers = userDetailMapper.finUserByName(username);
        //System.out.println(sysUsers);
        if(!ObjectUtil.isNull(sysUsers)){
            return Result.error().message("已存在该用户名");
        }
        String encode = passwordEncoder.encode(password);
        sysUser.setPassword(encode);
        userDetailMapper.insert(sysUser);
        //System.out.println(encode);
        return Result.ok();
    }

    public Result logout(String userkey){
        String username = jwtUtils.extractUsername(userkey);
        redisUtils.del("UserToken_"+username);
        return Result.ok().message("登出成功");
    }

}
