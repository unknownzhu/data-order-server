/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.connection;

/**
 * {@linkplain DriverEntityManager}在未找到指定名称的驱动库时将抛出此异常。
 * 
 * @author datagear@163.com
 *
 */
public class DriverLibraryNotFoundException extends DriverEntityManagerException
{
	private static final long serialVersionUID = 1L;

	private DriverEntity driverEntity;

	private String libraryName;

	public DriverLibraryNotFoundException(DriverEntity driverEntity, String libraryName)
	{
		super(driverEntity + " 's library [" + libraryName + "] not found");

		this.driverEntity = driverEntity;
		this.libraryName = libraryName;
	}

	public DriverEntity getDriverEntity()
	{
		return driverEntity;
	}

	public String getLibraryName()
	{
		return libraryName;
	}
}
