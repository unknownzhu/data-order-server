package com.ctjsoft.orderserver.analyze.service.impl;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDicionaryLog;
import com.ctjsoft.orderserver.analyze.mapper.JxStdDatabaseDicionaryLogMapper;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDicionaryLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@Service
public class JxStdDatabaseDicionaryLogServiceImpl implements JxStdDatabaseDicionaryLogService {

    @Autowired
    private JxStdDatabaseDicionaryLogMapper stdDatabaseDicionaryLogMapper;

    @Override
    public List<JxStdDatabaseDicionaryLog> detail(String tableId) {
        return stdDatabaseDicionaryLogMapper.detail(tableId);
    }
}
