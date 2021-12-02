package com.zyx.crackgameserver.mapper;

import com.zyx.crackgameserver.model.DownloadEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface DownloadMapper extends Mapper<DownloadEntity> {

    @Select("SELECT Gname,Gdescribe,Gmmss,Gbd,Gty from gamelist WHERE Gcode = #{gcode}")
    DownloadEntity downloadByCode(@Param(value = "gcode")String gcode);
}
