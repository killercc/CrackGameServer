package com.zyx.crackgameserver.controller;


import com.zyx.crackgameserver.mapper.UserMapper;
import com.zyx.crackgameserver.model.Charge;
import com.zyx.crackgameserver.model.RegCode;
import com.zyx.crackgameserver.modules.spider.utils.MD5Utils;
import com.zyx.crackgameserver.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("piggyin301")
@RequiredArgsConstructor
public class AdminController {

    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ROLE_admin')")
    @PostMapping("/generatekey")
    public Result generatekey(@RequestBody Charge charge){

        int nums = charge.getNums();
        int times = charge.getTimes();

        List<RegCode> regCodeList = new ArrayList<>(nums);
        for (int i = 0; i < nums; i++) {
            String s = MD5Utils.randomMD5();
            RegCode regCode = new RegCode();
            regCode.setRegkey(s);
            regCode.setTimes(times);
            regCodeList.add(regCode);
        }
        //System.out.println(regCodeList);
        userMapper.insertkeys(regCodeList);
        return Result.ok().data("keys",regCodeList);
    }



}
