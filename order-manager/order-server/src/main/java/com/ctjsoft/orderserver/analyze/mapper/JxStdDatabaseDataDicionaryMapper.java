package com.ctjsoft.orderserver.analyze.mapper;

 import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionary;
import org.apache.ibatis.annotations.Param;
 import org.mapstruct.Mapper;

 import java.util.HashMap;
import java.util.List;

//@Repository
@Mapper
public interface JxStdDatabaseDataDicionaryMapper {

    void batchInsert(List<JxStdDatabaseDataDicionary> list);

    void update(JxStdDatabaseDataDicionary stdDatabaseDataDicionary);

    List<JxStdDatabaseDataDicionary> list();

    List<JxStdDatabaseDataDicionary> loadByDatabaseId(String databaseId);

    void batchUpdate(List<JxStdDatabaseDataDicionary> list);

    void tableRowNumChange(List<JxStdDatabaseDataDicionary> list);

    void batchDelete(List<JxStdDatabaseDataDicionary> list);

    List<HashMap<String, Object>> listByGroupCount();

    List<JxStdDatabaseDataDicionary> loadByDbIdAndKeyword( @Param("databaseId") String databaseId,  @Param("keyword") String keyword);

    List<JxStdDatabaseDataDicionary> loadByDbIdAndKeywordPage(
            @Param("databaseId") String databaseId,  @Param("keyword")  String keyword,
            @Param("page") String page,  @Param("limit")  String limit);

    List<JxStdDatabaseDataDicionary> selectByDbIdAndTableName( @Param("databaseId") String databaseId,  @Param("tableName") String tableName);

    String loadByDbIdAndKeywordCount(  @Param("databaseId") String databaseId,  @Param("keyword")  String keyword);
}