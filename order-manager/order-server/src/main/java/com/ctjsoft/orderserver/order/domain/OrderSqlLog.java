package com.ctjsoft.orderserver.order.domain;


import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 语句执行日志对象 jx_order_sql_log
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Data
public class OrderSqlLog extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 工单id */
    private String orderId;

    /** 执行结果 */
    private String info;

    /** 任务id(仅定时任务有此字段） */
    private String cronJobId;

    private String sqls;

    private String flowId;

    private String schemaId;

    private String today;

    public OrderSqlLog(String schemaId, String today) {
        this.schemaId = schemaId;
        this.today = today;
    }

    public OrderSqlLog() {
     super();
    }
}
