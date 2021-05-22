package com.ctjsoft.orderserver.order.controller;

import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.domain.OrderSqlLog;
import com.ctjsoft.orderserver.order.service.IOrderSqlLogService;
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

/**
 * 语句执行日志Controller
 * 
 * @author zzz
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/system/orderSqlLog")
public class OrderSqlLogController extends BaseController
{
    private String prefix = "system/orderSqlLog";

    @Autowired
    private IOrderSqlLogService orderSqlLogService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/main','system:orderSqlLog:main')")
    public ModelAndView main()
    {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询语句执行日志列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/data','system:orderSqlLog:data')")
    public ResultTable list(@ModelAttribute OrderSqlLog orderSqlLog, PageDomain pageDomain)
    {
        PageInfo<OrderSqlLog> pageInfo = orderSqlLogService.selectOrderSqlLogPage(orderSqlLog,pageDomain);
        return pageTable(pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * 查询语句执行日志列表
     */
    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/data','system:orderSqlLog:data')")
    public Result list(@ModelAttribute OrderSqlLog orderSqlLog )
    {
        return Result.success(orderSqlLogService.selectOrderSqlLogList(orderSqlLog));
    }

    /**
     * 新增语句执行日志
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/add','system:orderSqlLog:add')")
    public ModelAndView add()
    {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存语句执行日志
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/add','system:orderSqlLog:add')")
    public Result save(@RequestBody OrderSqlLog orderSqlLog)
    {
        return decide(orderSqlLogService.insertOrderSqlLog(orderSqlLog));
    }

    /**
     * 修改语句执行日志
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        OrderSqlLog cron = orderSqlLogService.selectOrderSqlLogById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cron);
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改保存语句执行日志
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/edit','system:orderSqlLog:edit')")
    public Result update(@RequestBody OrderSqlLog orderSqlLog)
    {
        return decide(orderSqlLogService.updateOrderSqlLog(orderSqlLog));
    }

    /**
     * 删除语句执行日志
     */
    @ResponseBody
    @DeleteMapping( "/batchRemove")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/remove','system:orderSqlLog:remove')")
    public Result batchRemove(String ids)
    {
        return decide(orderSqlLogService.deleteOrderSqlLogByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/orderSqlLog/remove','system:orderSqlLog:remove')")
    public Result remove(@PathVariable("id") String id)
    {
        return decide(orderSqlLogService.deleteOrderSqlLogById(id));
    }
}
