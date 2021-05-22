package com.ctjsoft.orderserver.order.domain;

import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 定时任务对象 cron_job
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Data
public class CronJob extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 表达式id */
    private String cronId;

    /** 工单id */
    private String orderId;

    /** 创建人 */
    private String creatorId;

    /** 状态 */
    private String status;

}
