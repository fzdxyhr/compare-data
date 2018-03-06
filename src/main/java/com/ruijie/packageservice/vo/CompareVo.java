package com.ruijie.packageservice.vo;

import lombok.Data;

/**
 * Created by YHR on 2018/3/5.
 */

@Data
public class CompareVo {

    private String userName;

    private String idCard;

    private Long localSum;

    private Long remoteSum;
    //状态 1 金额不平，2 本地不存在，3 医保中心不存在
    private Integer status;

}
