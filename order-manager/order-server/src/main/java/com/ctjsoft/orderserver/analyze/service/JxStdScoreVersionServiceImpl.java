package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.domain.JxScoreRelationDetailVo;
import com.ctjsoft.orderserver.analyze.mapper.JxStdScoreVersionMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyx
 * @date 2020/11/25
 */
@Service
public class JxStdScoreVersionServiceImpl {
    @Autowired
    private JxStdScoreVersionMapper jxStdScoreVersionMapper;

 //   @Autowired
 //   private StdScoreVersionMapper stdScoreVersionMapper;
//
 //   @Autowired
 //   private StdScoreRelationMapper stdScoreRelationMapper;

    public List<JxScoreRelationDetailVo> jxDetail(String mark) {
        List<JxScoreRelationDetailVo> firstContents = this.jxStdScoreVersionMapper.jxSelectRelation("0", null);
        List<JxScoreRelationDetailVo> result = new ArrayList<>(firstContents.size());
        for (JxScoreRelationDetailVo firstContent : firstContents) {
            List<JxScoreRelationDetailVo> secondContents = this.jxStdScoreVersionMapper.jxSecondLevelSelectRelation(firstContent.getCode(), mark);
            firstContent.setChildren(secondContents);
            result.add(firstContent);
        }
        return result;
    }

      public List<Object> getList(String mark) {
        List<JxScoreRelationDetailVo> firstContents = this.jxStdScoreVersionMapper.jxSelectRelation("0", null);
        //List<JxScoreRelationDetailVo> result = new ArrayList<>(firstContents.size());
        List<Object> result = new ArrayList<>();
        for (JxScoreRelationDetailVo firstContent : firstContents) {
            List<JxScoreRelationDetailVo> secondContents = this.jxStdScoreVersionMapper.jxSecondLevelSelectRelation(firstContent.getCode(), mark);

            Integer shengchanNum = 0;
            Integer huizongNum = 0;
            for (JxScoreRelationDetailVo jxScoreRelationDetailVo : secondContents) {
                if (jxScoreRelationDetailVo.getStdType().equals("0")) {
                    shengchanNum++;
                }else if (jxScoreRelationDetailVo.getStdType().equals("1")) {
                    huizongNum++;
                }
            }
            List<Object> objectList = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            BeanUtils.copyProperties(secondContents, objectList );
            map.put("children", secondContents);
            map.put("shengchanNum", shengchanNum);
            map.put("huizongNum", huizongNum);
            result.add(map);
        }
        return result;
    }

/*    @Transactional(rollbackFor = Exception.class)
    public void update(List<ScoreRelationSaveReqDto> list) {
        StdScoreVersion stdScoreVersion = stdScoreVersionMapper.getMaxScoreVersion();
        for (ScoreRelationSaveReqDto scoreRelationSaveReq : list) {
            StdScoreRelation stdScoreRelation = new StdScoreRelation();
            stdScoreRelation.setId(ScUtil.getUUID());
            stdScoreRelation.setScore(scoreRelationSaveReq.getScore());
            stdScoreRelation.setScoreContentId(scoreRelationSaveReq.getScoreContentId());
            stdScoreRelation.setScoreVersionId(stdScoreVersion.getId());
            stdScoreRelation.setProportion(scoreRelationSaveReq.getProportion());
            this.stdScoreRelationMapper.updateByContentIdAndVersionId(stdScoreRelation);
        }
    }*/


}

