package com.zyx.crackgameserver.controller;

import com.zyx.crackgameserver.model.Gentity;
import com.zyx.crackgameserver.logging.logAnnotation;
import com.zyx.crackgameserver.response.Result;
import com.zyx.crackgameserver.service.GameService;
import com.zyx.crackgameserver.utils.commonTools;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
public class GameController {

//    @Autowired
//    private RedisUtils redisUtil;
//
//    @Autowired
//    private GameService gameService;
//
//
//    @PostMapping("getgames")
//    public ResponseEntity<List<Gentity>> getgames(){
//        List<Gentity> getgames = gameService.getgames();
//        return ResponseEntity.ok(getgames);
//    }
//
//
//    @PostMapping("findgames")
//    @logAnnotation(module = "Controller",operator = "查找游戏")
//    public ResponseEntity<List<Gentity>> findgames(@RequestParam(value = "gameV")String gamename){
//
//        List<Gentity> getgames = new ArrayList<>();
//        if(gamename.isEmpty())
//            return ResponseEntity.ok(getgames);
//        Object rresult = redisUtil.get(gamename);
//        if(rresult!=null){
//            getgames = commonTools.castList(rresult,Gentity.class);
//            System.out.println("query in redis");
//            return ResponseEntity.ok(getgames);
//        }else{
//            getgames = gameService.fingames(gamename);
//            boolean set = redisUtil.set(gamename, getgames, 60 * 10);
//            System.out.println("save status "+set);
//            System.out.println("query in sql");
//            return ResponseEntity.ok(getgames);
//        }
//
//
//    }

//    @PostMapping("checkregCode")
//    public ResponseEntity<String> checkregCode(@RequestParam(value = "regCode")String regCode){
//        if(regCode.isEmpty())return ResponseEntity.ok("Null");
//        return ResponseEntity.ok(gameService.checkCode(regCode));
//    }

//    @PostMapping("download")
//    public ResponseEntity<?> download(@RequestParam(value = "regCode")String regCode,
//                                      @RequestParam(value = "gcode")String gcode){
//        if(regCode.isEmpty())return ResponseEntity.ok("Null");
//        String checkresult = gameService.checkCode(regCode);
//        if(checkresult.equals("error") )return ResponseEntity.ok("Null");
//        else if(checkresult.equals("timesError"))return ResponseEntity.ok("timesError");
//        if(gcode.isEmpty())return ResponseEntity.ok("Null");
//        downloadEntity download = gameService.download(regCode,gcode);
//        return ResponseEntity.ok(download);
//    }


    @PostMapping("uploadGame")
    public Result upload(@RequestParam("file") MultipartFile file){

        if(file.isEmpty()){
            return Result.error().message("请选择有效文件");
        }
        String fileName = file.getOriginalFilename();
        String fileSavePath = "D:\\";

        File dest = new File(fileSavePath + fileName);
        try {
            file.transferTo(dest);
            return Result.ok().message("上传成功");

        } catch (IOException ignored) {
            System.out.println("上传失败");
            return Result.error().message("上传异常");
        }
    }

//    @PostMapping("addkey")
//    public boolean raddkey(@RequestParam("key") String key,
//                          @RequestParam("value") String value){
//
//        return redisUtil.set(key,value,10);
//    }
//    @GetMapping("findkey")
//    public String rfindkey(@RequestParam("key") String key){
//        return (String) redisUtil.get(key);
//    }
}
