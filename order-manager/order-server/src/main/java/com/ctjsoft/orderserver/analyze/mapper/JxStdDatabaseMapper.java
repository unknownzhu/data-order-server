package com.ctjsoft.orderserver.analyze.mapper;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabase;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseExtendRelationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zyx
 * @date 2020/11/11
 * @desc
 */
@Repository
public interface JxStdDatabaseMapper {
    int insert(JxStdDatabase record);


    JxStdDatabase selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JxStdDatabase record);

    List<JxStdDatabaseExtendRelationVo> search(@Param("search") String search, @Param("companyCodeList") List<String> companyCodeList);

    List<JxStdDatabase> list();

    List<JxStdDatabase> loadSearch(@Param("search") String search);

    List<JxStdDatabase> getScheduleDatabaseByType(@Param("list") List<Integer> typeList);

    List<String> getBusinessList(JxStdDatabase stdDatabase);

    List<String> selectName();

    List<String> selectNameRemoveSelf(String id);
}
