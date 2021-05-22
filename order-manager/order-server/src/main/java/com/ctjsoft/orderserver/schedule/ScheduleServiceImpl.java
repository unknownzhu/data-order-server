package com.ctjsoft.orderserver.schedule;

import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicionaryService;
import com.ctjsoft.orderserver.order.controller.OrdersController;
import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.mapper.CronJobMapper;
import com.ctjsoft.orderserver.order.mapper.CronMapper;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.util.ReturnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datagear.management.domain.Schema;
import org.datagear.management.service.SchemaService;
import org.datagear.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author zyx
 * @date 2020/5/22
 */
@Component
public class ScheduleServiceImpl implements IScheduleService {

    @Resource
    DefaultSchedulingConfigurer defaultSchedulingConfigurer;
    @Resource
    CronMapper cronMapper;
    @Resource
    CronJobMapper cronJobMapper;
    @Resource
    OrdersController ordersService;

    @Autowired
    private JxStdDatabaseDataDicionaryService stdDatabaseDataDicionaryService;

    @Autowired
    private SchemaService schemaService;
    Logger logger = LogManager.getLogger(LogManager.ROOT_LOGGER_NAME);

    @Value("${stdCheckCron}")
    private String stdCheckCron;


    @Override
    public Object checkJob() {

        defaultSchedulingConfigurer.addTriggerTask("stdCheckCron",
                new TriggerTask(
                        () -> {
                            System.out.println("stdCheckCron: " + stdCheckCron );

                            Query query = new Query();
                            List<Schema> list = schemaService.query(query);
                            for (Schema schema : list) {
                                String status = "";
                                try {
                                    stdDatabaseDataDicionaryService.reScan(schema.getId());
                                    status = "成功";
                                } catch (Exception e) {
                                    status = "失败";
                                    e.printStackTrace();
                                }
                                System.out.println(schema.getTitle() +"扫描:" +status);
                            }

                            System.out.println("schedule scan finish");

                      /*      try {
                                Query query = new Query();
                                List<Schema> list = schemaService.query(query);
                                for (Schema schema : list) {
                                    stdDatabaseDataDicionaryService.reScan(schema.getId());
                                }
                              //   Result result = ordersService.execSql(tmp.getOrderId(),tmp.getId());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                        },
                        new CronTrigger(stdCheckCron)));
        return null;
    }


    @Override
    public Object startAllCronJobs() {
        CronJob cronJob = new CronJob();
        cronJob.setStatus("1");
        List<CronJob> list = cronJobMapper.selectCronJobList(cronJob);
        for (CronJob tmp : list) {
            try {
                String cron = cronMapper.selectCronById(tmp.getCronId()).getCron();
                String tmpStr = tmp.getId();

                tmp.setUpdateTime(new Date());
                tmp.setStatus("1");
                cronJobMapper.updateCronJob(tmp);
                defaultSchedulingConfigurer.addTriggerTask(tmpStr,
                        new TriggerTask(
                                () -> {
                                    System.out.println(tmpStr);
                                    try {
                                        Result result = ordersService.execSql(tmp.getOrderId(), tmp.getId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                },
                                new CronTrigger(cron)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Object startJobsByCronJobId(CronJob tmp) {
        try {
            String cron = cronMapper.selectCronById(tmp.getCronId()).getCron();
            String tmpStr = tmp.getId();

            tmp.setUpdateTime(new Date());
            tmp.setStatus("1");
            cronJobMapper.updateCronJob(tmp);

            defaultSchedulingConfigurer.addTriggerTask(tmpStr,
                    new TriggerTask(
                            () -> {
                                System.out.println(tmpStr);
                                try {
                                    Result result = ordersService.execSql(tmp.getOrderId(), tmp.getId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            },
                            new CronTrigger(cron)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void stopSchedule(String id) {
        defaultSchedulingConfigurer.cancelTriggerTask(id);
    }


}
