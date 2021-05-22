/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.management.dbversion;

import java.io.IOException;
import java.util.List;

import org.datagear.util.version.Version;
import org.datagear.util.version.VersionContent;
import org.junit.Assert;
import org.junit.Test;

/**
 * {@linkplain DbVersionManager}单元测试类。
 * 
 * @author datagear@163.com
 *
 */
public class DbVersionManagerTest
{
	private DbVersionManager dbVersionManager = new DbVersionManager();

	@Test
	public void resolveUpgradeSqlVersionContentsTest() throws IOException
	{
		List<VersionContent> versionContents = dbVersionManager.resolveUpgradeSqlVersionContents(null);

		Assert.assertTrue(versionContents.size() > 0);

		{
			VersionContent versionContent = versionContents.get(0);
			List<String> contents = versionContent.getContents();

			Assert.assertEquals(Version.valueOf("1.0.0"), versionContent.getVersion());
			Assert.assertEquals(4, contents.size());

			Assert.assertTrue(contents.get(0).startsWith("CREATE TABLE DATAGEAR_VERSION"));
			Assert.assertTrue(contents.get(0).endsWith(")"));

			Assert.assertTrue(contents.get(1).startsWith("CREATE TABLE DATAGEAR_USER"));
			Assert.assertTrue(contents.get(1).endsWith(")"));

			Assert.assertTrue(contents.get(2).startsWith("INSERT INTO DATAGEAR_USER"));
			Assert.assertTrue(contents.get(2).endsWith("CURRENT_TIMESTAMP)"));

			Assert.assertTrue(contents.get(3).startsWith("CREATE TABLE DATAGEAR_SCHEMA"));
			Assert.assertTrue(contents.get(3).endsWith(")"));
		}
	}
}
