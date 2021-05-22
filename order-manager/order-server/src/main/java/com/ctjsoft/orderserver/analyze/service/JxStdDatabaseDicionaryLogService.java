package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDicionaryLog;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-19
 * @desc
 */
public interface JxStdDatabaseDicionaryLogService {

    List<JxStdDatabaseDicionaryLog> detail(String tableId);
}
