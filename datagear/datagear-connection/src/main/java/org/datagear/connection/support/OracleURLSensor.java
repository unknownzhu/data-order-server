/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.connection.support;

import org.datagear.connection.PrefixURLSensor;
import org.datagear.connection.URLSensor;

/**
 * Oracle {@linkplain URLSensor}。
 * 
 * @author datagear@163.com
 *
 */
public class OracleURLSensor extends PrefixURLSensor
{
	public static final String JDBC_PREFIX = "jdbc:oracle";

	public static final OracleURLSensor INSTANCE = new OracleURLSensor();

	public OracleURLSensor()
	{
		super(JDBC_PREFIX);
	}

	@Override
	public void setPrefix(String prefix)
	{
		throw new UnsupportedOperationException();
	}
}
