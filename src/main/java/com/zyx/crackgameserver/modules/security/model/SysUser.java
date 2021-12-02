package com.zyx.crackgameserver.modules.security.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "sys_user")
@Data
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    private String password;

    private Integer downloadtimes;

    @Transient
    private String code;

    @Transient
    private String codeuuid;

}
