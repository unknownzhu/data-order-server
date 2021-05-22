/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.util.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.datagear.util.IOUtil;

/**
 * 文件输入流{@linkplain ResourceFactory}。
 * 
 * @author datagear@163.com
 *
 */
public class FileInputStreamResourceFactory implements ResourceFactory<InputStream>
{
	private File file;

	public FileInputStreamResourceFactory()
	{
		super();
	}

	public FileInputStreamResourceFactory(File file)
	{
		super();
		this.file = file;
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	@Override
	public InputStream get() throws Exception
	{
		return new BufferedInputStream(new FileInputStream(this.file));
	}

	@Override
	public void release(InputStream resource) throws Exception
	{
		IOUtil.close(resource);
	}

	/**
	 * 构建{@linkplain FileInputStreamResourceFactory}。
	 * 
	 * @param file
	 * @return
	 */
	public static FileInputStreamResourceFactory valueOf(File file)
	{
		return new FileInputStreamResourceFactory(file);
	}
}
