/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.analysis.support;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.datagear.analysis.DataSetProperty;
import org.datagear.analysis.DataSetResult;
import org.datagear.analysis.ResolvedDataSetResult;
import org.junit.Test;

/**
 * {@linkplain CsvDirectoryFileDataSet}单元测试类。
 * 
 * @author datagear@163.com
 *
 */
public class CsvDirectoryFileDataSetTest
{
	private static final File DIRECTORY = new File("src/test/resources/org/datagear/analysis/support/");

	@Test
	public void getResultTest()
	{
		List<DataSetProperty> properties = new ArrayList<>();
		properties.add(new DataSetProperty("name", DataSetProperty.DataType.STRING));
		properties.add(new DataSetProperty("value", DataSetProperty.DataType.NUMBER));
		properties.add(new DataSetProperty("尺寸", DataSetProperty.DataType.NUMBER));

		CsvDirectoryFileDataSet dataSet = new CsvDirectoryFileDataSet("a", "a", properties, DIRECTORY,
				"CsvDirectoryFileDataSetTest-0.csv");
		dataSet.setNameRow(1);

		@SuppressWarnings("unchecked")
		DataSetResult result = dataSet.getResult(Collections.EMPTY_MAP);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getData();

		{
			assertEquals(3, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(11, ((Number) row.get("value")).intValue());
				assertEquals(12, ((Number) row.get("尺寸")).intValue());
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(21, ((Number) row.get("value")).intValue());
				assertEquals(22, ((Number) row.get("尺寸")).intValue());
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(31, ((Number) row.get("value")).intValue());
				assertEquals(32, ((Number) row.get("尺寸")).intValue());
			}
		}
	}

	@Test
	public void resolveTest()
	{
		CsvDirectoryFileDataSet dataSet = new CsvDirectoryFileDataSet("a", "a", DIRECTORY,
				"CsvDirectoryFileDataSetTest-0.csv");
		dataSet.setNameRow(1);

		@SuppressWarnings("unchecked")
		ResolvedDataSetResult result = dataSet.resolve(Collections.EMPTY_MAP, null);
		List<DataSetProperty> properties = result.getProperties();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getResult().getData();

		{
			assertEquals(3, properties.size());

			{
				DataSetProperty property = properties.get(0);
				assertEquals("name", property.getName());
				assertEquals(DataSetProperty.DataType.STRING, property.getType());
			}

			{
				DataSetProperty property = properties.get(1);
				assertEquals("value", property.getName());
				assertEquals(DataSetProperty.DataType.NUMBER, property.getType());
			}

			{
				DataSetProperty property = properties.get(2);
				assertEquals("尺寸", property.getName());
				assertEquals(DataSetProperty.DataType.NUMBER, property.getType());
			}
		}

		{
			assertEquals(3, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals("aaa", row.get("name"));
				assertEquals(11, ((Number) row.get("value")).intValue());
				assertEquals(12, ((Number) row.get("尺寸")).intValue());
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("bbb", row.get("name"));
				assertEquals(21, ((Number) row.get("value")).intValue());
				assertEquals(22, ((Number) row.get("尺寸")).intValue());
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("ccc", row.get("name"));
				assertEquals(31, ((Number) row.get("value")).intValue());
				assertEquals(32, ((Number) row.get("尺寸")).intValue());
			}
		}
	}

	@Test
	public void resolveTest_noNameRow()
	{
		CsvDirectoryFileDataSet dataSet = new CsvDirectoryFileDataSet("a", "a", DIRECTORY,
				"CsvDirectoryFileDataSetTest-0.csv");

		@SuppressWarnings("unchecked")
		ResolvedDataSetResult result = dataSet.resolve(Collections.EMPTY_MAP, null);
		List<DataSetProperty> properties = result.getProperties();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> data = (List<Map<String, Object>>) result.getResult().getData();

		{
			assertEquals(3, properties.size());

			{
				DataSetProperty property = properties.get(0);
				assertEquals("1", property.getName());
				assertEquals(DataSetProperty.DataType.STRING, property.getType());
			}

			{
				DataSetProperty property = properties.get(1);
				assertEquals("2", property.getName());
				assertEquals(DataSetProperty.DataType.STRING, property.getType());
			}

			{
				DataSetProperty property = properties.get(2);
				assertEquals("3", property.getName());
				assertEquals(DataSetProperty.DataType.STRING, property.getType());
			}
		}

		{
			assertEquals(4, data.size());

			{
				Map<String, Object> row = data.get(0);

				assertEquals("name", row.get("1"));
				assertEquals("value", row.get("2"));
				assertEquals("尺寸", row.get("3"));
			}

			{
				Map<String, Object> row = data.get(1);

				assertEquals("aaa", row.get("1"));
				assertEquals("11", row.get("2"));
				assertEquals("12", row.get("3"));
			}

			{
				Map<String, Object> row = data.get(2);

				assertEquals("bbb", row.get("1"));
				assertEquals("21", row.get("2"));
				assertEquals("22", row.get("3"));
			}

			{
				Map<String, Object> row = data.get(3);

				assertEquals("ccc", row.get("1"));
				assertEquals("31", row.get("2"));
				assertEquals("32", row.get("3"));
			}
		}
	}
}
