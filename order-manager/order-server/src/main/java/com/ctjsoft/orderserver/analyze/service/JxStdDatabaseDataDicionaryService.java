package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionary;
import com.ctjsoft.orderserver.analyze.domain.StdTable;

import java.util.List;


/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
public interface JxStdDatabaseDataDicionaryService {

    //void save(StdDatabaseDataDicionary stdDatabaseDataDicionary);

    List<JxStdDatabaseDataDicionary> loadByDatabaseId(String databaseId);


    List<JxStdDatabaseDataDicionary> reScan(String databaseId) throws Exception;


    void save(List<JxStdDatabaseDataDicionary> list);

    List<JxStdDatabaseDataDicionary> loadByDbIdAndKeyword(String databaseId, String keyword);

    List<JxStdDatabaseDataDicionary> loadByDbIdAndKeywordPage(String databaseId, String keyword, String page, String limit);

    String loadByDbIdAndKeywordCount(String databaseId, String keyword);

    List<JxStdDatabaseDataDicionary> selectByDbIdAndTableName(String databaseId, String tableName);

/*
    List<StdTable> getCzbTable();*/
}
