/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange;

import java.io.Serializable;

/**
 * 文本导出设置项。
 * 
 * @author datagear@163.com
 *
 */
public class TextDataExportOption implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 列值非法时设置为null */
	private boolean nullForIllegalColumnValue = false;

	public TextDataExportOption()
	{
		super();
	}

	public TextDataExportOption(boolean nullForIllegalColumnValue)
	{
		super();
		this.nullForIllegalColumnValue = nullForIllegalColumnValue;
	}

	public boolean isNullForIllegalColumnValue()
	{
		return nullForIllegalColumnValue;
	}

	public void setNullForIllegalColumnValue(boolean nullForIllegalColumnValue)
	{
		this.nullForIllegalColumnValue = nullForIllegalColumnValue;
	}
}
