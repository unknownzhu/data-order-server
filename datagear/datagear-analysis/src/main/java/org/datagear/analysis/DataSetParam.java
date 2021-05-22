/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.analysis;

/**
 * 数据集参数。
 * <p>
 * 此类描述{@linkplain DataSet}获取{@linkplain DataSetResult}所需要的输入参数信息。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public class DataSetParam extends AbstractDataNameType
{
	/** 是否必须 */
	private boolean required;

	/** 参数描述 */
	private String desc;

	/** 界面输入框类型 */
	private String inputType;

	/** 界面输入框载荷，比如：输入框为下拉选择时，定义选项内容JSON；输入概况为日期时，定义日期格式 */
	private String inputPayload;

	public DataSetParam()
	{
		super();
	}

	public DataSetParam(String name, String type, boolean required)
	{
		super(name, type);
		this.required = required;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public boolean hasDesc()
	{
		return (this.desc != null && !this.desc.isEmpty());
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public boolean hasInputType()
	{
		return (this.inputType != null && !this.inputType.isEmpty());
	}

	public String getInputType()
	{
		return inputType;
	}

	public void setInputType(String inputType)
	{
		this.inputType = inputType;
	}

	public boolean hasInputPayload()
	{
		return (this.inputPayload != null && !this.inputPayload.isEmpty());
	}

	public String getInputPayload()
	{
		return inputPayload;
	}

	public void setInputPayload(String inputPayload)
	{
		this.inputPayload = inputPayload;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [required=" + required + ", desc=" + desc + ", inputType=" + inputType
				+ ", inputPayload=" + inputPayload + "]";
	}

	/**
	 * {@linkplain DataSetParam#getType()}枚举。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class DataType
	{
		/** 字符串 */
		public static final String STRING = "STRING";

		/** 布尔值 */
		public static final String BOOLEAN = "BOOLEAN";

		/** 整数 */
		public static final String NUMBER = "NUMBER";
	}

	/**
	 * 常用的{@linkplain DataSetParam#getInputType()}枚举。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class InputType
	{
		/** 文本框 */
		public static final String TEXT = "text";

		/** 下拉框 */
		public static final String SELECT = "select";

		/** 日期 */
		public static final String DATE = "date";

		/** 时间 */
		public static final String TIME = "time";

		/** 日期时间 */
		public static final String DATETIME = "datetime";

		/** 单选框 */
		public static final String RADIO = "radio";

		/** 复选框 */
		public static final String CHECKBOX = "checkbox";

		/** 文本域 */
		public static final String TEXTAREA = "textarea";
	}
}
