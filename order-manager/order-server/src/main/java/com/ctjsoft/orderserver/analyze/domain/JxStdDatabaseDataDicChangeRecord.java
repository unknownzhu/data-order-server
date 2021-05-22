package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

@Data
public class JxStdDatabaseDataDicChangeRecord {
    private String id;
    private String tableId;
    private String tableName;
    private Integer type;
    private String remark;
    private String databaseId;

    public JxStdDatabaseDataDicChangeRecord(){

    }

    public JxStdDatabaseDataDicChangeRecord(String id, String tableId, String tableName, Integer type, String remark, String databaseId){
        this.id = id;
        this.tableId = tableId;
        this.tableName = tableName;
        this.type = type;
        this.remark = remark;
        this.databaseId = databaseId;
    }
}
