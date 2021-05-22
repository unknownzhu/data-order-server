package com.ctjsoft.orderserver.schedule;

import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.util.ReturnMessage;

import java.util.Map;

/**
 * @author zyx
 * @date 2020/5/22
 */
public interface IScheduleService {

    public Object startAllCronJobs();

    public Object checkJob();

    public Object startJobsByCronJobId(CronJob cronJob);

    void stopSchedule(String id);

 //   void restartSchedule(String id, String cron);

}