package com.ctjsoft.orderserver.order.controller;

import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.domain.OrderReviewLog;
import com.ctjsoft.orderserver.order.service.IOrderReviewLogService;
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
 * 送审日志Controller
 * 
 * @author zzz
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/system/orderReviewLog")
public class OrderReviewLogController extends BaseController
{
    private String prefix = "system/orderReviewLog";

    @Autowired
    private IOrderReviewLogService orderReviewLogService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/main','system:orderReviewLog:main')")
    public ModelAndView main()
    {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询送审日志列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/data','system:orderReviewLog:data')")
    public ResultTable list(@ModelAttribute OrderReviewLog orderReviewLog, PageDomain pageDomain)
    {
        PageInfo<OrderReviewLog> pageInfo = orderReviewLogService.selectOrderReviewLogPage(orderReviewLog,pageDomain);
        return pageTable(pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * 新增送审日志
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/add','system:orderReviewLog:add')")
    public ModelAndView add()
    {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存送审日志
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/add','system:orderReviewLog:add')")
    public Result save(@RequestBody OrderReviewLog orderReviewLog)
    {
        orderReviewLog.setId(StringUtils.getUUid());
        orderReviewLog.setCreatorId(WebUtils.getUser().getId());
        orderReviewLog.setCreateTime(new Date());
        return decide(orderReviewLogService.insertOrderReviewLog(orderReviewLog));
    }

    /**
     * 修改送审日志
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        OrderReviewLog cron = orderReviewLogService.selectOrderReviewLogById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cron);
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改保存送审日志
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/edit','system:orderReviewLog:edit')")
    public Result update(@RequestBody OrderReviewLog orderReviewLog)
    {
        return decide(orderReviewLogService.updateOrderReviewLog(orderReviewLog));
    }

    /**
     * 删除送审日志
     */
    @ResponseBody
    @DeleteMapping( "/batchRemove")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/remove','system:orderReviewLog:remove')")
    public Result batchRemove(String ids)
    {
        return decide(orderReviewLogService.deleteOrderReviewLogByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/orderReviewLog/remove','system:orderReviewLog:remove')")
    public Result remove(@PathVariable("id") String id)
    {
        return decide(orderReviewLogService.deleteOrderReviewLogById(id));
    }
}
