package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class JxStdDatabase implements Serializable {
    private String id;

    private String code;

    private String name;

    private Integer type;

    private String driver;

    private String url;

    private String username;

    private String pwd;

    private String des;

    private String modelName;

    private String business;

    private String version;

    private String companyCode;

    private String deteceFrequency;

    private String rate;

    private String isSchedule;

    private String cron;

    private static final long serialVersionUID = 1L;
}