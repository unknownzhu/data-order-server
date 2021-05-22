/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.connection;

/**
 * {@linkplain PathDriverFactory}异常。
 * 
 * @author datagear@163.com
 *
 */
public class PathDriverFactoryException extends DriverEntityManagerException
{
	private static final long serialVersionUID = 1L;

	public PathDriverFactoryException()
	{
		super();
	}

	public PathDriverFactoryException(String message)
	{
		super(message);
	}

	public PathDriverFactoryException(Throwable cause)
	{
		super(cause);
	}

	public PathDriverFactoryException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
