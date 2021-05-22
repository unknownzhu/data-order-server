package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

@Data
public class JxStdDatabaseDataDicionaryColumn {
    private String id;
    private String name;
    private String chineseName;
    private String type;
    private Integer length;
    private String accuracy;
    private String isPk;
    private String isEmpty;
    private String defaultValue;
    private String codeSet;
    private String libraryTable;
    private String tableName;
    private String tableId;
    private String databaseId;
    private String remark;
    private String logicalTables;

    public JxStdDatabaseDataDicionaryColumn(){

    }

    public JxStdDatabaseDataDicionaryColumn(String id, String tableId, String name, String type, Integer length,
                                            String accuracy, String isPk, String isEmpty, String defaultValue,
                                            String chineseName, String tableName, String databaseId,String logicalTables){
        this.id = id;
        this.tableId = tableId;
        this.name = name;
        this.type = type;
        this.length = length;
        this.accuracy = accuracy;
        this.isPk = isPk;
        this.isEmpty = isEmpty;
        this.defaultValue = defaultValue;
        this.chineseName = chineseName;
        this.tableName = tableName;
        this.databaseId = databaseId;
        this.logicalTables = logicalTables;
    }
    public JxStdDatabaseDataDicionaryColumn(String id, String tableId, String name, String type, Integer length,
                                            String accuracy, String isPk, String isEmpty, String defaultValue,
                                            String chineseName, String tableName, String databaseId){
        this.id = id;
        this.tableId = tableId;
        this.name = name;
        this.type = type;
        this.length = length;
        this.accuracy = accuracy;
        this.isPk = isPk;
        this.isEmpty = isEmpty;
        this.defaultValue = defaultValue;
        this.chineseName = chineseName;
        this.tableName = tableName;
        this.databaseId = databaseId;
    }
}
