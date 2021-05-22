package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zyx
 * @date 2020/12/21
 */
@Data
public class DatabaseScanObject {

    List<JxStdDatabaseQuestionRecord> questionRecords = new ArrayList<>(); //问题记录
    List<JxStdDatabaseDataDicChangeRecord> changeRecords = new ArrayList<>(); //改变记录
    List<JxStdDatabaseDataDicionary> updateDicRecords = new ArrayList<>(); //问题字典记录
    List<JxStdDatabaseDataDicionary> insertDicRecords = new ArrayList<>(); //新增的问题字典
    List<JxStdDatabaseDataDicionary> deleteDicRecords = new ArrayList<>(); //删除的问题字典
    List<JxStdDatabaseDataDicionaryColumn> updateColumnsRecord = new ArrayList<>(); //修改字段记录
    List<JxStdDatabaseDataDicionaryColumn> insertColumnsRecord = new ArrayList<>(); //新增字段记录
    List<JxStdDatabaseDataDicionaryColumn> deleteColumnsRecord = new ArrayList<>(); //删除字段记录
    List<JxStdDatabaseDicionaryLog> dicionaryLogRecords = new ArrayList(); //变更日志


    List<JxStdDatabaseDataDicionary> tableRowNumChange = new ArrayList(); //行数变更
    String scanStatus = "";
    String info = "";

}
