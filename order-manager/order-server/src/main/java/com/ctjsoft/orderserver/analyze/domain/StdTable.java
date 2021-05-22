package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Data
public class StdTable implements Serializable {
	/**
	    * 新增表时version字段的初始版本
	*/
	public static final long INIT_VERSION = 1000;
	/**
	    * 新增表时changeVersion字段的初始版本
	*/
	public static final long INIT_CHANGE_VERSION = 10000;

	private String id;

	private String companyCode;

	private String systemCode;

	private String businessCode;

	private String tableCode;
	private String tableInCode;

	private String tableName;

	private String tableCnName;

	private String tableDes;

	private String businessKey;

	/**
	 * 是否部标 1 部标 0 上海扩展
	 */
	private Integer isStandard;

	private Long version;

	private Long changeVersion;

	private Date updateTime;

	private String updateUser;

	private Date createTime;

	private String createUser;

	private Long catalogEnumId;

	/**
	 * 逻辑库标准类型  1 标准表 2 扩充表 3扩展表
	 */
	private Integer tableType;

	/**
	 * 标准分类 0技术标准 1汇总标准
	 */
	private Integer stdType;
    /**
     * 0 未通过 1已通过
     */
    private Integer checkResult;
   
    private String failReason;

	private Integer isDeleted;
	private Integer tableOrder;

	private Integer isWzzx;
	
	private Integer isYyzw;	
	
	private Integer isJqrj;	
	
	private Integer isJrrj;
	
	private static final long serialVersionUID = 1L;
	
	
	
	/**
	 * 待完善
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj instanceof StdTable) {
			StdTable dto = (StdTable) obj;
			return ObjectUtils.compare(this.getTableName(), dto.getTableName()) == 0
					&& ObjectUtils.compare(this.getTableCnName(), dto.getTableCnName()) == 0;
		}
		return false;
	}
	
	/**
	 * 待完善
	 */
	@Override
	public int hashCode() {
		return Optional.ofNullable(this.getTableName()).map(String::hashCode).orElse(0)
				+ Optional.ofNullable(this.getTableCnName()).map(String::hashCode).orElse(0);
	}

}