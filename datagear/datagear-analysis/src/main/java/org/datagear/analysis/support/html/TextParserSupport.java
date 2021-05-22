/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.analysis.support.html;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * 文本解析支持类。
 * 
 * @author datagear@163.com
 *
 */
public class TextParserSupport
{
	public TextParserSupport()
	{
		super();
	}

	/**
	 * 读取最多指定字符数的字符串。
	 * 
	 * @param reader
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public String readString(Reader reader, int count) throws IOException
	{
		char[] chars = new char[count];

		int readed = reader.read(chars);

		return new String(chars, 0, readed);
	}

	/**
	 * 读取最多指定字符。
	 * 
	 * @param reader
	 * @param out
	 * @param count
	 * @return
	 * @throws IOException
	 */
	public void readChars(Reader reader, StringBuilder out, int count) throws IOException
	{
		for (int i = 0; i < count; i++)
		{
			int c = reader.read();

			if (isValidReadChar(c))
				appendChar(out, c);
			else
				break;
		}
	}

	/**
	 * 读取到{@code '}或者{@code "}引号的下一个字符。
	 * <p>
	 * 下一个字符不会写入{@code out}。
	 * </p>
	 * 
	 * @param in
	 * @param out
	 * @param quote {@code '}或者{@code "}
	 * @return 引号的下一个字符、-1
	 * @throws IOException
	 */
	public int readQuoted(Reader in, StringBuilder out, int quote) throws IOException
	{
		int c = -1;
		boolean inEscape = false;

		while ((c = in.read()) > -1)
		{
			appendChar(out, c);

			if (inEscape)
				inEscape = false;
			else
			{
				if (c == '\\')
					inEscape = true;
				else if (c == quote)
					break;
			}
		}

		if (c == quote)
			c = in.read();

		return c;
	}

	/**
	 * 读取到{@linkplain Predicate#test(char)}为{@code false}。
	 * 
	 * @param in
	 * @param out
	 *            读取字符写入缓存，为{@code null}则不写入
	 * @param predicate
	 * @param appendLast
	 *            结束字符是否也写入缓存
	 * @return
	 * @throws IOException
	 */
	public int readUntil(Reader in, StringBuilder out, Predicate predicate, boolean appendLast) throws IOException
	{
		int c = -1;

		while ((c = in.read()) > -1)
		{
			if (predicate.test(c))
				appendCharIfNotNull(out, c);
			else
			{
				if (appendLast)
					appendCharIfNotNull(out, c);

				break;
			}
		}

		return c;
	}

	/**
	 * 跳过空格或者注释。
	 * 
	 * @param in
	 * @param out
	 *            读取字符写入缓存，为{@code null}则不写入
	 * @param appendLast
	 *            结束字符是否也写入缓存
	 * @return 非空格或注释的内容、空字符串
	 * @throws IOException
	 */
	public String skipWhitespaceOrBlockLineComment(Reader in, StringBuilder out, boolean appendLast) throws IOException
	{
		int c = skipWhitespace(in, out, false);

		if (c == -1)
			return new String("");
		else if (c == '/')
		{
			int next = in.read();

			boolean isComment = false;

			// 行注释
			if (next == '/')
			{
				appendCharIfNotNull(out, c);
				appendCharIfNotNull(out, next);
				c = skipLineComment(in, out, false);
				isComment = true;
			}
			// 块注释
			else if (next == '*')
			{
				appendCharIfNotNull(out, c);
				appendCharIfNotNull(out, next);
				c = skipBlockComment(in, out, false);
				isComment = true;
			}

			if (isComment)
			{
				if (isWhitespace(c))
				{
					appendCharIfNotNull(out, c);
					return skipWhitespaceOrBlockLineComment(in, out, appendLast);
				}
				else
				{
					if (appendLast)
						appendCharIfNotNull(out, c);

					StringBuilder sb = new StringBuilder();
					appendChar(sb, c);

					return sb.toString();
				}
			}
			else
			{
				if (appendLast)
					appendCharIfNotNull(out, c);

				StringBuilder sb = new StringBuilder();
				appendChar(sb, c);

				if (isValidReadChar(next))
					appendChar(sb, next);

				return sb.toString();
			}
		}
		else
		{
			if (appendLast)
				appendCharIfNotNull(out, c);

			StringBuilder sb = new StringBuilder();
			appendChar(sb, c);

			return sb.toString();
		}
	}

	/**
	 * 读取到行注释“<code>//</code>”结束。
	 * 
	 * @param in
	 * @param out
	 *            读取字符写入缓存，为{@code null}则不写入
	 * @param appendLast
	 *            结束字符是否也写入缓存
	 * @return 注释结束符的下一个字符、-1
	 * @throws IOException
	 */
	public int skipLineComment(Reader in, StringBuilder out, boolean appendLast) throws IOException
	{
		int c = -1;

		while ((c = in.read()) > -1)
		{
			appendCharIfNotNull(out, c);

			if (c == '\n')
				break;
		}

		if (c == '\n')
		{
			c = in.read();

			if (appendLast)
				appendCharIfValid(out, c);
		}

		return c;
	}

	/**
	 * 读取到块注释“<code>*</code><code>/</code>”结束。
	 * 
	 * @param in
	 * @param out
	 *            读取字符写入缓存，为{@code null}则不写入
	 * @param appendLast
	 *            结束字符是否也写入缓存
	 * @return 注释结束的下一个字符、-1
	 * @throws IOException
	 */
	public int skipBlockComment(Reader in, StringBuilder out, boolean appendLast) throws IOException
	{
		int c = -1;

		while ((c = in.read()) > -1)
		{
			appendCharIfNotNull(out, c);

			if (c == '/')
			{
				int len = out.length();
				int prev = (len > 1 ? out.charAt(len - 2) : -1);

				if (prev == '*')
					break;
			}
		}

		if (c == '/')
		{
			c = in.read();

			if (appendLast)
				appendCharIfValid(out, c);
		}

		return c;
	}

