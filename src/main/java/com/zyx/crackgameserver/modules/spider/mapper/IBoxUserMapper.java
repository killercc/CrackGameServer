package com.zyx.crackgameserver.modules.spider.mapper;

import com.zyx.crackgameserver.modules.spider.model.IBoxUserDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

public interface IBoxUserMapper extends Mapper<IBoxUserDetail> {
    @Update("REPLACE INTO iboxtoken (phonenum,token) VALUES(#{phonenum}, #{token})")
    public void tokenOperate(@Param("phonenum")String phonenum,
                             @Param("token")String token);
}
