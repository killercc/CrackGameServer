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
        //获取当前页序号
        //System.out.println(gamepages.getPageNum());
        //获取共分几页
        //System.out.println(gamepages.getPages());
        //获取用户设置的页大小
       //System.out.println(gamepages.getPageSize());
        findgame = gamepages.getList();
        Map<String,Object> resultmap = new HashMap<>();
        resultmap.put("gamelist",findgame);
        resultmap.put("nums",gamepages.getPages());
        return Result.ok().data(resultmap);
    }

    /**
     * 11/30   savegame 改为redis维护
     * @param request
     * @param gcode
     * @return
     */
    public Result download(HttpServletRequest request, String gcode) {
        /**
         * 添加判断用户剩余下载次数的方法
         * TODO
         */
        String auth = request.getHeader("auth");
        String username = jwtUtils.extractUsername(auth);
        Integer currentTimes = userMapper.getTimesByUsername(username);
        if(currentTimes <= 0 ){
            return Result.error().message("剩余下载次数不足");
        }else{
            DownloadEntity downloadEntities = downloadMapper.downloadByCode(gcode);
            userMapper.updatetimes(currentTimes - 1,username);
            //使用 username + gcode + "random" 的MD5作为主键 实现 有则更新 无则添加
            //String ugmd5 = SecureUtil.md5(username + gcode + "random");


            // 设置gcode主键 set集合  永久生效
            redisUtils.sSet(indexheader+username ,gcode);
            //添加 游戏体到 key:value  设置过期时间             设置 7天 (s)
            redisUtils.set(contentheader+username+"_"+gcode,downloadEntities,savetime);

            // 设置 7天 (ms)
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

        // 获取用户对应所有 gcode
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

        //System.out.println("查询激活码剩余次数结果----->" + keyTimes);

        Integer timesByUsername = userMapper.getTimesByUsername(username);



        if(ObjectUtil.isNotNull(keyTimes)){
            if(keyTimes <= 0){
                return Result.error().message("激活码次数已用完");
            }
            userMapper.updatetimes(keyTimes + timesByUsername,username);
            userMapper.updatekey(regCode,0);
            return Result.ok().message("充值成功");
        }else{
            return Result.error().message("激活码不存在");
        }

    }
}
