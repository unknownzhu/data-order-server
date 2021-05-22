package com.ctjsoft.orderserver.analyze.service;

import com.ctjsoft.orderserver.analyze.domain.DataSourceEntity;
import com.ctjsoft.orderserver.analyze.domain.DbInfo;
import org.datagear.management.domain.Schema;

import java.util.List;


public interface StrategyService {

	/**
	 * 根据id查询数据源
	 * @param id
	 * @return
	 */
	Schema findById(String id );

}
