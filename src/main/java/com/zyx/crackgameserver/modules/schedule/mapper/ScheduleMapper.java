package com.zyx.crackgameserver.modules.schedule.mapper;

import com.zyx.crackgameserver.modules.schedule.model.SchedulePojo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;


import java.util.List;
public interface ScheduleMapper extends Mapper<SchedulePojo> {

    @Select("SELECT * FROM schedule WHERE status = #{status}")
    List<SchedulePojo> getJobStatus(@Param("status") Integer status);


}
