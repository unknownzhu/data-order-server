package com.ctjsoft.orderserver.analyze.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
public class JxStdDatabaseQuestionRecord {
    private String id;
    private String tableName;
    private String columnName;
    private String type;
    private String databaseId;
    private String keyword;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    private String status;
    private String entityName;

    private String tableId;

    private String isEmpty;
    private String isPk;
    private Integer length;
    private String defaultValue;
    private String columnType;
    private Integer page;
    private Integer limit;


    private String isNewest;
    private String chineseName;
    private String isPrimary;
    private String REMARK;
    private String orderId;
    private String scanId;


    public JxStdDatabaseQuestionRecord(){

    }
    public JxStdDatabaseQuestionRecord(String id, String tableName, String columnName, String type, String databaseId, Date createTime,
                                       String isEmpty,String isPk,Integer length,String defaultValue,String columnType,String tableId
    ){
        this.id = id;
        this.tableName = tableName;
        this.columnName = columnName;
        this.type = type;
        this.databaseId = databaseId;
        this.createTime = createTime;
        this.tableId = tableId;
        this.isEmpty = isEmpty;
        this.isPk = isPk;
        this.length = length;
        this.defaultValue = defaultValue;
        this.columnType = columnType;
    }

    public JxStdDatabaseQuestionRecord(String id, String tableName, String columnName, String type, String databaseId, Date createTime
    ){
        this.id = id;
        this.tableName = tableName;
        this.columnName = columnName;
        this.type = type;
        this.databaseId = databaseId;
        this.createTime = createTime;

    }


}
