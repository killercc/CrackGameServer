package com.zyx.crackgameserver.modules.schedule.service;


import com.zyx.crackgameserver.modules.schedule.mapper.ScheduleMapper;
import com.zyx.crackgameserver.modules.schedule.model.SchedulePojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    public List<SchedulePojo> getJobStatus(Integer status){
        return scheduleMapper.getJobStatus(status);
    }

}
