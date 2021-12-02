package com.zyx.crackgameserver.modules.schedule.task;




import java.util.concurrent.ScheduledFuture;

public class ScheduleTask {

    public volatile ScheduledFuture<?> future;

    public void cancel(){
        ScheduledFuture<?> future = this.future;
        if(future != null){
            future.cancel(true);

        }
    }
}
