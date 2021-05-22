package com.ctjsoft.orderserver.analyze.mapper;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionaryColumn;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JxStdDatabaseDataDicColumMapper {

    void batchInsert(List<JxStdDatabaseDataDicionaryColumn> list);

    List<JxStdDatabaseDataDicionaryColumn> selectByTableId(String tableId);


    List<JxStdDatabaseDataDicionaryColumn> list();

    List<JxStdDatabaseDataDicionaryColumn> loadByDatabaseId(String databaseId);

    void update(JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn);

    void batchDelete(List<JxStdDatabaseDataDicionaryColumn> list);

    void batchUpdate(List<JxStdDatabaseDataDicionaryColumn> list);

    void batchUpdateTableRowNum(List<JxStdDatabaseDataDicionaryColumn> list);
}