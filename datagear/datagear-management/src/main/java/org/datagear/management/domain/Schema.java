/*
 * Copyright 2018 datagear.tech
 *
 * Licensed under the LGPLv3 license:
 * http://www.gnu.org/licenses/lgpl-3.0.html
 */

package org.datagear.management.domain;

import java.util.Date;

import org.datagear.connection.DriverEntity;

/**
 * 数据库模式实体。
 * 
 * @author datagear@163.com
 *
 */
public class Schema extends AbstractStringIdEntity
		implements CreateUserEntity<String>, DataPermissionEntity<String>, Cloneable
{
	private static final long serialVersionUID = 1L;

	/** 授权资源类型 */
	public static final String AUTHORIZATION_RESOURCE_TYPE = "data_source";

	/*------------------------------------------------------*/
	/*
	 * 从业务角度看，对数据源的授权不应是对其记录本身，而是它包含表中的数据。
	 * 所以，这里扩展了Authorization.PERMISSION_READ_START权限， 授权时，仅支持对数据源授予下面这些权限。
	 * 这样，即不会暴露数据源记录本身的编辑、删除权限，同时又能满足业务需求。
	 */

	/** 数据源内的表数据权限：读取 */
	public static final int PERMISSION_TABLE_DATA_READ = Authorization.PERMISSION_READ_START;

	/** 数据源内的表数据权限：编辑 */
	public static final int PERMISSION_TABLE_DATA_EDIT = Authorization.PERMISSION_READ_START + 4;

	/** 数据源内的表数据权限：删除 */
	public static final int PERMISSION_TABLE_DATA_DELETE = Authorization.PERMISSION_READ_START + 8;

	/*------------------------------------------------------*/

	public static final String PROPERTY_TITLE = "title";

	/** 标题 */
	private String title;

	/** 连接URL */
	private String url;

	/** 连接用户 */
	private String user;

	/** 连接密码 */
	private String password;

	/** 此模式的创建用户 */
	private User createUser;

	/** 此模式的创建时间 */
	private Date createTime;
	private String tablespaceName;
	private String total;
	private String used;
	private String free;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTablespaceName() {
		return tablespaceName;
	}

	public void setTablespaceName(String tablespaceName) {
		this.tablespaceName = tablespaceName;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getFree() {
		return free;
	}

	public void setFree(String free) {
		this.free = free;
	}


	/** 数据库驱动程序路径名 */
	private DriverEntity driverEntity;

	/** 权限 */
	private int dataPermission = PERMISSION_NOT_LOADED;

	public Schema()
	{
		super();
	}

	public Schema(String id, String title, String url, String user, String password)
	{
		super(id);
		this.title = title;
		this.url = url;
		this.user = user;
		this.password = password;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUser()
	{
		return user;
	}

	public void setUser(String user)
	{
		this.user = user;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean hasCreateUser()
	{
		return (this.createUser != null);
	}

	@Override
	public User getCreateUser()
	{
		return createUser;
	}

	@Override
	public void setCreateUser(User createUser)
	{
		this.createUser = createUser;
	}

	public boolean hasCreateTime()
	{
		return (this.createTime != null);
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public boolean hasDriverEntity()
	{
		if (this.driverEntity == null)
			return false;

		String driverEntityId = this.driverEntity.getId();

		return (driverEntityId != null && !driverEntityId.isEmpty());
	}

	public DriverEntity getDriverEntity()
	{
		return driverEntity;
	}

	public void setDriverEntity(DriverEntity driverEntity)
	{
		this.driverEntity = driverEntity;
	}

	@Override
	public int getDataPermission()
	{
		return dataPermission;
	}

	@Override
	public void setDataPermission(int dataPermission)
	{
		this.dataPermission = dataPermission;
	}

	/**
	 * 清除密码属性值。
	 * <p>
	 * 密码是敏感信息，某些情况下需要清除。
	 * </p>
	 * 
	 */
	public void clearPassword()
	{
		this.password = null;
	}

	public static boolean isReadTableDataPermission(int permission)
	{
		return permission >= PERMISSION_TABLE_DATA_READ && permission < PERMISSION_TABLE_DATA_EDIT;
	}

	public static boolean isEditTableDataPermission(int permission)
	{
		return permission >= PERMISSION_TABLE_DATA_EDIT && permission < PERMISSION_TABLE_DATA_DELETE;
	}

	public static boolean isDeleteTableDataPermission(int permission)
	{
		return permission >= PERMISSION_TABLE_DATA_DELETE;
	}

	public static boolean canReadTableData(int permission)
	{
		return permission >= PERMISSION_TABLE_DATA_READ;
	}

	public static boolean canEditTableData(int permission)
	{
		return permission >= PERMISSION_TABLE_DATA_EDIT;
	}

	public static boolean canDeleteTableData(int permission)
	{
		return permission >= PERMISSION_TABLE_DATA_DELETE;
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " [title=" + title + ", url=" + url + ", user=" + user + ", createUser="
				+ createUser + ", createTime=" + createTime + ", driverEntity=" + driverEntity + "]";
	}
}
