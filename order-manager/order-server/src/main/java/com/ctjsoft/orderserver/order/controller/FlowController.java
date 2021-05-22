package com.ctjsoft.orderserver.order.controller;

import com.ctjsoft.orderserver.order.domain.Flow;
import com.ctjsoft.orderserver.order.domain.Orders;
import com.ctjsoft.orderserver.order.service.IFlowService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.StringUtils;
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
 * 流程管理Controller
 * 
 * @author zzz
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/system/flow")
public class FlowController extends BaseController
{
    private String prefix = "system/flow";

    @Autowired
    private IFlowService flowService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/flow/main','system:flow:main')")
    public ModelAndView main()
    {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/flow/data','system:flow:data')")
    public ResultTable data(@ModelAttribute Flow flow, PageDomain pageDomain)
    {
        PageInfo<Flow> pageInfo = flowService.selectFlowPage(flow,pageDomain);
        return pageTable(pageInfo.getList(),pageInfo.getTotal());
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/flow/data','system:flow:data')")
    public Result list(@ModelAttribute Flow flow )
    {
        Result result = new Result();
        List<Flow> list = flowService.selectFlowList(flow);
        result.setSuccess(true);
        result.setCode(200);
        result.setData(list);
        return result;
    }


    /**
     * 新增流程管理
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/flow/add','system:flow:add')")
    public ModelAndView add()
    {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存流程管理
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/flow/add','system:flow:add')")
    public Result save(@RequestBody Flow flow)
    {
        flow.setId(StringUtils.getUUid());
        return decide(flowService.insertFlow(flow));
    }

    /**
     * 修改流程管理
     */
/*    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/flow/edit','system:flow:edit')")
    public ModelAndView edit(String id, ModelMap mmap)
    {
        Flow flow = flowService.selectFlowById(id);
        mmap.put("flow", flow);
        return JumpPage(prefix + "/edit");
    }*/
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        Flow flow = flowService.selectFlowById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(flow);
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改保存流程管理
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/flow/edit','system:flow:edit')")
    public Result update(@RequestBody Flow flow)
    {
        return decide(flowService.updateFlow(flow));
    }

    /**
     * 删除流程管理
     */
    @ResponseBody
    @DeleteMapping( "/batchRemove")
    //@PreAuthorize("hasPermission('/system/flow/remove','system:flow:remove')")
    public Result batchRemove(String ids)
    {
        return decide(flowService.deleteFlowByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/flow/remove','system:flow:remove')")
    public Result remove(@PathVariable("id") String id)
    {
        return decide(flowService.deleteFlowById(id));
    }
}
