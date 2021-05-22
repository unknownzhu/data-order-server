package com.ctjsoft.orderserver.analyze.domain;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class DbInfo {
	private String id;
	// 数据源类型oracle、mysql、hive
	private String type;
	// 版本号
	private String version;
	// ip + 端口
	@Length(max=100,message="数据源URL长度不能超过100个字符")
    private String url;
    // 数据库名
	@Length(max=100,message="数据库名长度不能超过100个字符")
    private String dbName;
    // 数据库驱动名称
	@Length(max=100,message="数据库驱动名长度不能超过100个字符")
    private String driverClassName;
    // 用户名
	@Length(max=100,message="用户名长度不能超过100个字符")
    private String userName;
    // 密码
	@Length(max=100,message="密码长度不能超过100个字符")
    private String password;
    // 表名
    private String tableName;
    // 数据库分级
	@Length(max=40,message="数据库分级长度不能超过40个字符")
    private String dataTiering;
    // 最大连接数
	@Max(value=9999,message ="最大连接数不超过4位")
 	private int maxActive;
 	// 最大等待时间
	@Max(value=9999,message="最大等待时间不超过4位")
 	private int maxWait;
 	// 备注
	@Length(max=500,message="备注长度不能超过500个字符")
 	private String remark; 
 	// 数据源名称
	@Length(max=100,message="数据源URL长度不能超过100个字符")
 	private String name;
 	// 修改人id
	private String updateUserId;
	// 修改人姓名
	private String updateUserName;
	// 创建人id
	private String createUserId;
	// 创建人姓名
	private String createUserName;
	
	private String source;
	
	private String sourceId;
}
