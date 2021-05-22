package com.ctjsoft.orderserver.analyze.service.impl;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicChangeRecord;
import com.ctjsoft.orderserver.analyze.mapper.JxStdDatabaseDataDicCRecordMapper;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicCRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@Service
public class JxStdDatabaseDataDicCRecordServiceImpl implements JxStdDatabaseDataDicCRecordService {

    @Autowired
    private JxStdDatabaseDataDicCRecordMapper stdDatabaseDataDicCRecordMapper;

    @Override
    public void save(JxStdDatabaseDataDicChangeRecord stdDatabaseDataDicChangeRecord) {
        if (!StringUtils.isEmpty(stdDatabaseDataDicChangeRecord.getId())) { //如果是修改

        }
    }

    @Override
    public List<JxStdDatabaseDataDicChangeRecord> search(Map<String, String> params) {
        return stdDatabaseDataDicCRecordMapper.search(params);
    }

    @Override
    public List<JxStdDatabaseDataDicChangeRecord> loadByDatabaseId(String databaseId) {
        return stdDatabaseDataDicCRecordMapper.loadByDatabaseId(databaseId);
    }

    @Override
    public List<JxStdDatabaseDataDicChangeRecord> loadByDbIdAndKeyword(String databaseId, String keyword, String page, String limit) {
        return stdDatabaseDataDicCRecordMapper.loadByDbIdAndKeyword(databaseId, keyword, page, limit);
    }


    @Override
    public String   loadByDbIdAndKeywordCount (String databaseId, String keyword) {
        return stdDatabaseDataDicCRecordMapper.loadByDbIdAndKeywordCount(databaseId, keyword);
    }

}
