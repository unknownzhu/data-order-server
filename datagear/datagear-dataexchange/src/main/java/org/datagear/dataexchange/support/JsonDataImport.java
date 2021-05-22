/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange.support;

import java.io.Reader;

import org.datagear.dataexchange.DataFormat;
import org.datagear.dataexchange.TableTextValueDataImport;
import org.datagear.dataexchange.ValueDataImportOption;
import org.datagear.util.resource.ConnectionFactory;
import org.datagear.util.resource.ResourceFactory;

/**
 * JSON导入。
 * 
 * @author datagear@163.com
 *
 */
public class JsonDataImport extends TableTextValueDataImport
{
	private ResourceFactory<Reader> readerFactory;

	public JsonDataImport()
	{
		super();
	}

	public JsonDataImport(ConnectionFactory connectionFactory, DataFormat dataFormat, JsonDataImportOption importOption,
			String table, ResourceFactory<Reader> readerFactory)
	{
		super(connectionFactory, dataFormat, importOption, table);
		this.readerFactory = readerFactory;
	}

	public ResourceFactory<Reader> getReaderFactory()
	{
		return readerFactory;
	}

	public void setReaderFactory(ResourceFactory<Reader> readerFactory)
	{
		this.readerFactory = readerFactory;
	}

	@Override
	public JsonDataImportOption getImportOption()
	{
		return (JsonDataImportOption) super.getImportOption();
	}

	@Override
	public void setImportOption(ValueDataImportOption importOption)
	{
		if (!(importOption instanceof JsonDataImportOption))
			throw new IllegalArgumentException();

		super.setImportOption(importOption);
	}
}
