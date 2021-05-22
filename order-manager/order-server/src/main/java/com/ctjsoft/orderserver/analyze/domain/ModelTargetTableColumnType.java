package com.ctjsoft.orderserver.analyze.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 字段类型
 * </p>
 *
 * @author gcy
 * @since 2020-06-29
 */
@TableName("MODEL_TARGET_TABLE_COLUMN_TYPE")
public class ModelTargetTableColumnType extends Model<ModelTargetTableColumnType> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("ID")
    private String id;
    /**
     * 名称
     */
    @TableField("NAME")
    private String name;
     
    @TableField("MYSQL_TYPE_NAME")
    private String mysqlTypeName;
    
    @TableField("ORACLE_TYPE_NAME")
    private String oracleTypeName;
    
    @TableField("HIVE_TYPE_NAME")
    private String hiveTypeName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getMysqlTypeName() {
		return mysqlTypeName;
	}

	public void setMysqlTypeName(String mysqlTypeName) {
		this.mysqlTypeName = mysqlTypeName;
	}

	public String getOracleTypeName() {
		return oracleTypeName;
	}

	public void setOracleTypeName(String oracleTypeName) {
		this.oracleTypeName = oracleTypeName;
	}

	public String getHiveTypeName() {
		return hiveTypeName;
	}

	public void setHiveTypeName(String hiveTypeName) {
		this.hiveTypeName = hiveTypeName;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ModelTargetTableColumnType{" +
        ", id=" + id +
        ", name=" + name +
        "}";
    }
}
