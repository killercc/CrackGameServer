package com.zyx.crackgameserver.modules.schedule.task;

import org.springframework.stereotype.Component;

@Component
public class testRun {

    public void testwithparams(String param){
        System.out.println("有参任务执行" + param);
    }
    public void testwithparams(){
        System.out.println("无参任务执行");
    }
}
