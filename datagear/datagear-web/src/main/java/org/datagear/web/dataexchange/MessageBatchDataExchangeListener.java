/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.web.dataexchange;

import java.util.Locale;

import org.datagear.dataexchange.BatchDataExchangeListener;
import org.datagear.dataexchange.DataExchangeException;
import org.datagear.dataexchange.SubDataExchange;
import org.datagear.web.util.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

/**
 * 发送消息的{@linkplain BatchDataExchangeListener}。
 * 
 * @author datagear@163.com
 *
 * @param <T>
 */
public class MessageBatchDataExchangeListener extends MessageDataExchangeListener implements BatchDataExchangeListener
{
	protected static final Logger LOGGER = LoggerFactory.getLogger(MessageBatchDataExchangeListener.class);

	public MessageBatchDataExchangeListener()
	{
		super();
	}

	public MessageBatchDataExchangeListener(MessageChannel messageChannel,
			String dataExchangeServerChannel, MessageSource messageSource, Locale locale)
	{
		super(messageChannel, dataExchangeServerChannel, messageSource, locale);
	}

	@Override
	public void onSubmitSuccess(SubDataExchange subDataExchange)
	{
		sendMessage(new SubSubmitSuccess(subDataExchange.getId()));
	}

	@Override
	public void onSubmitFail(SubDataExchange subDataExchange)
	{
		sendMessage(new SubSubmitFail(subDataExchange.getId()));
	}

	@Override
	public void onCancel(SubDataExchange subDataExchange)
	{
		sendMessage(new SubCancelSuccess(subDataExchange.getId()));
	}

	@Override
	protected DataExchangeMessage buildStartMessage()
	{
		return new Start();
	}

	@Override
	protected DataExchangeMessage buildExceptionMessage(DataExchangeException e)
	{
		return new Exception(resolveDataExchangeExceptionI18n(e));
	}

	@Override
	protected DataExchangeMessage buildSuccessMessage()
	{
		return new Success();
	}

	@Override
	protected DataExchangeMessage buildFinishMessage()
	{
		return new Finish(evalDuration());
	}

	/**
	 * 数据交换开始消息。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class Start extends DataExchangeMessage
	{
		public Start()
		{
			super();
		}
	}

	/**
	 * 数据交换异常消息。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class Exception extends DataExchangeMessage
	{
		private String content;

		public Exception()
		{
			super();
		}

		public Exception(String content)
		{
			super();
			this.content = content;
		}

		public String getContent()
		{
			return content;
		}

		public void setContent(String content)
		{
			this.content = content;
		}
	}

	public static class Success extends DataExchangeMessage
	{
		public Success()
		{
			super();
		}
	}

	/**
	 * 数据交换完成消息。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class Finish extends DataExchangeMessage
	{
		private long duration;

		public Finish()
		{
			super();
		}

		public Finish(long duration)
		{
			super();
			this.duration = duration;
		}

		public long getDuration()
		{
			return duration;
		}

		public void setDuration(long duration)
		{
			this.duration = duration;
		}
	}

	/**
	 * 子数据交换提交成功消息。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class SubSubmitSuccess extends SubDataExchangeMessage
	{
		public static final int ORDER = 0;

		public SubSubmitSuccess()
		{
			super();
		}

		public SubSubmitSuccess(String subDataExchangeId)
		{
			super(subDataExchangeId, 0);
		}
	}

	/**
	 * 子数据交换提交失败消息。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class SubSubmitFail extends SubDataExchangeMessage
	{
		public static final int ORDER = 0;

		public SubSubmitFail()
		{
			super();
		}

		public SubSubmitFail(String subDataExchangeId)
		{
			super(subDataExchangeId, ORDER);
		}
	}

	/**
	 * 子数据交换取消成功消息。
	 * 
	 * @author datagear@163.com
	 *
	 */
	public static class SubCancelSuccess extends SubDataExchangeMessage
	{
		public static final int ORDER = SubSubmitSuccess.ORDER + 99;

		public SubCancelSuccess()
		{
			super();
		}

		public SubCancelSuccess(String subDataExchangeId)
		{
			super(subDataExchangeId, ORDER);
		}
	}
}
