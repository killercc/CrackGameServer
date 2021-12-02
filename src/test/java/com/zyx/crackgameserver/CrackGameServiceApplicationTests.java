package com.zyx.crackgameserver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyx.crackgameserver.modules.redis.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@SpringBootTest
class CrackGameServiceApplicationTests {

    @Autowired
    private RedisUtils redisUtils;
    @Test
    void contextLoads() {
    }

    @Test
    void redistest(){

        /**                 set集合  设置永久生效
         * savegameindex:userid == >  [userid_gamecode0,userid_gamecode1,userid_gamecodeN]
         *
         * savegamecontent:userid_gamecode0 ===> {body0} ttl0
         * savegamecontent:userid_gamecode1 ===> {body1} ttl1
         *
         */
//        String username = "xiaoge";
//
//        //下载游戏id数组
//        // {"Gcode":"123","Gname":"GTA"}
//        redisUtils.sSet("savegameindex:"+username ,"GTA4");
//        redisUtils.sSet("savegameindex:"+username , "GTA5");
//
//        //List<Object> objects = redisUtils.lGet("savegames:" + username, 0, -1);
//        Set<Object> objects = redisUtils.sGet("savegameindex:" + username);
//        assert objects != null;
//        int i = 0;
//        for (Object object : objects) {
//            redisUtils.setIfAbsent("savegamecontent:"+username+"_"+object,"{\"Gcode\":\"123\",\"Gname\":\"GTA\"}",10);
//            ++i;
//        }
//
//        List<String> keys = new ArrayList<>();
//        for (Object object : objects) {
//            keys.add("savegamecontent:" + username + "_" + object);
//
//        }

        System.out.println("git test");
        Object mget = redisUtils.get("savegamecontent:xiaoge_D58");
        //JSONObject jsonObject = JSONObject.parseObject(mget);
        System.out.println(mget.toString());
//        for (Object o : mget) {
//            System.out.println(o);
//        }





//        assert objects != null;
//        for (Object object : objects) {
//            System.out.println((String) object);
//        }
    }

}
