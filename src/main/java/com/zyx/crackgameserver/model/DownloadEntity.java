package com.zyx.crackgameserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "gamelist")
@Data
public class DownloadEntity implements Serializable {

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
