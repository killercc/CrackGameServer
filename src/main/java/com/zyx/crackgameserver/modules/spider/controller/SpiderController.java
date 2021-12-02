package com.zyx.crackgameserver.modules.spider.controller;


import com.zyx.crackgameserver.modules.spider.model.IboxUser;
import com.zyx.crackgameserver.modules.spider.service.IboxLoginService;
import com.zyx.crackgameserver.response.Result;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spider")
public class SpiderController {

    @Autowired
    private IboxLoginService iboxLoginService;

    @SneakyThrows
    @GetMapping("/sendMsg")
    public Result sendmsg(@RequestParam(value = "phonenum") String phonenum){
        return iboxLoginService.sendMsg(phonenum);

    }

    @SneakyThrows
    @PostMapping("/loginibox")
    public Result login(@RequestBody IboxUser iboxUser) {

        return iboxLoginService.login(iboxUser);
    }

}
