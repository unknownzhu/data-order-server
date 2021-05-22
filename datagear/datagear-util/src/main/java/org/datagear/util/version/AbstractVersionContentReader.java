/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.util.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 抽象版本内容读取器。
 * <p>
 * 它从符合给定格式的输入流中读取版本内容。
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public abstract class AbstractVersionContentReader
{
	public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

	/** UTF-8编码 */
	public static final String ENCODING_UTF8 = "UTF-8";

	public AbstractVersionContentReader()
	{
		super();
	}

	/**
	 * 解析{@linkplain VersionContent}列表。
	 * 
	 * @param reader
	 * @param from
	 *            起始版本，为{@code null}表示从第一个版本
	 * @param to
	 *            结束版本（包含），为{@code null}表示至最后一个版本
	 * @param containsFrom
	 *            是否包含起始版本
	 * @param contailsTo
	 *            是否包含结束版本
	 * @return
	 * @throws IOException
	 */
	protected List<VersionContent> resolveVersionContents(BufferedReader reader, Version from, Version to,
			boolean containsFrom, boolean contailsTo) throws IOException
	{
		List<VersionContent> versionContents = new ArrayList<VersionContent>();

		VersionContent versionContent = null;
		List<String> contents = null;

		StringBuilder lineCache = new StringBuilder();

		String line = null;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null)
		{
			if (isVersionLine(line))
			{
				Version myVersion = resolveVersion(line);

				if (to != null && (myVersion.isHigherThan(to) || (!contailsTo && myVersion.equals(to))))
				{
					break;
				}

				if (from == null || myVersion.isHigherThan(from) || (containsFrom && myVersion.equals(from)))
				{
					if (versionContent != null)
					{
						finishVersionContent(versionContent, contents, lineCache);

						versionContent.setVersionEndLine(lineNumber - 1);
						versionContents.add(versionContent);

						versionContent = null;
					}

					if (versionContent == null)
					{
						versionContent = new VersionContent(myVersion);
						contents = new ArrayList<String>();

						versionContent.setContents(contents);
						versionContent.setVersionStartLine(lineNumber);
					}
				}
			}
			else if (isCommentLine(line))
			{

			}
			else if (versionContent != null)
			{
				handleVersionContentLine(versionContent, contents, lineCache, line);
			}
			else
				;

			lineNumber++;
		}

		if (versionContent != null)
		{
			finishVersionContent(versionContent, contents, lineCache);

			versionContent.setVersionEndLine(lineNumber);
			versionContents.add(versionContent);

			versionContent = null;
		}

		return versionContents;
	}

	/**
	 * 处理版本内容行。
	 * 
	 * @param versionContent
	 *            行所处的{@linkplain VersionContent}
	 * @param contents
	 *            行所处的{@linkplain VersionContent#getContents()}
	 * @param cache
	 *            用于对行进行缓存处理的字符串构建器
	 * @param line
	 *            行字符串，原始行内容，未做任何处理
	 */
	protected abstract void handleVersionContentLine(VersionContent versionContent, List<String> contents,
			StringBuilder cache, String line);

	/**
	 * 完成版本内容。
	 * 
	 * @param versionContent
	 * @param contents
	 * @param cache
	 */
	protected abstract void finishVersionContent(VersionContent versionContent, List<String> contents,
			StringBuilder cache);

	/**
	 * 是否是注释行。
	 * 
	 * @param line
	 * @return
	 */
	protected boolean isCommentLine(String line)
	{
		return line.startsWith("--");
	}

	/**
	 * 判断给定行是否是版本标识行。
	 * 
	 * @param line
	 * @return
	 */
	protected abstract boolean isVersionLine(String line);

	/**
	 * 从行字符串中解析版本号。
	 * 
	 * @param line
	 * @return
	 */
	protected abstract Version resolveVersion(String line);
}
