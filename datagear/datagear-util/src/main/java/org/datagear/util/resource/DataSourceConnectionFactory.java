/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.util.resource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.datagear.util.JdbcUtil;

/**
 * 数据源{@linkplain ConnectionSource}。
 * 
 * @author datagear@163.com
 *
 */
public class DataSourceConnectionFactory implements ConnectionFactory
{
	private DataSource dataSource;

	public DataSourceConnectionFactory()
	{
		super();
	}

	public DataSourceConnectionFactory(DataSource dataSource)
	{
		super();
		this.dataSource = dataSource;
	}

	public DataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	@Override
	public Connection get() throws SQLException
	{
		return this.dataSource.getConnection();
	}

	@Override
	public void release(Connection cn) throws SQLException
	{
		JdbcUtil.closeConnection(cn);
	}
}
