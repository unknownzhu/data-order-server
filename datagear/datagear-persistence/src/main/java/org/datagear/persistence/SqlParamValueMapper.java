/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.persistence;

import java.sql.Connection;

import org.datagear.meta.Column;
import org.datagear.meta.Table;
import org.datagear.util.SqlParamValue;

/**
 * 列值至{@linkplain SqlParamValue}映射器。
 * 
 * @author datagear@163.com
 *
 */
public interface SqlParamValueMapper
{
	/**
	 * 映射。
	 * <p>
	 * 此方法的返回值可能是{@linkplain LiteralSqlParamValue}。
	 * </p>
	 * <p>
	 * 注意：此方法不应返回{@code null}，返回的{@linkplain SqlParamValue#getValue()}允许为{@code null}。
	 * </p>
	 * 
	 * @param cn
	 * @param table
	 * @param column
	 * @param value
	 *            允许为{@code null}
	 * @return
	 * @throws SqlParamValueMapperException
	 */
	SqlParamValue map(Connection cn, Table table, Column column, Object value) throws SqlParamValueMapperException;
}
