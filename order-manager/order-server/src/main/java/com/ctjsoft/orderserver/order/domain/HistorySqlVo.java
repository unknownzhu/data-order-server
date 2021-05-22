package com.ctjsoft.orderserver.order.domain;

import lombok.Data;

/**
 * 常用语句对象 jx_common_sql
 *
 * @author zzz
 * @date 2021-03-24
 */
@Data
public class HistorySqlVo extends HistorySql {

    private static final long serialVersionUID = 1L;


    private String dbName;
    private String dbTypeName;
    private String creatorName;
    private String typeName;

    /*

     */
/** 主键 *//*

    private String id;

    */
/** 名称 *//*

    private String name;

    private String sqls;


    */
/** 类别id *//*

    private String typeId;

    */
/** 创建人 *//*

    private String creatorId;

    */
/** 数据库类型ID *//*

    private String dbType;

    */
    /**
     * 显示范围：0 所有人 1仅管理员 2 创建人及管理员
     *//*

    private String showType;

*/


}
