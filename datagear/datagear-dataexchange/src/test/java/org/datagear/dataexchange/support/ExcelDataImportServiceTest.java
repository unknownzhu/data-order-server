/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange.support;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.Connection;
import java.util.concurrent.atomic.AtomicInteger;

import org.datagear.dataexchange.ColumnNotFoundException;
import org.datagear.dataexchange.DataExchangeException;
import org.datagear.dataexchange.DataFormat;
import org.datagear.dataexchange.DataIndex;
import org.datagear.dataexchange.DataexchangeTestSupport;
import org.datagear.dataexchange.ExceptionResolve;
import org.datagear.dataexchange.ValueDataImportOption;
import org.datagear.util.JdbcUtil;
import org.datagear.util.resource.SimpleConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@linkplain ExcelDataImportService}单元测试类。
 * 
 * @author datagear@163.com
 *
 */
public class ExcelDataImportServiceTest extends DataexchangeTestSupport
{
	private ExcelDataImportService excelDataImportService;

	public ExcelDataImportServiceTest()
	{
		super();
		this.excelDataImportService = new ExcelDataImportService(dbMetaResolver);
	}

	@Test
	public void exchangeTest_xls() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Assert.assertThrows(ColumnNotFoundException.class, () ->
		{
			Connection cn = null;

			try
			{
				cn = getConnection();

				File excelFile = getClasspathFileForTest(
						"org/datagear/dataexchange/support/ExcelDataImportServiceTest.xls");

				ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, false,
						true);

				ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
						valueDataImportOption, excelFile);

				clearTable(cn, TABLE_NAME_DATA_IMPORT);
				clearTable(cn, TABLE_NAME_DATA_EXPORT);

				this.excelDataImportService.exchange(impt);

			}
			finally
			{
				JdbcUtil.closeConnection(cn);
			}
		});
	}

	@Test
	public void exchangeTest_xls_ignoreInexistentColumn() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Connection cn = getConnection();

		try
		{
			cn = getConnection();

			File excelFile = getClasspathFileForTest(
					"org/datagear/dataexchange/support/ExcelDataImportServiceTest.xls");

			final AtomicInteger successCount = new AtomicInteger(0);
			final AtomicInteger ignoreCount = new AtomicInteger(0);

			ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, true, true);

			ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
					valueDataImportOption, excelFile);

			impt.setListener(new MockValueDataImportListener()
			{
				@Override
				public void onSuccess(DataIndex dataIndex)
				{
					super.onSuccess(dataIndex);
					successCount.incrementAndGet();
				}

				@Override
				public void onIgnore(DataIndex dataIndex, DataExchangeException e)
				{
					super.onIgnore(dataIndex, e);
					ignoreCount.incrementAndGet();
				}
			});

			clearTable(cn, TABLE_NAME_DATA_IMPORT);
			clearTable(cn, TABLE_NAME_DATA_EXPORT);

			this.excelDataImportService.exchange(impt);

			int count0 = getCount(cn, TABLE_NAME_DATA_IMPORT);
			int count1 = getCount(cn, TABLE_NAME_DATA_EXPORT);

			assertEquals(5, count0);
			assertEquals(6, count1);
		}
		finally
		{
			JdbcUtil.closeConnection(cn);
		}
	}

	@Test
	public void exchangeTest_xlsx_ignoreInexistentColumn() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Connection cn = getConnection();

		try
		{
			cn = getConnection();

			File excelFile = getClasspathFileForTest(
					"org/datagear/dataexchange/support/ExcelDataImportServiceTest.xlsx");

			final AtomicInteger successCount = new AtomicInteger(0);
			final AtomicInteger ignoreCount = new AtomicInteger(0);

			ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, true, true);

			ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
					valueDataImportOption, excelFile);

			impt.setListener(new MockValueDataImportListener()
			{
				@Override
				public void onSuccess(DataIndex dataIndex)
				{
					super.onSuccess(dataIndex);
					successCount.incrementAndGet();
				}

				@Override
				public void onIgnore(DataIndex dataIndex, DataExchangeException e)
				{
					super.onIgnore(dataIndex, e);
					ignoreCount.incrementAndGet();
				}
			});

			clearTable(cn, TABLE_NAME_DATA_IMPORT);
			clearTable(cn, TABLE_NAME_DATA_EXPORT);

			this.excelDataImportService.exchange(impt);

			int count0 = getCount(cn, TABLE_NAME_DATA_IMPORT);
			int count1 = getCount(cn, TABLE_NAME_DATA_EXPORT);

			assertEquals(5, count0);
			assertEquals(6, count1);
		}
		finally
		{
			JdbcUtil.closeConnection(cn);
		}
	}

	/**
	 * 单元格的格式为：
	 * <p>
	 * &lt;c&gt;&lt;is&gt;&lt;t&gt;......&lt;/t&gt;&lt;/is&gt;&lt;/c&gt;
	 * </p>
	 * 而非
	 * </p>
	 * <p>
	 * &lt;c&gt;&lt;v&gt;......&lt;/v&gt;&lt;/c&gt;
	 * </p>
	 * 
	 * @throws Throwable
	 */
	@Test
	public void exchangeTest_xlsx_inlineStr_ignoreInexistentColumn() throws Throwable
	{
		DataFormat dataFormat = new DataFormat();

		Connection cn = getConnection();

		try
		{
			cn = getConnection();

			File excelFile = getClasspathFileForTest(
					"org/datagear/dataexchange/support/ExcelDataImportServiceTest_inlineStr.xlsx");

			final AtomicInteger successCount = new AtomicInteger(0);
			final AtomicInteger ignoreCount = new AtomicInteger(0);

			ValueDataImportOption valueDataImportOption = new ValueDataImportOption(ExceptionResolve.ABORT, true, true);

			ExcelDataImport impt = new ExcelDataImport(new SimpleConnectionFactory(cn, false), dataFormat,
					valueDataImportOption, excelFile);
			impt.setUnifiedTable(TABLE_NAME_DATA_IMPORT);

			impt.setListener(new MockValueDataImportListener()
			{
				@Override
				public void onSuccess(DataIndex dataIndex)
				{
					super.onSuccess(dataIndex);
					successCount.incrementAndGet();
				}

				@Override
				public void onIgnore(DataIndex dataIndex, DataExchangeException e)
				{
					super.onIgnore(dataIndex, e);
					ignoreCount.incrementAndGet();
				}
			});

			clearTable(cn, TABLE_NAME_DATA_IMPORT);

			this.excelDataImportService.exchange(impt);

			int count = getCount(cn, TABLE_NAME_DATA_IMPORT);

			assertEquals(5, count);
		}
		finally
		{
			JdbcUtil.closeConnection(cn);
		}
	}

	protected File getClasspathFileForTest(String classpath)
	{
		if (!classpath.startsWith("/"))
			classpath = "/" + classpath;

		File file = new File("src/test/resources" + classpath);

		return file;
	}
}
