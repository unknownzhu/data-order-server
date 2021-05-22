package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class StdDatabase implements Serializable {
	
	//数据库类型
	/**
	 * 生产库 1 
	 */
	public static final Integer DATABASE_TYPE_SCK = 1; 
	/**
	 * 前置库 2
	 */
	public static final Integer DATABASE_TYPE_QZK = 2;
	/**
	 * 中心库 3
	 */
	public static final Integer DATABASE_TYPE_ZXK = 3;

    public static final String TABLE_NAME = "TABLE_NAME";
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

    private static final long serialVersionUID = 1L;
}