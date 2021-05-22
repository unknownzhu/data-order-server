/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.util.resource;

import java.sql.Connection;

import org.datagear.util.JdbcUtil;

/**
 * 简单{@linkplain ConnectionFactory}。
 * 
 * @author datagear@163.com
 *
 */
public class SimpleConnectionFactory implements ConnectionFactory
{
	private Connection resource;

	private boolean closeOnRelease = true;

	public SimpleConnectionFactory()
	{
		super();
	}

	public SimpleConnectionFactory(Connection resource, boolean closeOnRelease)
	{
		super();
		this.resource = resource;
		this.closeOnRelease = closeOnRelease;
	}

	public Connection getResource() throws Exception
	{
		return resource;
	}

	public void setResource(Connection resource)
	{
		this.resource = resource;
	}

	public boolean isCloseOnRelease()
	{
		return closeOnRelease;
	}

	public void setCloseOnRelease(boolean closeOnRelease)
	{
		this.closeOnRelease = closeOnRelease;
	}

	@Override
	public Connection get() throws Exception
	{
		return this.resource;
	}

	@Override
	public void release(Connection resource) throws Exception
	{
		if (this.resource != resource)
			throw new IllegalStateException();

		if (this.closeOnRelease && this.resource != null)
			JdbcUtil.closeConnection(this.resource);
	}
}
