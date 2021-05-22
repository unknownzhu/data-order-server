package com.ctjsoft.orderserver.analyze.domain;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import org.datagear.web.config.DataSourceConfig;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author gcy
 * @since 2020-05-11
 */
@TableName("MODEL_TARGET_TABLE")
@Data
public class ModelTargetTable extends Model<ModelTargetTable> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("ID")
    private String id;
    /**
     * 表id
     */
    @TableField("TABLE_ID")
    private String tableId;
    /**
     * 表名
     */
    @TableField("TABLE_NAME")
    private String tableName;

    /**
     * 表描述
     */
    @TableField("TABLE_RENARK")
    private String tableRenark;
    /**
     * 表空间
     */
    @TableField("TABLE_SPACE")
    private String tableSpace;
    /**
     * 数据源ID
     */
    @TableField("DATA_SOURCE_ID")
    private String dataSourceId;
    
    /**
     * 1表示mysql2表示oracle3表示hive
     */
    @TableField("TABLE_TYPE")
    private String tableType;
    
    /**
     * 数据源ID
     */
    @TableField("IS_CREATE")
    private String isCreate;
    
    
    
    /**
     * 表中文名称
     */
    @TableField("NAME")
    private String name;


    /**
     * 表编码
     */
    @TableField("TABLE_CODE")
    private String tableCode;


    /**
     * 数据来源
     */
    @TableField("TABLE_SOURCE")
    private String tableSource;

    /**
     * 数据资源目录id
     */
    @TableField("DATA_LAYER")
    private String dataLayer;
    /**
     * 数据分层id
     */
    @TableField("HIERARCHY_ID")
    private String hierarchyId;

    /**
     * 版本号
     */
    @TableField("VERSION")
    private String version;

    
    @TableField("USER_ID")
    private String userId;
    
    @TableField("USER_NAME")
    private String userName;
    
    @TableField("CREATE_TIME")
    private Date createTime;
    
    
    @TableField("PARTTITION_COLUMN")
    private String parttitionColumn;
    
    @TableField("SUB_PARTTITION_COLUMN")
    private String subParttitionColumn;
    
    
    @TableField("PARTTITION_SQL")
    private String parttitionSql;

    @TableField("TEMPLET_ID")
    private String templetId;
    
	@TableField(exist = false)
    private List<ModelTargetTableColumn> listColumn;
    
    @TableField(exist = false)
    private DataSourceConfig dataSourceConfig;

    /**
     * 是否建立了分区 不参与数据库查询，
     * 目的是为了在表现层出现
     */
    @TableField(exist = false)
    private  String isParttition=null;


    public String getIsParttition() {
        return isParttition;
    }

    public void setIsParttition(String isParttition) {
        this.isParttition = isParttition;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "ModelTargetTable{" +
        ", id=" + id +
        ", tableName=" + tableName +
        ", tableRenark=" + tableRenark +
        ", tableSpace=" + tableSpace +
        ", dataSourceId=" + dataSourceId +
        "}";
    }
}
