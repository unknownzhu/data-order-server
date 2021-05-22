package com.ctjsoft.orderserver.analyze.domain;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class DataSourceEntity implements Serializable {

	private static final long serialVersionUID = 257181222904777454L;

	private String id;
	// 数据源名称
	private String name;
	// 数据源类型mysql、oracle、hive
	private String type;
	// 版本
	private String version;
	// 数据分层
	private String dataTiering;
	// 数据库连接地址
	private String url;
	// 数据库地址全称
	private String fullUrl;
	// 数据库名
	private String dbName;
	// 驱动名称
	private String driverClassName;
	// 数据库用户名
	private String userName;
	// 用户密码
	private String password;
	// 创建人id
	private String createUserId;
	// 创建人姓名
	private String createUserName;
	// 创建时间
	private Date createTime;
	// 修改人id
	private String updateUserId;
	// 修改人姓名
	private String updateUserName;
	// 修改时间
	private Date updateTime;
	
	private String isDel;
	// 最大连接数
	private int maxActive;
	// 最大等待时间
	private int maxWait;
	// 备注
	private String remark;
	
	private String source;
	
	private String sourceId;
}
