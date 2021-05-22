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
public class CronJobVo extends CronJob
{
    private static final long serialVersionUID = 1L;


    private String cronName;

    private String cronText;

    private String orderName;

    private String creatorName;

    private String cronStatus; //定时状态: 0 停用 1 启用

}
