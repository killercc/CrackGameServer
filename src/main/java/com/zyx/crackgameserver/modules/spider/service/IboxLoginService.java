package com.zyx.crackgameserver.modules.spider.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyx.crackgameserver.modules.spider.mapper.IBoxUserMapper;
import com.zyx.crackgameserver.modules.spider.model.IBoxUserDetail;
import com.zyx.crackgameserver.modules.spider.model.IboxUser;
import com.zyx.crackgameserver.modules.spider.utils.MD5Utils;
import com.zyx.crackgameserver.response.Result;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IboxLoginService {


    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private IBoxUserMapper iBoxUserMapper;

    @SneakyThrows
    public Result sendMsg(String phonenum){
        if(StringUtils.isNotBlank(phonenum)){
            HttpPost httpPost = new HttpPost("https://www.ibox.art/nft-mall-web/nft/user/sendSMSCode");
            httpPost.setHeader("user-agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Mobile Safari/537.36");
            httpPost.setHeader("content-type","application/json;charset=UTF-8");
            String params = "{phoneNumber: \""+phonenum+"\", transId: \""+ MD5Utils.randomMD5() +"\"}";
            StringEntity stringEntity = new StringEntity(params);
            httpPost.setEntity(stringEntity);
            CloseableHttpResponse execute = httpClient.execute(httpPost);
            try {
                JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(execute.getEntity()));
                boolean success = jsonObject.getBooleanValue("success");
                if(success){
                    return Result.ok().message("发送成功");
                }else{
                    return Result.ok().message("发送失败");
                }
            }catch (Exception e){
                return Result.error().message("异常！");
            }


        }else{
            return Result.error();
        }

    }

    @SneakyThrows
    public Result login(IboxUser iboxUser){
        String phonenum = iboxUser.getPhonenum();
        String code = iboxUser.getCode();
        if(!StringUtils.isNoneBlank(phonenum,code))
            return Result.error().message("手机号或验证码为空！");
        HttpPost httpPost = new HttpPost("https://www.ibox.art/nft-mall-web/v1/nft/user/login");
        httpPost.setHeader("user-agent","Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Mobile Safari/537.36");
        httpPost.setHeader("content-type","application/json;charset=UTF-8");
        String params = "{\"phoneNumber\":\""+phonenum+"\",\"code\":\""+code+"\",\"transId\":\""+ MD5Utils.randomMD5() +"\"}";
        StringEntity stringEntity = new StringEntity(params);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse execute = httpClient.execute(httpPost);
        //System.out.println(EntityUtils.toString(execute.getEntity()));
        try {
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(execute.getEntity()));
            String token = jsonObject.getJSONObject("data").getString("token");
            boolean loginsuccess = jsonObject.getBooleanValue("success");
            if(loginsuccess){
                iBoxUserMapper.tokenOperate(phonenum,token);
                return Result.ok().message("登录成功");
            }else{
                return Result.ok().message("登录失败");
            }
        }catch (Exception e){
            return Result.error().message("异常！");
        }


    }


}
