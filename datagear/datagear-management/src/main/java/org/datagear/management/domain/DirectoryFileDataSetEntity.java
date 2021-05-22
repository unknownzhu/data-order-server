/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.management.domain;

import java.io.File;
import java.util.Map;

import org.datagear.analysis.support.AbstractDataSet;
import org.datagear.util.FileUtil;

/**
 * 目录内文件数据集实体。
 * <p>
 * 文件有两种类型：
 * 用户上传的文件，保存至{@linkplain #getDirectory()}内的{@linkplain #getFileName()}文件；
 * 服务器端文件，本来就存储在服务器磁盘指定目录内的文件，
 * </p>
 * 
 * @author datagear@163.com
 *
 */
public interface DirectoryFileDataSetEntity extends DataSetEntity
{
	/** 文件类型：用户上传文件 */
	String FILE_SOURCE_TYPE_UPLOAD = "UPLOAD";

	/** 文件类型：服务器端文件 */
	String FILE_SOURCE_TYPE_SERVER = "SERVER";

	/**
	 * 获取文件源类型。
	 * 
	 * @return
	 */
	String getFileSourceType();

	/**
	 * 设置文件类型：{@linkplain #FILE_SOURCE_TYPE_UPLOAD}、{@linkplain #FILE_SOURCE_TYPE_SERVER}。
	 * 
	 * @param fileSourceType
	 */
	void setFileSourceType(String fileSourceType);

	/**
	 * 获取用户上传文件的存储目录。
	 * 
	 * @return
	 */
	File getDirectory();

	/**
	 * 设置用户上传文件的存储目录。
	 * 
	 * @param directory
	 */
	void setDirectory(File directory);

	/**
	 * 获取用户上传文件的文件名。
	 * 
	 * @return
	 */
	String getFileName();

	/**
	 * 设置用户上传文件的存储文件名。
	 * 
	 * @param fileName
	 */
	void setFileName(String fileName);

	/**
	 * 获取用户上传文件的展示名。
	 * 
	 * @return
	 */
	String getDisplayName();

	/**
	 * 设置用户上传文件的文件展示名。
	 * 
	 * @param displayName
	 */
	void setDisplayName(String displayName);

	/**
	 * 获取服务器端文件所在的目录。
	 * 
	 * @return
	 */
	DataSetResDirectory getDataSetResDirectory();

	/**
	 * 设置服务器端文件所在的目录。
	 * 
	 * @param dataSetResDirectory
	 */
	void setDataSetResDirectory(DataSetResDirectory dataSetResDirectory);

	/**
	 * 获取服务器端文件的文件名（相对于{@linkplain #getDataSetResDirectory()}）。
	 * 
	 * @return
	 */
	String getDataSetResFileName();

	/**
	 * 设置服务器端文件的文件名（相对于{@linkplain #getDataSetResDirectory()}）。
	 * 
	 * @param fileName
	 */
	void setDataSetResFileName(String fileName);

	/**
	 * 将文件名作为<code>Freemarker</code>模板解析。
	 * <p>
	 * 实现规则应与{@linkplain AbstractDataSet#resolveAsFmkTemplate(String, Map)}一致。
	 * </p>
	 * 
	 * @param fileName
	 * @param paramValues
	 * @return
	 */
	String resolveFileNameAsFmkTemplate(String fileName, Map<String, ?> paramValues);

	FileSupport FILE_SUPPORT = new FileSupport();

	class FileSupport
	{
		public File getFile(DirectoryFileDataSetEntity entity, Map<String, ?> paramValues) throws Throwable
		{
			File file = null;

			if (FILE_SOURCE_TYPE_UPLOAD.equals(entity.getFileSourceType()))
				file = FileUtil.getFile(entity.getDirectory(), entity.getFileName());
			else if (FILE_SOURCE_TYPE_SERVER.equals(entity.getFileSourceType()))
			{
				// 服务器端文件名允许参数化
				String fileName = entity.resolveFileNameAsFmkTemplate(entity.getDataSetResFileName(), paramValues);

				File directory = FileUtil.getDirectory(entity.getDataSetResDirectory().getDirectory(), false);
				file = FileUtil.getFile(directory, fileName, false);
			}
			else
				throw new IllegalStateException("Unknown file source type :" + entity.getFileSourceType());

			return file;
		}
	}
}
