package com.zyx.crackgameserver.service;


import com.zyx.crackgameserver.mapper.GameMapper;
import com.zyx.crackgameserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {


    @Autowired
    private GameMapper gameMapper;

    @Autowired
    private UserMapper userMapper;




//    public downloadEntity download(String regCode,String gcode){
//        downloadEntity download = downloadMapper.selectByPrimaryKey(gcode);
//        if(download == null){
//            return null;
//        }
//        if(download.getGty().equals("WLCW")){
//            download.setGty("");
//        }
//        reqEntity reqentity = userMapper.findregCode(regCode).get(0);
//        Integer times = reqentity.getTimes() - 1;
//        System.out.println(reqentity);
//        userMapper.updatetimes(times,regCode);
//        return download;
//    }






}
