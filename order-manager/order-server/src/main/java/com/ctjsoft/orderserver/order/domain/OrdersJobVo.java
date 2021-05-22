package com.ctjsoft.orderserver.order.domain;

import lombok.Data;

/**
 * 工单管理对象 orders
 *
 * @author zzz
 * @date 2021-03-10
 */
@Data
public class OrdersJobVo extends OrdersVo {

    private String jobId;
    private String jobStatus;
    private String cronName;
    private String cronText;

    //  jobStatus,b.name as cronName,b.cron as cronText


}