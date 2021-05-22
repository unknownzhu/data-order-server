/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.management.domain;

/**
 * 抽象实体，它实现{@linkplain #getId()}和{@linkplain #setId(String)}逻辑，同时实现了
 * {@linkplain #hashCode()}和{@linkplain #equals(Object)}方法。
 * 
 * @author datagear@163.com
 *
 * @param <ID>
 */
public abstract class AbstractEntity<ID> implements Entity<ID>
{
	private static final long serialVersionUID = 1L;

	/** ID */
	private ID id;

	public AbstractEntity()
	{
	}

	public AbstractEntity(ID id)
	{
		this.id = id;
	}

	@Override
	public ID getId()
	{
		return id;
	}

	@Override
	public void setId(ID id)
	{
		this.id = id;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [id=" + id + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity<?> other = (AbstractEntity<?>) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}
