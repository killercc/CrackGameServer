package com.zyx.crackgameserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "gamelist")
@Data
public class Gentity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * 游戏代码
     */
    private String Gcode;

    /**
     * 游戏中文名称
     */
    private String Gname;

    /**
     * 游戏英文名
     */
    private String Gdescribe;

    /**
     * 游戏容量
     */
    private String Grlzz;



}
