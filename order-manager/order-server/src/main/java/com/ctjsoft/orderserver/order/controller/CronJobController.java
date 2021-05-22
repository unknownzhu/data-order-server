package com.ctjsoft.orderserver.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.order.domain.*;
import com.ctjsoft.orderserver.order.service.ICronJobService;
import com.ctjsoft.orderserver.schedule.DefaultSchedulingConfigurer;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * 定时任务Controller
 *
 * @author zzz
 * @date 2021-03-22
 */
@RestController
@RequestMapping("/system/job")
public class CronJobController extends BaseController {
    private String prefix = "system/job";

    @Autowired
    private ICronJobService cronJobService;


    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/job/main','system:job:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询定时任务列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/job/data','system:job:data')")
    public ResultTable data(@ModelAttribute CronJob cronJob, PageDomain pageDomain) {
        PageInfo<CronJob> pageInfo = cronJobService.selectCronJobPage(cronJob, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 查询定时任务列表
     */
    @ResponseBody
    @GetMapping("/linkdata")
//@PreAuthorize("hasPermission('/system/job/data','system:job:data')")
    public ResultTable linkPageData(@ModelAttribute CronJobVo cronJobVo, PageDomain pageDomain) {
        PageInfo<CronJobVo> pageInfo = cronJobService.linkPageData(cronJobVo, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }


    @ResponseBody
    @GetMapping("/control")
    //@PreAuthorize("hasPermission('/system/log/data','system:log:data')")
    public Result control(@RequestParam String id, @RequestParam String type) {
        return cronJobService.control(id, type);
    }

    @ResponseBody
    @GetMapping("/batchControl")
    //@PreAuthorize("hasPermission('/system/log/data','system:log:data')")
    public Result batchControl(@RequestParam String ids, @RequestParam String type) {
        List<String> idList = Arrays.asList(ids.split(","));
        for (String id : idList) {
            try {
                cronJobService.control(id, type);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failure("操作失败", e.getMessage());
            }

        }
        return Result.success("操作成功");
    }


    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/log/data','system:log:data')")
    public Result list(@ModelAttribute CronJob cronJob) {
        List<CronJob> list = cronJobService.selectCronJobList(cronJob);
        return Result.success(list);
    }

    /**
     * 新增定时任务
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/job/add','system:job:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存定时任务
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/job/add','system:job:add')")
    public Result save(@RequestBody CronJob cronJob) {
        return decide(cronJobService.insertCronJob(cronJob));
    }

    /**
     * 修改定时任务
     */
/*
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/job/edit','system:job:edit')")
    public ModelAndView edit(String id, ModelMap mmap)
    {
        CronJob cronJob = cronJobService.selectCronJobById(id);
        mmap.put("cronJob", cronJob);
        return JumpPage(prefix + "/edit");
    }
*/


    /**
     * 修改工单管理
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        CronJob cronJob = cronJobService.selectCronJobById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cronJob);
        result.setSuccess(true);
        return result;
    }


    /**
     * 修改保存定时任务
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/job/edit','system:job:edit')")
    public Result update(@RequestBody CronJob cronJob) {
        return decide(cronJobService.updateCronJob(cronJob));
    }

    /**
     * 删除定时任务
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/job/remove','system:job:remove')")
    public Result batchRemove(String ids) {
        return decide(cronJobService.deleteCronJobByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/job/remove','system:job:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(cronJobService.deleteCronJobById(id));
    }
}
