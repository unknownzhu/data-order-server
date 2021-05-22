package com.ctjsoft.orderserver.analyze.mapper;

 import com.ctjsoft.orderserver.analyze.domain.JxScoreRelationDetailVo;
 import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JxStdScoreVersionMapper {

    List<JxScoreRelationDetailVo> jxSelectRelation(@Param("parentCode") String parentCode, @Param("mark") String mark);

    List<JxScoreRelationDetailVo> jxSecondLevelSelectRelation(@Param("parentCode") String parentCode, @Param("mark") String mark);

}