package com.ctjsoft.orderserver.order.controller;


import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.domain.CronJobLog;
import com.ctjsoft.orderserver.order.service.ICronJobLogService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 定时任务日志Controller
 *
 * @author zzz
 * @date 2021-03-22
 */
@RestController
@RequestMapping("/system/log")
public class CronJobLogController extends BaseController {
    private String prefix = "system/log";

    @Autowired
    private ICronJobLogService cronJobLogService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/log/main','system:log:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询定时任务日志列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/log/data','system:log:data')")
    public ResultTable data(@ModelAttribute CronJobLog cronJobLog, PageDomain pageDomain) {
        PageInfo<CronJobLog> pageInfo = cronJobLogService.selectCronJobLogPage(cronJobLog, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/log/data','system:log:data')")
    public Result list(@ModelAttribute CronJobLog cronJobLog) {
        List<CronJobLog> list = cronJobLogService.selectCronJobLogList(cronJobLog);
        return Result.success(list);
    }

    /**
     * 新增定时任务日志
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/log/add','system:log:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存定时任务日志
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/log/add','system:log:add')")
    public Result save(@RequestBody CronJobLog cronJobLog) {
        return decide(cronJobLogService.insertCronJobLog(cronJobLog));
    }

    /**
     * 修改定时任务日志
     */
/*    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/log/edit','system:log:edit')")
    public ModelAndView edit(String id, ModelMap mmap)
    {
        CronJobLog cronJobLog = cronJobLogService.selectCronJobLogById(id);
        mmap.put("cronJobLog", cronJobLog);
        return JumpPage(prefix + "/edit");
    }*/
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        CronJobLog cronJobLog = cronJobLogService.selectCronJobLogById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cronJobLog);
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改保存定时任务日志
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/log/edit','system:log:edit')")
    public Result update(@RequestBody CronJobLog cronJobLog) {
        return decide(cronJobLogService.updateCronJobLog(cronJobLog));
    }

    /**
     * 删除定时任务日志
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/log/remove','system:log:remove')")
    public Result batchRemove(String ids) {
        return decide(cronJobLogService.deleteCronJobLogByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/log/remove','system:log:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(cronJobLogService.deleteCronJobLogById(id));
    }
}
