/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.analysis.support.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Set;

import org.datagear.analysis.ChartPlugin;
import org.datagear.util.FileUtil;
import org.datagear.util.IOUtil;
import org.junit.Test;

/**
 * {@linkplain DirectoryHtmlChartPluginManager}单元测试类。
 * 
 * @author datagear@163.com
 *
 */
public class DirectoryHtmlChartPluginManagerTest
{
	public DirectoryHtmlChartPluginManagerTest()
	{
		super();

	}

	@Test
	public void uploadTest() throws Exception
	{
		File root = FileUtil.getFile("target/DirectoryHtmlChartPluginManagerTest/uploadTest/", true);
		File managerDirectory = FileUtil.getFile(root, "manager/", true);
		File uploadDirectory = FileUtil.getFile(root, "upload/", true);
		DirectoryHtmlChartPluginManager directoryHtmlChartPluginManager = new DirectoryHtmlChartPluginManager(
				managerDirectory);

		FileUtil.clearDirectory(managerDirectory);
		FileUtil.clearDirectory(uploadDirectory);

		try (InputStream in = DirectoryHtmlChartPluginManagerTest.class.getClassLoader().getResourceAsStream(
				"org/datagear/analysis/support/html/directoryHtmlChartPluginManagerTest/plugin.current.zip"))
		{
			IOUtil.write(in, FileUtil.getFile(managerDirectory, "plugin.zip"));
		}

		try (InputStream in = DirectoryHtmlChartPluginManagerTest.class.getClassLoader().getResourceAsStream(
				"org/datagear/analysis/support/html/directoryHtmlChartPluginManagerTest/plugin.upload.zip"))
		{
			IOUtil.write(in, FileUtil.getFile(uploadDirectory, "plugin.zip"));
		}

		directoryHtmlChartPluginManager.init();

		ChartPlugin plugin = directoryHtmlChartPluginManager.get("test");

		assertNotNull(plugin);
		assertEquals("0.1.0", plugin.getVersion());

		{
			Set<HtmlChartPlugin> uploaded = directoryHtmlChartPluginManager.upload(uploadDirectory);
			plugin = directoryHtmlChartPluginManager.get("test");

			assertEquals(1, uploaded.size());
			assertNotNull(plugin);
			assertEquals("0.1.1", plugin.getVersion());
		}

		{
			Set<HtmlChartPlugin> uploaded = directoryHtmlChartPluginManager.upload(uploadDirectory);
			plugin = directoryHtmlChartPluginManager.get("test");

			assertEquals(0, uploaded.size());

			assertNotNull(plugin);
			assertEquals("0.1.1", plugin.getVersion());
		}
	}
}
