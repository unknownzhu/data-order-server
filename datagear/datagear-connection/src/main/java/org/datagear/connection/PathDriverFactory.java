/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.connection;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.Driver;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 特定路径JDBC驱动工厂。
 * <p>
 * 此类实例在使用前需要调用其{@linkplain #init()}方法，在弃用前，需要调用其{@linkplain #release()}方法。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class PathDriverFactory
{
	private static final Logger LOGGER = LoggerFactory.getLogger(PathDriverFactory.class);

	/** 驱动程序库路径 */
	private File path;

	private PathDriverClassLoader pathDriverClassLoader;

	private Object driverTool;

	public PathDriverFactory(String path)
	{
		this(new File(path));
	}

	public PathDriverFactory(File path)
	{
		super();
		this.path = path;
	}

	public File getPath()
	{
		return path;
	}

	/**
	 * 初始化。
	 * 
	 * @throws PathDriverFactoryException
	 */
	public synchronized void init() throws PathDriverFactoryException
	{
		if (this.pathDriverClassLoader != null)
			return;

		this.pathDriverClassLoader = initPathClassLoader(this.path);

		try
		{
			Class<?> driverToolClass = this.pathDriverClassLoader.loadClass(DriverTool.class.getName());
			this.driverTool = driverToolClass.newInstance();
		}
		catch (ClassNotFoundException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (InstantiationException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new PathDriverFactoryException(e);
		}
	}

	/**
	 * 释放资源。
	 * 
	 * @throws PathDriverFactoryException
	 */
	public synchronized void release() throws PathDriverFactoryException
	{
		try
		{
			releaseJdbcDrivers();
		}
		finally
		{
		}
	}

	/**
	 * 初始化{@linkplain PathDriverClassLoader}。
	 * 
	 * @param path
	 * @return
	 */
	protected PathDriverClassLoader initPathClassLoader(File path)
	{
		PathDriverClassLoader classLoader = new PathDriverClassLoader(path);

		return classLoader;
	}

	/**
	 * 获取路径上次修改时间。
	 * 
	 * @return
	 */
	public long getPathLastModified()
	{
		return getPathLastModified(this.path);
	}

	/**
	 * 获取指定类名的JDBC驱动程序。
	 * 
	 * @param driverClassName
	 * @return
	 * @throws PathDriverFactoryException
	 */
	public synchronized Driver getDriver(String driverClassName) throws PathDriverFactoryException
	{
		try
		{
			Class.forName(driverClassName, true, this.pathDriverClassLoader);
		}
		catch (ClassNotFoundException e)
		{
			throw new DriverNotFoundException(this.path.getPath(), driverClassName, e);
		}
		catch (ClassFormatError e)
		{
			throw new DriverClassFormatErrorException(e);
		}
		catch (Throwable t)
		{
			throw new PathDriverFactoryException(t);
		}

		try
		{
			Driver driver = (Driver) this.driverTool.getClass().getMethod("getDriver", String.class)
					.invoke(this.driverTool, driverClassName);

			if (driver == null)
				throw new PathDriverFactoryException(
						"No Driver named [" + driverClassName + "] found in [" + this.path + "]");

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("Get JDBC driver [" + driverClassName + "] in path [" + this.path + "]");

			return driver;
		}
		catch (IllegalArgumentException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (SecurityException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new PathDriverFactoryException(e);
		}
	}

	/**
	 * 释放{@linkplain Driver}资源。
	 * 
	 * @throws PathDriverFactoryException
	 */
	protected void releaseJdbcDrivers() throws PathDriverFactoryException
	{
		try
		{
			@SuppressWarnings("unchecked")
			List<String> driverNames = (List<String>) this.driverTool.getClass().getMethod("deregisterDrivers")
					.invoke(this.driverTool);

			if (LOGGER.isDebugEnabled())
				LOGGER.debug("deregister JDBC drivers loaded in path [" + this.path + "] :" + driverNames);
		}
		catch (IllegalArgumentException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (SecurityException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (IllegalAccessException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (InvocationTargetException e)
		{
			throw new PathDriverFactoryException(e);
		}
		catch (NoSuchMethodException e)
		{
			throw new PathDriverFactoryException(e);
		}
	}

	protected PathDriverClassLoader getPathDriverClassLoader()
	{
		return this.pathDriverClassLoader;
	}

	/**
	 * 获取路径的上次修改时间。
	 * 
	 * @param path
	 * @return
	 */
	protected long getPathLastModified(File path)
	{
		long pathLastModified = path.lastModified();

		if (path.isDirectory())
		{
			File[] children = path.listFiles();

			if (children != null)
			{
				for (File child : children)
				{
					long childLastModified = child.lastModified();

					if (childLastModified > pathLastModified)
						pathLastModified = childLastModified;
				}
			}
		}

		return pathLastModified;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [path=" + path + "]";
	}
}
