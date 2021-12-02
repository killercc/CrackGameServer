package com.zyx.crackgameserver.mapper;

import com.zyx.crackgameserver.model.Gentity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface GameMapper extends Mapper<Gentity> {
    @Select(value = "SELECT Gcode,Gname,Gdescribe,Grlzz FROM gamelist WHERE Gname LIKE concat('%',#{gname},'%')")
    List<Gentity> findgame(@Param(value = "gname") String gname);


}
