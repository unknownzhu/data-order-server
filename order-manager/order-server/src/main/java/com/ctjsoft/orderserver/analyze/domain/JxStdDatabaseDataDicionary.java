package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

@Data
public class JxStdDatabaseDataDicionary {
    private String id;
    private String tableName;
    private String chineseName;
    private String type;
    private String entityName;
    private String logicalTable;
    private String businessClassfi;
    private String detectionResult;
    private String illustrate;
    private String databaseId;
    private String tableRowSize;
    private String tableRowChangeTime;
}

