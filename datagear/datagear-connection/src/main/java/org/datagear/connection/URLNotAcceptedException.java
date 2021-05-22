/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.connection;

/**
 * {@linkplain DriverEntity}对应的驱动不支持给定的URL异常。
 * 
 * @author datagear@163.com
 *
 */
public class URLNotAcceptedException extends ConnectionSourceException
{
	private static final long serialVersionUID = 1L;

	private DriverEntity driverEntity;

	private String url;

	public URLNotAcceptedException(DriverEntity driverEntity, String url)
	{
		super(driverEntity + " 's driver can not accept url [" + url + "]");
		this.driverEntity = driverEntity;
		this.url = url;
	}

	public DriverEntity getDriverEntity()
	{
		return driverEntity;
	}

	public String getUrl()
	{
		return url;
	}
}
