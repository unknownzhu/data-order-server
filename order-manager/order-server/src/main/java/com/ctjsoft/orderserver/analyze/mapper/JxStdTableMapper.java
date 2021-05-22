package com.ctjsoft.orderserver.analyze.mapper;

import com.ctjsoft.orderserver.analyze.domain.StdTable;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zyx
 * @date 2020/11/11
 * @desc
 */
@Repository
public interface JxStdTableMapper {


    List<StdTable> selectCZBTable(@Param("isDeleted") Integer isDeleted, @Param("version") long version,
                                  @Param("stdType") Integer stdType, @Param("businessEnumList") List<String> businessEnumList);


    List<StdTable>  getEvaluaAreaByDataBaseId(@Param("dataBaseId") String dataBaseId);


}
