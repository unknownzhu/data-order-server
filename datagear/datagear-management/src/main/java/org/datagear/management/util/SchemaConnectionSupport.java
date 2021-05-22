/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.management.util;

import java.sql.Connection;

import org.datagear.connection.ConnectionOption;
import org.datagear.connection.ConnectionSource;
import org.datagear.connection.ConnectionSourceException;
import org.datagear.connection.DriverEntity;
import org.datagear.management.domain.Schema;

/**
 * {@linkplain Schema}数据库连接支持类。
 * 
 * @author datagear@163.com
 *
 */
public class SchemaConnectionSupport
{
	public SchemaConnectionSupport()
	{
	}

	/**
	 * 获取指定{@linkplain Schema}的{@linkplain Connection}。
	 * 
	 * @param connectionSource
	 * @param schema
	 * @return
	 * @throws ConnectionSourceException
	 */
	public Connection getSchemaConnection(ConnectionSource connectionSource, Schema schema)
			throws ConnectionSourceException
	{
		System.out.println("getSchemaConnection");

		Connection cn = null;

		ConnectionOption connectionOption = ConnectionOption.valueOf(schema.getUrl(), schema.getUser(),
				schema.getPassword());
		System.out.println(connectionOption);

		if (schema.hasDriverEntity())
		{
			DriverEntity driverEntity = schema.getDriverEntity();

			System.out.println("driverEntity1");
			System.out.println(driverEntity);
			cn = connectionSource.getConnection(driverEntity, connectionOption);
		}
		else
		{
			System.out.println("driverEntity2");
			System.out.println("connectionOption");
			cn = connectionSource.getConnection(connectionOption);
		}

		return cn;
	}
}
