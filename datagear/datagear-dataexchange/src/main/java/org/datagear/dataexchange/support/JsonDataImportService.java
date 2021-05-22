/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange.support;

import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.json.Json;
import javax.json.stream.JsonLocation;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.datagear.dataexchange.AbstractDevotedDBMetaDataExchangeService;
import org.datagear.dataexchange.ColumnNotFoundException;
import org.datagear.dataexchange.DataExchangeContext;
import org.datagear.dataexchange.DataExchangeException;
import org.datagear.dataexchange.IndexFormatDataExchangeContext;
import org.datagear.dataexchange.RowColumnDataIndex;
import org.datagear.meta.Column;
import org.datagear.meta.resolver.DBMetaResolver;
import org.datagear.util.JdbcUtil;
import org.datagear.util.StringUtil;

/**
 * JSON导入服务。
 * 
 * @author datagear@163.com
 *
 */
public class JsonDataImportService extends AbstractDevotedDBMetaDataExchangeService<JsonDataImport>
{
	public JsonDataImportService()
	{
		super();
	}

	public JsonDataImportService(DBMetaResolver dbMetaResolver)
	{
		super(dbMetaResolver);
	}

	@Override
	protected DataExchangeContext createDataExchangeContext(JsonDataImport dataExchange)
	{
		return IndexFormatDataExchangeContext.valueOf(dataExchange);
	}

	@Override
	protected void exchange(JsonDataImport dataExchange, DataExchangeContext context) throws Throwable
	{
		JsonDataImportOption importOption = dataExchange.getImportOption();
		JsonDataFormat jsonDataFormat = importOption.getJsonDataFormat();

		if (JsonDataFormat.ROW_ARRAY.equals(jsonDataFormat))
			importForRowArrayData(dataExchange, context);
		else if (JsonDataFormat.TABLE_OBJECT.equals(jsonDataFormat))
			importForTableObjectData(dataExchange, context);
		else
			throw new UnsupportedOperationException();
	}

	/**
	 * 导入{@linkplain JsonDataFormat#TABLE_OBJECT}格式的数据。
	 * 
	 * @param dataExchange
	 * @param context
	 * @throws Throwable
	 */
	protected void importForTableObjectData(JsonDataImport dataExchange, DataExchangeContext context) throws Throwable
	{
		IndexFormatDataExchangeContext importContext = IndexFormatDataExchangeContext.cast(context);

		Reader jsonReader = getResource(dataExchange.getReaderFactory(), importContext);

		Connection cn = context.getConnection();
		JdbcUtil.setAutoCommitIfSupports(cn, false);
		JdbcUtil.setReadonlyIfSupports(cn, false);

		JsonParser p = Json.createParser(jsonReader);

		if (p.hasNext())
		{
			Event event = p.next();

			if (!Event.START_OBJECT.equals(event))
				throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.START_OBJECT);

			String table = null;

			while (p.hasNext())
			{
				event = p.next();

				if (Event.END_OBJECT.equals(event))
					break;

				if (Event.KEY_NAME.equals(event))
				{
					table = p.getString();
				}
				else if (Event.START_ARRAY.equals(event))
				{
					if (table == null)
						throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

					importJsonArray(dataExchange, importContext, cn, p, table);
					table = null;
				}
				else
					throw new IllegalJsonDataFormatException(p.getLocation(), false, event);
			}
		}

