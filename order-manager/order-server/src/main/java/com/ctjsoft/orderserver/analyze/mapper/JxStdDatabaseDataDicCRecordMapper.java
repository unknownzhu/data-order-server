package com.ctjsoft.orderserver.analyze.mapper;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicChangeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public interface JxStdDatabaseDataDicCRecordMapper {

    void insert(JxStdDatabaseDataDicChangeRecord stdDatabaseDataDicChangeRecord);

    List<JxStdDatabaseDataDicChangeRecord> search(Map<String, String> params);

    List<JxStdDatabaseDataDicChangeRecord> list();

    List<JxStdDatabaseDataDicChangeRecord> loadByDatabaseId(String databaseId);

    void batchInsert(List<JxStdDatabaseDataDicChangeRecord> changeRecords);

    List<HashMap<String, Object>> listByGroupCount();

    List<JxStdDatabaseDataDicChangeRecord> loadByDbIdAndKeyword(
            @Param("databaseId") String databaseId, @Param("keyword") String keyword,
            @Param("page") String page,@Param("limit") String limit  );

    String loadByDbIdAndKeywordCount(  @Param("databaseId") String databaseId, @Param("keyword") String keyword  );


}