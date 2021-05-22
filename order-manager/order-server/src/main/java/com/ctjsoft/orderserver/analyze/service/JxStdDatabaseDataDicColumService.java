package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionaryColumn;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
public interface JxStdDatabaseDataDicColumService {

    List<JxStdDatabaseDataDicionaryColumn> selectByTableId(String tableId);


    void save(List<JxStdDatabaseDataDicionaryColumn> list);
}
