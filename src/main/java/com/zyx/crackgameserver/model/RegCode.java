package com.zyx.crackgameserver.model;


import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "regcode")
@Data
public class RegCode {

    @Id
    private String regkey;

    private Integer times;


}
