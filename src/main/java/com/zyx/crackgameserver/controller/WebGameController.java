package com.zyx.crackgameserver.controller;


import com.zyx.crackgameserver.config.anno.AccessLimit;
import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.service.WebGameService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Api(tags = "web接口操作")
@RestController()
@RequestMapping("web")
public class WebGameController {

    @Autowired
    private WebGameService webGameService;
    /**
     * 查找游戏 需要登录
     */

    @AccessLimit
    @GetMapping("getgame")
    public Result getgame(@RequestParam(value = "index")Integer index,
                          @RequestParam(value = "size")Integer size,
                          @RequestParam(value = "gamename")String gamename) throws ExecutionException, InterruptedException {

        return webGameService.fingames(index,size,gamename);

    }
    /**
     * 下载请求 需登录  验证下载次数
     */
    @AccessLimit
    @GetMapping("/download")
    public Result download(HttpServletRequest request,
                           @RequestParam(value = "gcode")String gcode){

        return webGameService.download(request,gcode);
    }


    @AccessLimit
    @GetMapping("/getinfo")
    public Result getUserinfo(HttpServletRequest request) throws ExecutionException, InterruptedException {
        String auth = request.getHeader("auth");
        return webGameService.getUserinfo(auth);

    }

    @AccessLimit
    @GetMapping("/getsavegames")
    public Result getsavegames(HttpServletRequest request,
                               @RequestParam(value = "index")Integer index,
                               @RequestParam(value = "size")Integer size){
        String auth = request.getHeader("auth");
        return webGameService.getUserSaveGames(index,size,auth);
    }

    /**
     * 用户 卡密充值接口 需要登录
     */
    @AccessLimit
    @GetMapping("/charge")
    public Result userCharge(HttpServletRequest request,
                             @RequestParam(value = "regCode")String regCode){
        if(StringUtils.isBlank(regCode)){
            return Result.error().message("激活码为空！");
        }
        String auth = request.getHeader("auth");
        return webGameService.usercharge(regCode,auth);
    }







}
