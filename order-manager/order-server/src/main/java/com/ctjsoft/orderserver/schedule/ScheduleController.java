package com.ctjsoft.orderserver.schedule;

import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.domain.OrdersJobVo;
import com.ctjsoft.orderserver.order.service.ICronJobService;
import com.ctjsoft.orderserver.order.service.IOrdersService;
import com.ctjsoft.util.ReturnMessage;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ScheduledFuture;

/**
 * @author zyx
 * @date 2020/5/21
 * 定时任务执行控制器
 */

@RestController
@RequestMapping("/schedule/")
public class ScheduleController {

    @Resource
    IScheduleService scheduleService;
    @Resource
    ICronJobService cronJobService;
    @Resource
    IOrdersService ordersService;
    @Resource
    DefaultSchedulingConfigurer defaultSchedulingConfigurer;



    @GetMapping("/list")
    //@ApiOperation(value = "查询定时任务信息")
    public ReturnMessage getEffectiveFutures() throws ParseException {
        Map<String, ScheduledFuture<?>> map = defaultSchedulingConfigurer.getEffectiveFutures();

        List<JSONObject> list = new ArrayList<>();
        for (Map.Entry<String, ScheduledFuture<?>> tmp : map.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            OrdersJobVo ordersJobVo = new OrdersJobVo();
            ordersJobVo.setJobId(tmp.getKey());
            ordersJobVo = ordersService.linkSelectCronJobLogList(ordersJobVo);
            jsonObject = (JSONObject) JSONObject.toJSON(ordersJobVo);

            CronExpression cronExpression = new CronExpression(ordersJobVo.getCronText());
            cronExpression.getNextValidTimeAfter(new Date());
            jsonObject.put("nextTime", cronExpression.getNextValidTimeAfter(new Date()));
            list.add(jsonObject);
        }

        ReturnMessage returnMessage = new ReturnMessage();
        returnMessage.setData(list);
        return returnMessage;
    }

    @GetMapping("/startAllCronJobs")
    public Object startAllCronJobs() {
        return scheduleService.startAllCronJobs();
    }

    @GetMapping("/startJobsByCronJobId")
    //@ApiOperation(value = "执行启用状态的规则配置的sql")
    public Object startJobsByCronJobId(String id) {
        CronJob cronJob = cronJobService.selectCronJobById(id);
        return scheduleService.startJobsByCronJobId(cronJob);
    }


    @GetMapping("/restartJobsByCronJobId")
    //@ApiOperation(value = "重置“执行启用状态的规则”的定时任务")
    public void restartJobsByCronJobId(String id) {
        defaultSchedulingConfigurer.cancelTriggerTask(id);
        try {
            startJobsByCronJobId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @GetMapping("stopSchedule")
    //@ApiOperation(value = "手动停止定时任务")
    public void stopSchedule(String id) {
        scheduleService.stopSchedule(id);
    }

    @GetMapping("stopSchedules")
    //@ApiOperation(value = "手动停止定时任务")
    public void stopSchedules(String ids) {
        List<String> idList = Arrays.asList((ids.replace("，", ",")).split(","));
        for (String id : idList) {
            scheduleService.stopSchedule(id);
        }
    }



/*    @GetMapping("start-schedule")
    //@ApiOperation(value = "手动启动定时任务")
    public void startSchedule(String id, String cron) {
        scheduleService.startSchedule(id, cron);
    }

    @PostMapping("start-schedules")
    //@ApiOperation(value = "手动启动多个定时任务")
    public void startSchedules(@RequestBody List<Map> list) {
        for (Map<String, Object> map : list) {
            scheduleService.startSchedule((String) map.get("id"), (String) map.get("cron"));
        }
    }*/


}
