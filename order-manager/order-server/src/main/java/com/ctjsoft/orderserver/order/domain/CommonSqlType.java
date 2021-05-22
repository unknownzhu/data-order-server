package com.ctjsoft.orderserver.order.domain;


import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 语句类型对象 jx_common_sql_type
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Data
public class CommonSqlType extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 名称 */
    private String name;

    private String isNormal;

}
