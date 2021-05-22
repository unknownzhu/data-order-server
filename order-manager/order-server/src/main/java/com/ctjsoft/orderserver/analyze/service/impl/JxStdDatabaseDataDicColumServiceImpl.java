package com.ctjsoft.orderserver.analyze.service.impl;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionaryColumn;
import com.ctjsoft.orderserver.analyze.mapper.JxStdDatabaseDataDicColumMapper;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicColumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@Service
public class JxStdDatabaseDataDicColumServiceImpl implements JxStdDatabaseDataDicColumService {

    @Autowired
    private JxStdDatabaseDataDicColumMapper stdDatabaseDataDicColumMapper;

    @Override
    public List<JxStdDatabaseDataDicionaryColumn> selectByTableId(String tableId) {
        return stdDatabaseDataDicColumMapper.selectByTableId(tableId);
    }



    @Override
    public void save(List<JxStdDatabaseDataDicionaryColumn> list) {
        for(JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn : list){
            stdDatabaseDataDicColumMapper.update(stdDatabaseDataDicionaryColumn);
        }
    }


}
