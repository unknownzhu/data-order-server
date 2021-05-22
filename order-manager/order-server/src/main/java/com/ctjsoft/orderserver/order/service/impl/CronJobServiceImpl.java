package com.ctjsoft.orderserver.order.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.order.controller.OrdersController;
import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.domain.CronJobVo;
import com.ctjsoft.orderserver.order.domain.OrdersJobVo;
import com.ctjsoft.orderserver.order.mapper.CronJobMapper;
import com.ctjsoft.orderserver.order.mapper.CronMapper;
import com.ctjsoft.orderserver.order.service.ICronJobService;
import com.ctjsoft.orderserver.schedule.DefaultSchedulingConfigurer;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.TriggerTask;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务Service业务层处理
 *
 * @author zzz
 * @date 2021-03-22
 */
@Service
public class CronJobServiceImpl implements ICronJobService {
    @Autowired
    private CronJobMapper cronJobMapper;
    @Autowired
    private CronMapper cronMapper;

    @Resource
    OrdersController ordersService;
    @Resource
    DefaultSchedulingConfigurer defaultSchedulingConfigurer;

    /**
     * 查询定时任务
     *
     * @param id 定时任务ID
     * @return 定时任务
     */
    @Override
    public CronJob selectCronJobById(String id) {
        return cronJobMapper.selectCronJobById(id);
    }

    /**
     * 查询定时任务列表
     *
     * @param cronJob 定时任务
     * @return 定时任务
     */
    @Override
    public List<CronJob> selectCronJobList(CronJob cronJob) {
        return cronJobMapper.selectCronJobList(cronJob);
    }


    @Override
    public Result control(String id, String type) {
        if (type.equals("0")) { //停用
            defaultSchedulingConfigurer.cancelTriggerTask(id);
            CronJob cronJob = new CronJob();
            cronJob.setId(id);
            cronJob.setUpdateTime(new Date());
            cronJob.setStatus("0");
            cronJobMapper.updateCronJob(cronJob);
        } else if (type.equals("1")) { //启用
            CronJob cronJob = cronJobMapper.selectCronJobById(id);
            try {
                String cron = cronMapper.selectCronById(cronJob.getCronId()).getCron();
                String tmpStr = cronJob.getId();
                defaultSchedulingConfigurer.addTriggerTask(tmpStr,
                        new TriggerTask(
                                () -> {
                                    System.out.println(tmpStr);
                                    try {
                                        Result result = ordersService.execSql(cronJob.getOrderId(), cronJob.getId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                },
                                new CronTrigger(cron)));
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failure("操作失败");
            }
            cronJob.setUpdateTime(new Date());
            cronJob.setStatus("1");
            cronJobMapper.updateCronJob(cronJob);
        }
        return Result.success("操作成功");

        //    return cronJobMapper.selectCronJobList(cronJob);
    }


    /**
     * 查询定时任务
     *
     * @param cronJob    定时任务
     * @param pageDomain
     * @return 定时任务 分页集合
     */
    @Override
    public PageInfo<CronJob> selectCronJobPage(CronJob cronJob, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<CronJob> data = cronJobMapper.selectCronJobList(cronJob);
        return new PageInfo<>(data);
    }

    @Override
    public PageInfo<CronJobVo> linkPageData(CronJobVo cronJobVoTmp, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<CronJobVo> data = cronJobMapper.linkPageData(cronJobVoTmp);
        Map<String, ScheduledFuture<?>> map = defaultSchedulingConfigurer.getEffectiveFutures();

        JSONObject jsonObject = new JSONObject();
        for (CronJobVo cronJobVo : data) {
            Boolean flag = false;
            for (Map.Entry<String, ScheduledFuture<?>> tmp : map.entrySet()) {
                if (cronJobVo.getId().equals(tmp.getKey())) {
                    flag = true;
                    break;
                }
               /* OrdersJobVo ordersJobVo = new OrdersJobVo();
                ordersJobVo.setJobId(tmp.getKey());
                ordersJobVo = ordersService.linkSelectCronJobLogList(ordersJobVo);
                jsonObject = (JSONObject) JSONObject.toJSON(ordersJobVo);

                CronExpression cronExpression = new CronExpression(ordersJobVo.getCronText());
                cronExpression.getNextValidTimeAfter(new Date());
                jsonObject.put("nextTime", cronExpression.getNextValidTimeAfter(new Date()));
*/
            }
            if (flag) {
                cronJobVo.setCronStatus("1");
            } else {
                cronJobVo.setCronStatus("0");
            }

        }


        return new PageInfo<>(data);
    }

    /**
     * 新增定时任务
     *
     * @param cronJob 定时任务
     * @return 结果
     */

    @Override
    public int insertCronJob(CronJob cronJob) {
        return cronJobMapper.insertCronJob(cronJob);
    }

    /**
     * 修改定时任务
     *
     * @param cronJob 定时任务
     * @return 结果
     */
    @Override
    public int updateCronJob(CronJob cronJob) {
        return cronJobMapper.updateCronJob(cronJob);
    }

    /**
     * 删除定时任务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCronJobByIds(String[] ids) {
        return cronJobMapper.deleteCronJobByIds(ids);
    }

    /**
     * 删除定时任务信息
     *
     * @param id 定时任务ID
     * @return 结果
     */
    @Override
    public int deleteCronJobById(String id) {
        return cronJobMapper.deleteCronJobById(id);
    }
}
