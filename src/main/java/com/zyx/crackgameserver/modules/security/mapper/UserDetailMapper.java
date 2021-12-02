package com.zyx.crackgameserver.modules.security.mapper;

import com.zyx.crackgameserver.modules.security.model.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface UserDetailMapper extends Mapper<SysUser> {


    @Select("SELECT * FROM sys_user WHERE username = #{username}")
    SysUser finUserByName(@Param("username") String username);

    
    @Select("SELECT " +
            "   code " +
            "FROM " +
            "   sys_user_role ur  " +
            "   LEFT JOIN sys_user u ON ur.user_id = u.id  " +
            "   LEFT JOIN sys_role r ON ur.role_id = r.id  " +
            "WHERE " +
            "   username = #{username}")
    List<String> getRoleCodeByname(@Param("username") String username);

}
