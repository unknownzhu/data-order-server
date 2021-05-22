package com.ctjsoft.orderserver.analyze.service.impl;


import com.ctjsoft.orderserver.analyze.domain.JxCompareVo;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseQuestionRecord;
import com.ctjsoft.orderserver.analyze.mapper.JxStdDatabaseQuestionRecordMapper;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseQuestionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@Service
public class JxStdDatabaseQuestionRecordServiceImpl implements JxStdDatabaseQuestionRecordService {

    @Autowired
    private JxStdDatabaseQuestionRecordMapper stdDatabaseQuestionRecordMapper;

    @Override
    public List<JxStdDatabaseQuestionRecord> loadByDatabaseId(String databaseId) {
        return stdDatabaseQuestionRecordMapper.loadByDatabaseId(databaseId);
    }

    @Override
    public void solveById(String id) {
        stdDatabaseQuestionRecordMapper.solveById(id);
    }

    @Override
    public List<JxStdDatabaseQuestionRecord> loadByDbIdAndKeyword(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord) {
        return stdDatabaseQuestionRecordMapper.loadByDbIdAndKeyword(jxStdDatabaseQuestionRecord);
    }
    @Override
    public String loadByDbIdAndKeywordCount(JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord) {
        return stdDatabaseQuestionRecordMapper.loadByDbIdAndKeywordCount(jxStdDatabaseQuestionRecord);
    }


    @Override
    public List<JxStdDatabaseQuestionRecord> listDistinctTableNameByDbId(String databaseId, String keyword) {
        return stdDatabaseQuestionRecordMapper.listDistinctTableNameByDbId(databaseId, keyword);
    }

    @Override
    public List<JxStdDatabaseQuestionRecord> listDistinctColumnNameByDbIdAndTbaleName(String databaseId, String tableName) {
        return stdDatabaseQuestionRecordMapper.listDistinctColumnNameByDbIdAndTbaleName(databaseId, tableName);
    }


    @Override
    public List<JxCompareVo> loadJxCompareVo(JxCompareVo jxCompareVo) {
        return stdDatabaseQuestionRecordMapper.loadJxCompareVo(jxCompareVo);
    }

    //     Page<JxCompareVo> loadJxCompareVoPlus(Page<JxCompareVo> page, @Param(Constants.WRAPPER) Wrapper<JxCompareVo> queryWrapper);
    @Override
    public List<JxCompareVo> loadJxCompareVoPlus(String databaseId, String remak, String tableName, String name, Integer page, Integer limit,String status) {

        return stdDatabaseQuestionRecordMapper.loadJxCompareVoPlus(databaseId, remak, tableName, name, page,limit,status);
    }
    @Override
    public String getCount(String databaseId, String remak, String tableName, String name,String status) {

        return stdDatabaseQuestionRecordMapper.getCount(databaseId, remak, tableName, name,status);
    }



}
