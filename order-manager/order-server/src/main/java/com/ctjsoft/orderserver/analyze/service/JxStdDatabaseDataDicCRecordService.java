package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicChangeRecord;

import java.util.List;
import java.util.Map;

/**
 * @author gyx
 * @date 2020-11-12
 * @desc
 */
public interface JxStdDatabaseDataDicCRecordService {

    void save(JxStdDatabaseDataDicChangeRecord stdDatabaseDataDicChangeRecord);

    List<JxStdDatabaseDataDicChangeRecord> search(Map<String, String> params);

    List<JxStdDatabaseDataDicChangeRecord> loadByDatabaseId(String databaseId);

    List<JxStdDatabaseDataDicChangeRecord> loadByDbIdAndKeyword(String databaseId, String keyword,String page,String limit);

    String loadByDbIdAndKeywordCount(String databaseId, String keyword);
}
