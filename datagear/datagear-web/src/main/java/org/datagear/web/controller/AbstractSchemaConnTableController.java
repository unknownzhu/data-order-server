/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.datagear.management.domain.Schema;
import org.datagear.meta.Table;
import org.datagear.meta.resolver.DBMetaResolver;
import org.datagear.persistence.support.NoColumnDefinedException;
import org.datagear.web.util.TableCache;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抽象数据库表连接控制器。
 * 
 * @author datagear@163.com
 *
 */
public abstract class AbstractSchemaConnTableController extends AbstractSchemaConnController
{
	@Autowired
	private DBMetaResolver dbMetaResolver;

	@Autowired
	private TableCache tableCache;

	public AbstractSchemaConnTableController()
	{
		super();
	}

	public DBMetaResolver getDbMetaResolver()
	{
		return dbMetaResolver;
	}

	public void setDbMetaResolver(DBMetaResolver dbMetaResolver)
	{
		this.dbMetaResolver = dbMetaResolver;
	}

	public TableCache getTableCache()
	{
		return tableCache;
	}

	public void setTableCache(TableCache tableCache)
	{
		this.tableCache = tableCache;
	}

	/**
	 * 抽象数据库表执行器。
	 * 
	 * @author datagear@163.com
	 *
	 */
	protected abstract class AbstractSchemaConnTableExecutor extends AbstractSchemaConnExecutor
	{
		private String tableName;

		public AbstractSchemaConnTableExecutor(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, String schemaId, String tableName, boolean readonly)
		{
			super(request, response, springModel, schemaId, readonly);
			this.tableName = tableName;
		}

		public AbstractSchemaConnTableExecutor(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, String schemaId, String tableName, boolean readonly,
				boolean customCommit)
		{
			super(request, response, springModel, schemaId, readonly, customCommit);
			this.tableName = tableName;
		}

		@Override
		protected void doExecute(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, Schema schema) throws Throwable
		{
			springModel.addAttribute("tableName", this.tableName);

			Table table = getTableCache().get(schema.getId(), this.tableName);
			if (table == null)
			{
				table = getDbMetaResolver().getTable(getConnection(), this.tableName);
				getTableCache().put(schema.getId(), table);
			}

			if (!table.hasColumn())
				throw new NoColumnDefinedException(table.getName());

			springModel.addAttribute("table", table);

			doExecute(request, response, springModel, schema, table);
		}

		/**
		 * 执行。
		 * <p>
		 * 此方法内可以调用{@linkplain #getConnection()}直接使用，而不需要关闭。
		 * </p>
		 * 
		 * @param request
		 * @param response
		 * @param springModel
		 * @param schema
		 * @param table
		 * @throws Throwable
		 */
		protected abstract void doExecute(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, Schema schema, Table table) throws Throwable;
	}

	/**
	 * 返回值{@linkplain AbstractSchemaConnTableExecutor}。
	 * 
	 * @author datagear@163.com
	 *
	 * @param <T>
	 */
	protected abstract class ReturnSchemaConnTableExecutor<T> extends AbstractSchemaConnTableExecutor
	{
		private T returnValue;

		public ReturnSchemaConnTableExecutor(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, String schemaId, String tableName, boolean readonly)
		{
			super(request, response, springModel, schemaId, tableName, readonly);
		}

		public ReturnSchemaConnTableExecutor(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, String schemaId, String tableName, boolean readonly,
				boolean customCommit)
		{
			super(request, response, springModel, schemaId, tableName, readonly, customCommit);
		}

		public T execute() throws Throwable
		{
			doExecute();
			return returnValue;
		}

		@Override
		protected void doExecute(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, Schema schema, Table table) throws Throwable
		{
			this.returnValue = execute(request, response, springModel, schema, table);
		}

		/**
		 * 执行。
		 * <p>
		 * 此方法内可以调用{@linkplain #getConnection()}直接使用，而不需要关闭。
		 * </p>
		 * 
		 * @param request
		 * @param response
		 * @param springModel
		 * @param schema
		 * @param table
		 * @return
		 */
		protected abstract T execute(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, Schema schema, Table table) throws Throwable;
	}

	/**
	 * 无返回值{@linkplain AbstractSchemaConnTableExecutor}。
	 * 
	 * @author datagear@163.com
	 *
	 * @param <T>
	 */
	protected abstract class VoidSchemaConnTableExecutor extends AbstractSchemaConnTableExecutor
	{
		public VoidSchemaConnTableExecutor(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, String schemaId, String tableName, boolean readonly)
		{
			super(request, response, springModel, schemaId, tableName, readonly);
		}

		public VoidSchemaConnTableExecutor(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, String schemaId, String tableName, boolean readonly,
				boolean customCommit)
		{
			super(request, response, springModel, schemaId, tableName, readonly, customCommit);
		}

		public void execute() throws Throwable
		{
			doExecute();
		}

		@Override
		protected void doExecute(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, Schema schema, Table table) throws Throwable
		{
			execute(request, response, springModel, schema, table);
		}

		/**
		 * 执行。
		 * <p>
		 * 此方法内可以调用{@linkplain #getConnection()}直接使用，而不需要关闭。
		 * </p>
		 * 
		 * @param request
		 * @param response
		 * @param springModel
		 * @param schema
		 * @param table
		 * @return
		 */
		protected abstract void execute(HttpServletRequest request, HttpServletResponse response,
				org.springframework.ui.Model springModel, Schema schema, Table table) throws Throwable;
	}
}
