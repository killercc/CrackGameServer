package com.zyx.crackgameserver.modules.security.controller;


import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.model.SysUser;
import com.zyx.crackgameserver.modules.security.security.ResponseWriteJson;
import com.zyx.crackgameserver.modules.security.service.LoginService;
import com.zyx.crackgameserver.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("user")
public class SecurityController extends ResponseWriteJson {


    private final LoginService loginService;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        return loginService.createcaptha();
    }
    @PostMapping("/login")
    public void login(HttpServletRequest req,
                        HttpServletResponse resp,
                        @RequestBody SysUser sysUser) throws Exception {

        loginService.login(req, resp, sysUser);

    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest req){
        String token = req.getHeader("auth");
        return loginService.logout(token);
    }

    @PostMapping("/register")
    public Result register(@RequestBody SysUser sysUser){
        sysUser.setDownloadtimes(0);
        return loginService.register(sysUser);
    }
}
