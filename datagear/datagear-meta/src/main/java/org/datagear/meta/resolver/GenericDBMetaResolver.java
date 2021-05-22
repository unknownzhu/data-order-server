/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.meta.resolver;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.datagear.connection.ConnectionOption;
import org.datagear.meta.Column;
import org.datagear.meta.DataType;
import org.datagear.meta.Database;
import org.datagear.meta.PrimaryKey;
import org.datagear.meta.SimpleTable;
import org.datagear.meta.Table;
import org.datagear.meta.resolver.support.MySqlDevotedDBMetaResolver;

/**
 * 通用{@linkplain DBMetaResolver}。
 * <p>
 * 它从其包含的{@linkplain DevotedDBMetaResolver}中（
 * {@linkplain #getDevotedDBMetaResolvers()}，越靠前越优先使用）查找能够处理给定{@link Connection}
 * 的那一个，并使用其API。
 * </p>
 * <p>
 * 如果没有查找到能处理给定{@link Connection}的{@linkplain DevotedDBMetaResolver}，此类将抛出
 * {@linkplain UnsupportedDBMetaResolverException}异常。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class GenericDBMetaResolver implements DBMetaResolver
{
	private List<DevotedDBMetaResolver> devotedDBMetaResolvers = null;

	public GenericDBMetaResolver()
	{
		super();

		this.devotedDBMetaResolvers = new ArrayList<>();
		this.devotedDBMetaResolvers.add(new MySqlDevotedDBMetaResolver());
		this.devotedDBMetaResolvers.add(new WildcardDevotedDBMetaResolver());
	}

	public List<DevotedDBMetaResolver> getDevotedDBMetaResolvers()
	{
		return devotedDBMetaResolvers;
	}

	public void setDevotedDBMetaResolvers(List<DevotedDBMetaResolver> devotedDBMetaResolvers)
	{
		this.devotedDBMetaResolvers = devotedDBMetaResolvers;
	}

	@Override
	public Database getDatabase(Connection cn) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getDatabase(cn);
	}

	@Override
	public List<SimpleTable> getSimpleTables(Connection cn) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getSimpleTables(cn);
	}

	@Override
	public SimpleTable getRandomSimpleTable(Connection cn) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getRandomSimpleTable(cn);
	}

	@Override
	public boolean isUserDataTable(Connection cn, SimpleTable table) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.isUserDataTable(cn, table);
	}

	@Override
	public boolean isUserDataEntityTable(Connection cn, SimpleTable table) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.isUserDataEntityTable(cn, table);
	}

	@Override
	public Table getTable(Connection cn, String tableName) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getTable(cn, tableName);
	}

	@Override
	public Column[] getColumns(Connection cn, String tableName) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getColumns(cn, tableName);
	}

	@Override
	public Column getRandomColumn(Connection cn, String tableName) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getRandomColumn(cn, tableName);
	}

	@Override
	public Column[] getColumns(Connection cn, ResultSetMetaData resultSetMetaData) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getColumns(cn, resultSetMetaData);
	}

	@Override
	public PrimaryKey getPrimaryKey(Connection cn, String tableName) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getPrimaryKey(cn, tableName);
	}

	@Override
	public List<DataType> getDataTypes(Connection cn) throws DBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getDataTypes(cn);
	}

	@Override
	public List<String[]> getImportTables(Connection cn, String... tableNames)
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolverNotNull(cn);
		return resolver.getImportTables(cn, tableNames);
	}

	/**
	 * 获取支持指定{@linkplain Connection}的{@linkplain DevotedDBMetaResolver}。
	 * 
	 * @param cn
	 * @return
	 * @throws UnsupportedDBMetaResolverException
	 */
	protected DevotedDBMetaResolver doGetDevotedDBMetaResolverNotNull(Connection cn)
			throws UnsupportedDBMetaResolverException
	{
		DevotedDBMetaResolver resolver = doGetDevotedDBMetaResolver(cn);

		if (resolver == null)
			throw new UnsupportedDBMetaResolverException(ConnectionOption.valueOfNonNull(cn));

		return resolver;
	}

	/**
	 * @param cn
	 * @return 返回{@code null}表示没有
	 */
	protected DevotedDBMetaResolver doGetDevotedDBMetaResolver(Connection cn)
	{
		if (this.devotedDBMetaResolvers == null)
			return null;

		for (DevotedDBMetaResolver resolver : this.devotedDBMetaResolvers)
		{
			if (resolver.supports(cn))
				return resolver;
		}

		return null;
	}
}
