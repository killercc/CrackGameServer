package com.zyx.crackgameserver.service;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyx.crackgameserver.mapper.DownloadMapper;
import com.zyx.crackgameserver.mapper.GameMapper;
import com.zyx.crackgameserver.mapper.UserMapper;
import com.zyx.crackgameserver.model.DownloadEntity;
import com.zyx.crackgameserver.model.Gentity;
import com.zyx.crackgameserver.model.SaveGames;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import com.zyx.crackgameserver.modules.security.utils.JwtUtils;
import com.zyx.crackgameserver.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
@Service
public class WebGameService {

    private final GameMapper gameMapper;
    private final UserMapper userMapper;
    private final DownloadMapper downloadMapper;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Value("${Savegame.savetime}")
    private Integer savetime;

    private final String indexheader = "savegameindex:";
    private final String contentheader = "savegamecontent:";


    public Result fingames(Integer index, Integer size ,String gname) throws ExecutionException, InterruptedException {
        PageHelper.startPage(index,size);

        CompletableFuture<List<Gentity>> findgameasync= CompletableFuture.supplyAsync(
                () -> gameMapper.findgame(gname)
                ,threadPoolTaskExecutor);

        List<Gentity> findgame = findgameasync.get();

        PageInfo<Gentity> gamepages = new PageInfo<>(findgame);
        //?????????????????????
        //System.out.println(gamepages.getPageNum());
        //??????????????????
        //System.out.println(gamepages.getPages());
        //??????????????????????????????
       //System.out.println(gamepages.getPageSize());
        findgame = gamepages.getList();
        Map<String,Object> resultmap = new HashMap<>();
        resultmap.put("gamelist",findgame);
        resultmap.put("nums",gamepages.getPages());
        return Result.ok().data(resultmap);
    }

    /**
     * 11/30   savegame ??????redis??????
     * @param request
     * @param gcode
     * @return
     */
    public Result download(HttpServletRequest request, String gcode) {
        /**
         * ?????????????????????????????????????????????
         * TODO
         */
        String auth = request.getHeader("auth");
        String username = jwtUtils.extractUsername(auth);
        Integer currentTimes = userMapper.getTimesByUsername(username);
        if(currentTimes <= 0 ){
            return Result.error().message("????????????????????????");
        }else{
            DownloadEntity downloadEntities = downloadMapper.downloadByCode(gcode);
            userMapper.updatetimes(currentTimes - 1,username);
            //?????? username + gcode + "random" ???MD5???????????? ?????? ???????????? ????????????
            //String ugmd5 = SecureUtil.md5(username + gcode + "random");


            // ??????gcode?????? set??????  ????????????
            redisUtils.sSet(indexheader+username ,gcode);
            //?????? ???????????? key:value  ??????????????????             ?????? 7??? (s)
            redisUtils.set(contentheader+username+"_"+gcode,downloadEntities,savetime);

            // ?????? 7??? (ms)
            //userMapper.savegames(ugmd5,gcode,username,savetime);//7*24*60*60*1000
            return Result.ok().data("downloadlist",downloadEntities);
        }
    }

    public Result getUserinfo(String auth) {

        String username = jwtUtils.extractUsername(auth);

        Integer timesByUsername = userMapper.getTimesByUsername(username);
        //List<SaveGames> savegames = userMapper.findSavegames(username);

        return Result.ok().data("levTimes",timesByUsername);
        //return new AsyncResult<>(timesByUsername);
    }

    public Result getUserSaveGames(Integer index, Integer size ,String auth){
        String username = jwtUtils.extractUsername(auth);
        PageHelper.startPage(index,size);

        //List<SaveGames> savegames = userMapper.findSavegames(username);

        // ???????????????????????? gcode
        Set<Object> objects = redisUtils.sGet(indexheader + username);
        if(ObjectUtil.isNotNull(objects)){
            List<Object> keys = new ArrayList<>();
            for (Object object : objects) {
                keys.add(contentheader + username + "_" + object);
            }
            List<Object> savegames = redisUtils.mget(keys);
            PageInfo<Object> savegamespages = new PageInfo<>(savegames);

            savegames = savegamespages.getList();

            Map<String,Object> resultmap = new HashMap<>();
            resultmap.put("savegames",savegames);
            resultmap.put("nums",savegamespages.getPages());

            return Result.ok().data(resultmap);
        }else{
            Map<String,Object> resultmap = new HashMap<>();
            resultmap.put("savegames","");
            resultmap.put("nums",0);
            return Result.ok().data(resultmap);
        }



    }

    public Result usercharge(String regCode,String auth) {

        String username = jwtUtils.extractUsername(auth);

        Integer keyTimes = userMapper.getKeyTimes(regCode);

        //System.out.println("?????????????????????????????????----->" + keyTimes);

        Integer timesByUsername = userMapper.getTimesByUsername(username);



        if(ObjectUtil.isNotNull(keyTimes)){
            if(keyTimes <= 0){
                return Result.error().message("????????????????????????");
            }
            userMapper.updatetimes(keyTimes + timesByUsername,username);
            userMapper.updatekey(regCode,0);
            return Result.ok().message("????????????");
        }else{
            return Result.error().message("??????????????????");
        }

    }
}
