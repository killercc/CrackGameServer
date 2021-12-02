package com.zyx.crackgameserver.mapper;

import com.zyx.crackgameserver.model.DownloadEntity;
import com.zyx.crackgameserver.model.RegCode;
import com.zyx.crackgameserver.model.SaveGames;
import com.zyx.crackgameserver.modules.security.model.SysUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserMapper extends Mapper<SysUser> {
    @Select(value = "SELECT downloadTimes FROM sys_user WHERE username = #{username}")
    Integer getTimesByUsername(@Param(value = "username") String username);

    @Select("SELECT times FROM regcode WHERE regKey = #{regCode}")
    Integer getKeyTimes(@Param(value = "regCode")String regCode);

    @Update(value = "UPDATE sys_user SET sys_user.downloadTimes = #{times} WHERE username = #{username}")
    void updatetimes(@Param(value = "times")Integer times, @Param(value = "username")String username);

    @Update(value = "UPDATE regcode SET times = #{times} WHERE regKey = #{regCode}")
    void updatekey(@Param(value = "regCode")String regCode,@Param(value = "times")Integer times);


    @Insert(value =" <script> " +
            " INSERT INTO regcode " +
            " (regKey,times) " +
            " VALUES " +
            " <foreach collection='list' item='RegCode' separator=','  > " +
            " (#{RegCode.regkey},#{RegCode.times}) " +
            " </foreach> "+
            " </script> ")
    void insertkeys(@Param(value = "list") List<RegCode> regCodeList);


    @Insert(value = "REPLACE INTO sys_get_games " +
            " (ugmd5,username,Gcode,createtime,endtime) " +
            " VALUES(#{ugmd5},#{username},#{gcode},REPLACE(unix_timestamp(current_timestamp(3)),'.',''),REPLACE(unix_timestamp(current_timestamp(3)),'.','')+ #{savetime})")
    void savegames(@Param(value = "ugmd5")String ugmd5,
                   @Param(value = "gcode")String gcode,
                   @Param(value = "username")String username,
                   @Param(value = "savetime")Integer savetime);


    @Select(value = "SELECT " +
            "Gname,Gdescribe,Gmmss,Gbd,Gty " +
            " FROM gamelist ur " +
            " LEFT JOIN sys_get_games u ON ur.Gcode = u.Gcode " +
            " WHERE " +
            " username = #{username} AND endtime > REPLACE(unix_timestamp(current_timestamp(3)),'.','')")
    List<SaveGames> findSavegames(@Param("username")String username);
}