	/**
	 * 读取到非空格。
	 * 
	 * @param in
	 * @param out
	 *            读取字符写入缓存，为{@code null}则不写入
	 * @param appendLast
	 *            结束字符是否也写入缓存
	 * @return 非空格字符、-1
	 * @throws IOException
	 */
	public int skipWhitespace(Reader in, StringBuilder out, boolean appendLast) throws IOException
	{
		int c = -1;

		while ((c = in.read()) > -1)
		{
			if (isWhitespace(c))
				appendCharIfNotNull(out, c);
			else
			{
				if (appendLast)
					appendCharIfNotNull(out, c);

				break;
			}
		}

		return c;
	}

	/**
	 * 追加字符。
	 * 
	 * @param sb
	 * @param c
	 */
	public void appendChar(StringBuilder sb, int c)
	{
		sb.appendCodePoint(c);
	}

	/**
	 * 追加字符。
	 * <p>
	 * 如果{@code sb}为{@code null}，则什么也不做并返回{@code false}。
	 * </p>
	 * 
	 * @param sb
	 * @param c
	 * @return
	 */
	public boolean appendCharIfNotNull(StringBuilder sb, int c)
	{
		if (sb == null)
			return false;

		sb.appendCodePoint(c);
		return true;
	}

	/**
	 * 追加字符。
	 * <p>
	 * 如果{@code sb}为{@code null}或者{@code c}不是合法字符，则什么也不做并返回{@code false}。
	 * </p>
	 * 
	 * @param sb
	 * @param c
	 * @return
	 */
	public boolean appendCharIfValid(StringBuilder sb, int c)
	{
		if (sb == null)
			return false;

		if (!isValidReadChar(c))
			return false;

		sb.appendCodePoint(c);
		return true;
	}

	public void append(Writer out, StringBuilder sb) throws IOException
	{
		out.write(sb.toString());
	}

	public void appendAndClear(Writer out, StringBuilder sb) throws IOException
	{
		out.write(sb.toString());
		clear(sb);
	}

	public void appendIfValid(Writer out, int c) throws IOException
	{
		if (isValidReadChar(c))
			out.write(c);
	}

	/**
	 * 是否是有效的读取字符。
	 * 
	 * @param c
	 * @return
	 */
	public boolean isValidReadChar(int c)
	{
		return c > -1;
	}

	/**
	 * 是否空格字符。
	 * 
	 * @param c
	 * @return
	 */
	public boolean isWhitespace(int c)
	{
		return Character.isWhitespace(c);
	}

	/**
	 * 删除末尾字符。
	 * 
	 * @param sb
	 * @param count
	 */
	public void deleteTail(StringBuilder sb, int count)
	{
		int len = sb.length();

		sb.delete(len - count, len);
	}

	/**
	 * 清除{@linkplain StringBuilder}。
	 * 
	 * @param sb
	 */
	public void clear(StringBuilder sb)
	{
		if (sb == null || isEmpty(sb))
			return;

		sb.delete(0, sb.length());
	}

	/**
	 * {@code sb}是否以{@code tail}结尾。
	 * 
	 * @param sb
	 * @param tail
	 * @return
	 */
	public boolean isTail(StringBuilder sb, String tail)
	{
		int sbLen = sb.length();
		int tailLen = tail.length();

		if (sbLen < tailLen)
			return false;

		for (int i = tailLen - 1; i >= 0; i--)
		{
			if (tail.charAt(i) != sb.charAt(i))
				return false;
		}

		return true;
	}

	/**
	 * {@linkplain StringBuilder}是否为空。
	 * 
	 * @param sb
	 * @return
	 */
	public boolean isEmpty(StringBuilder sb)
	{
		return (sb == null || sb.length() == 0);
	}

	/**
	 * {@linkplain StringBuilder}是否不为空。
	 * 
	 * @param sb
	 * @return
	 */
	public boolean isNotEmpty(StringBuilder sb)
	{
		return (sb != null && sb.length() > 0);
	}

	public StringBuilder createStringBuilder()
	{
		return new StringBuilder();
	}

	public String toString(StringBuilder sb)
	{
		return sb.toString();
	}

	/**
	 * 获取引号内容。
	 * 
	 * @param sb
	 * @return
	 */
	protected String valueIfQuoted(StringBuilder sb)
	{
		return valueIfQuoted(sb.toString());
	}

	/**
	 * 获取引号内容。
	 * 
	 * @param str
	 * @return
	 */
	protected String valueIfQuoted(String str)
	{
		int len = str.length();

		if (len < 2)
			return str.toString();

		int ch = str.charAt(0);
		int ct = str.charAt(len - 1);

		if ((ch == '\'' && ct == '\'') || (ch == '"' && ct == '"'))
			return str.substring(1, len - 1);
		else
			return str.toString();
	}

	public static interface Predicate
	{
		/**
		 * 判断字符是否合法。
		 * 
		 * @param c
		 * @return
		 */
		boolean test(int c);
	}

	public static class WhitespacePredicate implements Predicate
	{
		@Override
		public boolean test(int c)
		{
			return Character.isWhitespace(c);
		}
	}

	public static class NotWhitespacePredicate implements Predicate
	{
		@Override
		public boolean test(int c)
		{
			return !Character.isWhitespace(c);
		}
	}
}
