package com.ctjsoft.orderserver.order.controller;

import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.domain.Orders;
import com.ctjsoft.orderserver.order.service.ICronService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

/**
 * 定时任务Controller
 * 
 * @author zzz
 * @date 2021-03-22
 */
@RestController
@RequestMapping("/system/cron")
public class CronController extends BaseController
{
    private String prefix = "system/cron";


    @Autowired
    private ICronService cronService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/cron/main','system:cron:main')")
    public ModelAndView main()
    {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询定时任务列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/cron/data','system:cron:data')")
    public ResultTable data(@ModelAttribute Cron cron, PageDomain pageDomain)
    {
        PageInfo<Cron> pageInfo = cronService.selectCronPage(cron,pageDomain);
        return pageTable(pageInfo.getList(),pageInfo.getTotal());
    }

    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/cron/data','system:cron:data')")
    public Result list(@ModelAttribute Cron cron )
    {
        List<Cron> list = cronService.selectCronList(cron);
        return Result.success(list);
    }

    /**
     * 新增定时任务
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/cron/add','system:cron:add')")
    public ModelAndView add()
    {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存定时任务
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/cron/add','system:cron:add')")
    public Result save(@RequestBody Cron cron)
    {
        cron.setId(StringUtils.getUUid());
        cron.setCreatorId(WebUtils.getUser().getId());
        cron.setCreateTime(new Date());
        return decide(cronService.insertCron(cron));
    }

/*
    */
/**
     * 修改定时任务
     *//*

    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/cron/edit','system:cron:edit')")
    public ModelAndView edit(String id, ModelMap mmap)
    {
        Cron cron = cronService.selectCronById(id);
        mmap.put("cron", cron);
        return JumpPage(prefix + "/edit");
    }
*/



    /**
     * 修改工单管理
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        Cron cron = cronService.selectCronById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cron);
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改保存定时任务
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/cron/edit','system:cron:edit')")
    public Result update(@RequestBody Cron cron)
    {
        return decide(cronService.updateCron(cron));
    }

    /**
     * 删除定时任务
     */
    @ResponseBody
    @DeleteMapping( "/batchRemove")
    //@PreAuthorize("hasPermission('/system/cron/remove','system:cron:remove')")
    public Result batchRemove(String ids)
    {
        return decide(cronService.deleteCronByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/cron/remove','system:cron:remove')")
    public Result remove(@PathVariable("id") String id)
    {
        return decide(cronService.deleteCronById(id));
    }
}
