package com.ctjsoft.orderserver.analyze.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class JxStdDatabaseDicionaryLog {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    private Integer type;
    private String remark;
    private String tableId;
    private String columnName;
    private String oldColumnName;
    private String orderId;
    private String recordId;
    private String tableName;
    private String dbId;


    public JxStdDatabaseDicionaryLog(){

    }

    public JxStdDatabaseDicionaryLog(String id, Date createTime, Integer type, String remark, String tableId,String tableName,String dbId,String columnName,String oldColumnName,String recordId){
        this.id = id;
        this.createTime = createTime;
        this.type = type;
        this.remark = remark;
        this.tableId = tableId;
        this.columnName = columnName;
        this.oldColumnName = oldColumnName;
        this.recordId = recordId;
        this.tableName = tableName;
        this.dbId = dbId;
    }

/*    public JxStdDatabaseDicionaryLog(String id, Date createTime, Integer type, String remark, String tableId ,String columnName,String oldColumnName,String recordId){
        this.id = id;
        this.createTime = createTime;
        this.type = type;
        this.remark = remark;
        this.tableId = tableId;
        this.columnName = columnName;
        this.oldColumnName = oldColumnName;
        this.recordId = recordId;
    }*/
}
