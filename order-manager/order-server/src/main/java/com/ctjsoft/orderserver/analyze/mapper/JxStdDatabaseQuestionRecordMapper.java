package com.ctjsoft.orderserver.analyze.mapper;



import com.ctjsoft.orderserver.analyze.domain.JxCompareVo;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionary;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseQuestionRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface JxStdDatabaseQuestionRecordMapper {
    void insert(JxStdDatabaseQuestionRecord qRecord);

    List<JxStdDatabaseQuestionRecord> list();

    List<JxStdDatabaseQuestionRecord> loadByDatabaseId(String databaseId);

    void batchInsert(List<JxStdDatabaseQuestionRecord> list);

    void solveById(String id);

    List<HashMap<String, Object>> listByGroupCount();

    void update(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord);

   /* void updateOrderID(@Param("id") String id, @Param("orderId") String orderId);*/


    List<JxStdDatabaseQuestionRecord> loadByDbIdAndKeyword(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord);
    String loadByDbIdAndKeywordCount(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord);

    List<JxStdDatabaseQuestionRecord> listDistinctTableNameByDbId(@Param("databaseId") String databaseId, @Param("keyword") String keyword);

    List<JxStdDatabaseQuestionRecord> listDistinctColumnNameByDbIdAndTbaleName(
            @Param("databaseId") String databaseId, @Param("tableName") String tableName);


    List<JxCompareVo> loadJxCompareVo(JxCompareVo jxCompareVo);


    List<JxCompareVo> loadJxCompareVoPlus(@Param("databaseId") String databaseId, @Param("remak") String remak
            , @Param("tableName") String tableName, @Param("name") String name , @Param("page") Integer page, @Param("limit") Integer limit, @Param("status") String status );

    String getCount(  @Param("databaseId") String databaseId, @Param("remak") String remak
            , @Param("tableName") String tableName, @Param("name") String name ,@Param("status") String status );


}