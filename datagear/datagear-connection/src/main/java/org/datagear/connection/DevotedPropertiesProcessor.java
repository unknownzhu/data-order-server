/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.connection;

import java.sql.Driver;
import java.util.Properties;

/**
 * 专职{@linkplain PropertiesProcessor}。
 * 
 * @author datagear@163.com
 *
 */
public interface DevotedPropertiesProcessor extends PropertiesProcessor
{
	/**
	 * 是否能处理给定连接参数。
	 * 
	 * @param driver
	 * @param properties
	 * @return
	 */
	boolean supports(Driver driver, Properties properties);
}
