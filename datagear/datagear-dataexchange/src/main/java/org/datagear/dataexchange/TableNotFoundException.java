/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange;

/**
 * 表未找到异常。
 * 
 * @author datagear@163.com
 *
 */
public class TableNotFoundException extends DataExchangeException
{
	private static final long serialVersionUID = 1L;

	private String table;

	public TableNotFoundException(String table)
	{
		super("Table [" + table + "] not found");

		this.table = table;
	}

	public String getTable()
	{
		return table;
	}

	protected void setTable(String table)
	{
		this.table = table;
	}
}
