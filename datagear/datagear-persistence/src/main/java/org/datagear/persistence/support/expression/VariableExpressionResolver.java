/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.persistence.support.expression;

/**
 * 变量表达式解析器。
 * <p>
 * 此类将表达式格式固化为<code>#{name:value}、#{value}</code>，用于解析变量表达式。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class VariableExpressionResolver extends NameExpressionResolver
{
	public VariableExpressionResolver()
	{
		super();
		super.setStartIdentifier(DEFAULT_START_IDENTIFIER_SHARP);
		super.setSeparator(DEFAULT_SEPARATOR);
		super.setEndIdentifier(DEFAULT_END_IDENTIFIER);
	}

	@Override
	public void setStartIdentifier(String startIdentifier)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEndIdentifier(String endIdentifier)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSeparator(String separator)
	{
		throw new UnsupportedOperationException();
	}
}
