package com.ctjsoft.orderserver.analyze.mapper;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDicionaryLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JxStdDatabaseDicionaryLogMapper {

    void batchInsert(List<JxStdDatabaseDicionaryLog> stdDatabaseDicionaryLogList);

    List<JxStdDatabaseDicionaryLog> detail(String tableId);

    List<JxStdDatabaseDicionaryLog> detailByTableIdAndDateAndColumnName(@Param("tableId") String tableId, @Param("today") String today, @Param("type") String type, @Param("columnName") String columnName);

    List<JxStdDatabaseDicionaryLog> detailBySqls(@Param("sqls") String  sqls , @Param("dbId") String  dbId, @Param("today") String  today  );

    void update(@Param("id") String id, @Param("orderId") String orderId);


}