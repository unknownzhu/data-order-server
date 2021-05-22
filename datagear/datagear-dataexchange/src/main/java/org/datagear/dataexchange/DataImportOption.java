/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.dataexchange;

import java.io.Serializable;

/**
 * 数据导入设置项。
 * 
 * @author datagear@163.com
 *
 */
public class DataImportOption implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 导入出错处理方式 */
	private ExceptionResolve exceptionResolve;

	public DataImportOption()
	{
		super();
	}

	public DataImportOption(ExceptionResolve exceptionResolve)
	{
		super();
		this.exceptionResolve = exceptionResolve;
	}

	public ExceptionResolve getExceptionResolve()
	{
		return exceptionResolve;
	}

	public void setExceptionResolve(ExceptionResolve exceptionResolve)
	{
		this.exceptionResolve = exceptionResolve;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [exceptionResolve=" + exceptionResolve + "]";
	}
}
