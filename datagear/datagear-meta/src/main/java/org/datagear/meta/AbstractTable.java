/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.meta;

import java.io.Serializable;

/**
 * 抽象表元信息。
 * 
 * @author datagear@163.com
 *
 */
public class AbstractTable implements Serializable
{
	private static final long serialVersionUID = 1L;

	/** 名称 */
	private String name;

	/** 类型 */
	private String type;

	/** 描述 */
	private String comment;

	public AbstractTable()
	{
		super();
	}

	public AbstractTable(String name, String type)
	{
		super();
		this.name = name;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [name=" + name + ", type=" + type + ", comment=" + comment + "]";
	}
}
