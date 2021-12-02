package com.zyx.crackgameserver.modules.spider.utils;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.MD5;
import org.apache.catalina.security.SecurityUtil;

public class MD5Utils {

    public static String randomMD5(){
        return SecureUtil.md5(RandomUtil.randomString(128));

    }

}
