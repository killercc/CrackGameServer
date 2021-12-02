package com.zyx.crackgameserver.modules.schedule.config;

import com.zyx.crackgameserver.modules.schedule.task.ScheduleTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class CornTaskRegister implements DisposableBean {

    //保存所有定时任务
    private final Map<Runnable, ScheduleTask> scheduleTaskMap = new ConcurrentHashMap<>(16);

    @Autowired
    TaskScheduler taskScheduler;

    /**
     * 添加定时任务
     */
    public void addCorntask(Runnable task, String cornExpression){
        if(StringUtils.isNoneBlank(cornExpression)){
            addCorntask(new CronTask(task,cornExpression));
        }
    }

    private void addCorntask(CronTask cronTask) {
        if(cronTask != null){
            Runnable runnable = cronTask.getRunnable();
            if(scheduleTaskMap.containsKey(runnable)){
                removeCornTask(runnable);
            }
            scheduleTaskMap.put(runnable,scheduleCornTask(cronTask));
        }
    }

    private ScheduleTask scheduleCornTask(CronTask cronTask) {
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.future = taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduleTask;

    }

    private void removeCornTask(Runnable runnable) {
        ScheduleTask scheduleTask = scheduleTaskMap.remove(runnable);
        scheduleTask.cancel();
    }


    @Override
    public void destroy() throws Exception {
        for (ScheduleTask task:
             scheduleTaskMap.values()) {
            task.cancel();
        }
        scheduleTaskMap.clear();
    }
}
