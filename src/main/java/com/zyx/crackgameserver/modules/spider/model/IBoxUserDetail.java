package com.zyx.crackgameserver.modules.spider.model;


import lombok.Data;


import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "iboxtoken")
public class IBoxUserDetail {


    @Id
    private String phonenum;

    private String token;
}
