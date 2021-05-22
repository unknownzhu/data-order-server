/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.management.util.dialect;

/**
 * Derby 方言。
 * 
 * @author datagear@163.com
 *
 */
public class DerbyMbSqlDialect extends MbSqlDialect
{
	public DerbyMbSqlDialect()
	{
		super();
	}

	public DerbyMbSqlDialect(String identifierQuote)
	{
		super(identifierQuote);
	}

	@Override
	public boolean supportsPaging()
	{
		return true;
	}

	@Override
	public String pagingSqlHead(int index, int fetchSize)
	{
		return "";
	}

	@Override
	public String pagingSqlFoot(int index, int fetchSize)
	{
		return " OFFSET " + index + " ROWS FETCH NEXT " + fetchSize + " ROWS ONLY";
	}
}
