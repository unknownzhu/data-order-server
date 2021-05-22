package com.ctjsoft.orderserver.order.domain;


import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 定时任务日志对象 cron_job_log
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Data
public class CronJobLog extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 任务id */
    private String cronJobId;

    /** 执行状态 */
    private String status;

    /** 执行结果 */
    private String resulrInfo;

}
