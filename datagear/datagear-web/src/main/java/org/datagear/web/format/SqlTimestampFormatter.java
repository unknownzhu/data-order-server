/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.web.format;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Locale;

/**
 * {@linkplain java.sql.Timestamp}格式类。
 * 
 * @author datagear@163.com
 *
 */
public class SqlTimestampFormatter extends AbstractDateFormatter<Timestamp>
{
	public static final String PATTERN_DESC = "yyyy-MM-dd HH:mm:ss[.fff...]";

	protected static final String NANO_ZEROS = "000000000";

	/** 纳秒最大长度 */
	protected static final int NANO_MAX_LENGTH = 9;

	/** 纳秒分隔符 */
	protected static final char NANO_SEPARATOR = '.';

	public SqlTimestampFormatter()
	{
		super();
	}

	@Override
	public Timestamp parse(String text, Locale locale) throws ParseException
	{
		try
		{
			return Timestamp.valueOf(text);
		}
		catch (Exception e)
		{
			java.util.Date candidate = parseByPatterns(text, PATTERN_YEAR_MONTH_DAY_HOUR_MIN_SEC,
					PATTERN_YEAR_MONTH_DAY_HOUR_MIN, PATTERN_YEAR_MONTH_DAY_HOUR, PATTERN_YEAR_MONTH_DAY,
					PATTERN_YEAR_MONTH, PATTERN_YEAR);

			if (candidate != null)
			{
				Timestamp timestamp = new Timestamp(candidate.getTime());
				int nano = parseNano(text);
				timestamp.setNanos(nano);

				return timestamp;
			}
			else
				throw new ParseException(text, 0);
		}
	}

	@Override
	public String print(Timestamp object, Locale locale)
	{
		// XXX 统一采用SqlDateFormatter.print(Date, Locale)的算法
		String str = formatByPattern(object, PATTERN_YEAR_MONTH_DAY_HOUR_MIN_SEC);
		str += NANO_SEPARATOR + printNano(object);

		return str;
	}

	@Override
	public String getParsePatternDesc(Locale locale)
	{
		return PATTERN_DESC;
	}

	/**
	 * 解析时间戳字符串中的纳秒值。
	 * 
	 * @param timestamp
	 * @return
	 * @throws ParseException
	 */
	protected int parseNano(String timestamp) throws ParseException
	{
		int didx = timestamp.lastIndexOf(NANO_SEPARATOR);

		if (didx < 0 || didx >= timestamp.length() - 1)
			return 0;

		String nanoStr = timestamp.substring(didx + 1, timestamp.length());

		if (nanoStr.length() > NANO_MAX_LENGTH)
			throw new ParseException(timestamp, 0);

		nanoStr = nanoStr + NANO_ZEROS.substring(0, NANO_MAX_LENGTH - nanoStr.length());

		try
		{
			return Integer.parseInt(nanoStr);
		}
		catch (NumberFormatException e)
		{
			throw new ParseException(timestamp, 0);
		}
	}

	/**
	 * 打印纳秒字符串。
	 * 
	 * @param timestamp
	 * @return
	 */
	protected String printNano(Timestamp timestamp)
	{
		int nano = timestamp.getNanos();

		if (nano == 0)
			return "0";

		String nanosStr = Integer.toString(nano);

		nanosStr = NANO_ZEROS.substring(0, (NANO_MAX_LENGTH - nanosStr.length())) + nanosStr;

		char[] nanosChars = nanosStr.toCharArray();
		int truncIndex = NANO_MAX_LENGTH - 1;
		while (nanosChars[truncIndex] == '0')
		{
			truncIndex--;
		}

		nanosStr = new String(nanosChars, 0, truncIndex + 1);

		return nanosStr;
	}
}
