package com.ctjsoft.orderserver.analyze.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author gcy
 * @since 2020-05-11
 */
@TableName("MODEL_TARGET_TABLE_COLUMN")
public class ModelTargetTableColumn extends Model<ModelTargetTableColumn> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("ID")
    private String id;
    /**
     * 字段名
     */
    @TableField("COLUMN_NAME")
    private String columnName;
    /**
     * 字段描述
     */
    @TableField("COLUMN_REMARK")
    private String columnRemark;
    /**
     * 是否是主键，1为主键。默认0
     */
    @TableField("IS_PRIMARY")
    private String isPrimary;
    /**
     * 数据源ID
     */
    @TableField("TARGET_TABLE_ID")
    private String targetTableId;
    /**
     * 字段类型
     */
    @TableField("COLUMN_TYPE")
    private String columnType;
    /**
     * 字段大小
     */
    @TableField("COLUMN_LENGTH")
    private String columnLength;
    /**
     * 是否能为空，默认可以，1为不可为空
     */
    @TableField("IS_CAN_NULL")
    private String isCanNull;
    
    /**
     * 默认值
     */
    @TableField("DEFAULT_VALUE")
    private String defaultValue;
    
    /**
     * 小数位数
     */
    @TableField("DECIMAL_COUNT")
    private String decimalCount; 
    
    
    /**
     * 字段中文名称
     */
    @TableField("NAME")
    private String name;
    
    
    @TableField(exist=false)
    private ModelTargetTableColumnType ModelTargetTableColumnType;
    
    
    
    public ModelTargetTableColumnType getModelTargetTableColumnType() {
		return ModelTargetTableColumnType;
	}

	public void setModelTargetTableColumnType(ModelTargetTableColumnType modelTargetTableColumnType) {
		ModelTargetTableColumnType = modelTargetTableColumnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDecimalCount() {
		return decimalCount;
	}

	public void setDecimalCount(String decimalCount) {
		this.decimalCount = decimalCount;
	}

	@TableField(exist=false)
    private ModelTargetTable modelTargetTable;
    

    public ModelTargetTable getModelTargetTable() {
		return modelTargetTable;
	}

	public void setModelTargetTable(ModelTargetTable modelTargetTable) {
		this.modelTargetTable = modelTargetTable;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnRemark() {
        return columnRemark;
    }

    public void setColumnRemark(String columnRemark) {
        this.columnRemark = columnRemark;
    }

    public String getIsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(String isPrimary) {
        this.isPrimary = isPrimary;
    }

    public String getTargetTableId() {
        return targetTableId;
    }

    public void setTargetTableId(String targetTableId) {
        this.targetTableId = targetTableId;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnLength() {
        return columnLength;
    }

    public void setColumnLength(String columnLength) {
        this.columnLength = columnLength;
    }

    public String getIsCanNull() {
        return isCanNull;
    }

    public void setIsCanNull(String isCanNull) {
        this.isCanNull = isCanNull;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ModelTargetTableColumn{" +
        ", id=" + id +
        ", columnName=" + columnName +
        ", columnRemark=" + columnRemark +
        ", isPrimary=" + isPrimary +
        ", targetTableId=" + targetTableId +
        ", columnType=" + columnType +
        ", columnLength=" + columnLength +
        ", isCanNull=" + isCanNull +
        "}";
    }
}
