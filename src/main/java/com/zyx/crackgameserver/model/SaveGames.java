package com.zyx.crackgameserver.model;

import lombok.Data;

@Data
public class SaveGames {
    /**
     * 游戏名称C
     */
    private String Gname;

    /**
     * 游戏名称E
     */
    private String Gdescribe;

    /**
     * 游戏激活码
     */
    private String Gmmss;

    /**
     * 百度云地址
     */
    private String Gbd;

    /**
     * 天翼云地址
     */
    private String Gty;
}
