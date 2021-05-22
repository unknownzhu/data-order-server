/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.web.controller;

/**
 * 文件未找到异常。
 * 
 * @author datagear@163.com
 *
 */
public class FileNotFoundException extends ControllerException
{
	private static final long serialVersionUID = 1L;

	private String fileName;

	public FileNotFoundException(String fileName)
	{
		super("No file named [" + fileName + "] is found");

		this.fileName = fileName;
	}

	public String getFileName()
	{
		return fileName;
	}
}
