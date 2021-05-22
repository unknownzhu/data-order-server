package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.domain.JxCompareVo;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseQuestionRecord;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
public interface JxStdDatabaseQuestionRecordService {
    List<JxStdDatabaseQuestionRecord> loadByDatabaseId(String databaseId);

    void solveById(String id);

    List<JxStdDatabaseQuestionRecord> loadByDbIdAndKeyword(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord);

    String loadByDbIdAndKeywordCount(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord);

    List<JxStdDatabaseQuestionRecord> listDistinctTableNameByDbId(String databaseId, String keyword);

    List<JxStdDatabaseQuestionRecord> listDistinctColumnNameByDbIdAndTbaleName(String databaseId, String tableName);


    List<JxCompareVo> loadJxCompareVo(JxCompareVo jxCompareVo);

    List<JxCompareVo> loadJxCompareVoPlus(String databaseId, String remak, String tableName, String name, Integer page, Integer limit,String status);

    String getCount(String databaseId, String remak, String tableName, String name,String status);



}