		commit(cn);
	}

	/**
	 * 导入{@linkplain JsonDataFormat#ROW_ARRAY}格式的数据。
	 * 
	 * @param dataExchange
	 * @param context
	 * @throws Throwable
	 */
	protected void importForRowArrayData(JsonDataImport dataExchange, DataExchangeContext context) throws Throwable
	{
		String table = dataExchange.getTable();

		if (StringUtil.isEmpty(table))
			throw new DataExchangeException("JsonDataImport.table must be set");

		IndexFormatDataExchangeContext importContext = IndexFormatDataExchangeContext.cast(context);

		Reader jsonReader = getResource(dataExchange.getReaderFactory(), importContext);

		Connection cn = context.getConnection();
		JdbcUtil.setAutoCommitIfSupports(cn, false);

		JsonParser p = Json.createParser(jsonReader);

		if (p.hasNext())
		{
			Event event = p.next();

			if (!Event.START_ARRAY.equals(event))
				throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.START_ARRAY);

			importJsonArray(dataExchange, importContext, cn, p, table);
		}

		commit(cn);
	}

	/**
	 * 解析并导入{@code [}标记之后的一个数组。
	 * 
	 * @param dataExchange
	 * @param context
	 * @param cn
	 * @param p
	 * @param table
	 * @throws Throwable
	 */
	@SuppressWarnings("unchecked")
	protected void importJsonArray(JsonDataImport dataExchange, IndexFormatDataExchangeContext context, Connection cn,
			JsonParser p, String table) throws Throwable
	{
		JsonDataImportOption importOption = dataExchange.getImportOption();

		List<Column> totalColumns = getColumns(cn, table);

		PreparedStatement prevSt = null;
		List<Column> prevColumns = null;

		while (p.hasNext())
		{
			Event event = p.next();

			if (Event.END_ARRAY.equals(event))
				break;

			if (!Event.START_OBJECT.equals(event))
				throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.START_OBJECT);

			JsonLocation jsonLocation = p.getLocation();
			context.setDataIndex(
					RowColumnDataIndex.valueOf(jsonLocation.getLineNumber(), jsonLocation.getColumnNumber()));

			Map<String, Object> row = parseNextObject(p);

			Object[] myColumnValuess = getColumnValues(dataExchange, table, totalColumns, row);
			List<Column> myColumns = (List<Column>) myColumnValuess[0];
			List<Object> myColumnValues = (List<Object>) myColumnValuess[1];

			if (!myColumns.isEmpty())
			{
				boolean newSql = false;

				if (prevSt == null || prevColumns == null)
					newSql = true;
				else if (myColumns.equals(prevColumns))
					newSql = false;
				else
					newSql = true;

				if (newSql)
				{
					JdbcUtil.closeStatement(prevSt);

					String sql = buildInsertPreparedSql(cn, table, myColumns);

					prevSt = cn.prepareStatement(sql);
					prevColumns = myColumns;
				}

				importValueData(cn, prevSt, prevColumns, myColumnValues, context.getDataIndex(),
						importOption.isNullForIllegalColumnValue(), importOption.getExceptionResolve(),
						context.getDataFormatContext(), dataExchange.getListener());
			}
		}

		JdbcUtil.closeStatement(prevSt);
	}

	/**
	 * 获取列信息及列值列表数组。
	 * 
	 * @param dataExchange
	 * @param table
	 * @param columns
	 * @param row
	 * @return
	 * @throws ColumnNotFoundException
	 */
	protected Object[] getColumnValues(JsonDataImport dataExchange, String table, List<Column> columns,
			Map<String, Object> row) throws ColumnNotFoundException
	{
		List<Column> myColumns = new ArrayList<>();
		List<Object> myColumnValues = new ArrayList<>();

		for (Column column : columns)
		{
			if (row.containsKey(column.getName()))
			{
				myColumns.add(column);
				myColumnValues.add(row.get(column.getName()));
			}
		}

		// 有不存在的列且不被允许
		if (row.size() > myColumns.size() && !dataExchange.getImportOption().isIgnoreInexistentColumn())
		{
			Set<String> myNames = row.keySet();

			for (String myName : myNames)
			{
				if (findColumn(columns, myName) == null)
					throw new ColumnNotFoundException(table, myName);
			}
		}

		return new Object[] { myColumns, myColumnValues };
	}

	/**
	 * 解析<code>{</code>标记之后的一个对象。
	 * 
	 * @param p
	 * @return
	 * @throws Throwable
	 */
	protected Map<String, Object> parseNextObject(JsonParser p) throws Throwable
	{
		Map<String, Object> map = new HashMap<>();

		String name = null;

		while (p.hasNext())
		{
			Event e = p.next();

			if (Event.KEY_NAME.equals(e))
			{
				name = p.getString();
			}
			else if (Event.VALUE_FALSE.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				map.put(name, false);
				name = null;
			}
			else if (Event.VALUE_NULL.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				map.put(name, null);
				name = null;
			}
			else if (Event.VALUE_NUMBER.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				map.put(name, p.getBigDecimal());
				name = null;
			}
			else if (Event.VALUE_STRING.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				map.put(name, p.getString());
				name = null;
			}
			else if (Event.VALUE_TRUE.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				map.put(name, true);
				name = null;
			}
			else if (Event.START_OBJECT.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				Object subMap = parseNextObject(p);
				map.put(name, subMap);
				name = null;
			}
			else if (Event.END_OBJECT.equals(e))
			{
				break;
			}
			else if (Event.START_ARRAY.equals(e))
			{
				if (name == null)
					throw new IllegalJsonDataFormatException(p.getLocation(), true, Event.KEY_NAME);

				Object[] subArray = parseNextArray(p);
				map.put(name, subArray);
				name = null;
			}
			else if (Event.END_ARRAY.equals(e))
				throw new IllegalJsonDataFormatException(p.getLocation(), false, Event.END_ARRAY);
		}

		return map;
	}

	/**
	 * 解析{@code [}标记之后的一个数组。
	 * 
	 * @param p
	 * @return
	 * @throws Throwable
	 */
	protected Object[] parseNextArray(JsonParser p) throws Throwable
	{
		List<Object> list = new ArrayList<>();

		while (p.hasNext())
		{
			Event e = p.next();

			if (Event.KEY_NAME.equals(e))
			{
				throw new IllegalJsonDataFormatException(p.getLocation(), false, Event.KEY_NAME);
			}
			else if (Event.VALUE_FALSE.equals(e))
			{
				list.add(false);
			}
			else if (Event.VALUE_NULL.equals(e))
			{
				list.add(null);
			}
			else if (Event.VALUE_NUMBER.equals(e))
			{
				list.add(p.getBigDecimal());
			}
			else if (Event.VALUE_STRING.equals(e))
			{
				list.add(p.getString());
			}
			else if (Event.VALUE_TRUE.equals(e))
			{
				list.add(true);
			}
			else if (Event.START_OBJECT.equals(e))
			{
				Object subMap = parseNextObject(p);
				list.add(subMap);
			}
			else if (Event.END_OBJECT.equals(e))
			{
				throw new IllegalJsonDataFormatException(p.getLocation(), false, Event.END_OBJECT);
			}
			else if (Event.START_ARRAY.equals(e))
			{
				Object[] subArray = parseNextArray(p);
				list.add(subArray);
			}
			else if (Event.END_ARRAY.equals(e))
			{
				break;
			}
		}

		return list.toArray();
	}

	@Override
	protected void onException(JsonDataImport dataExchange, DataExchangeContext context, DataExchangeException e)
			throws DataExchangeException
	{
		processTransactionForDataExchangeException(context, e, dataExchange.getImportOption().getExceptionResolve());

		super.onException(dataExchange, context, e);
	}
}
