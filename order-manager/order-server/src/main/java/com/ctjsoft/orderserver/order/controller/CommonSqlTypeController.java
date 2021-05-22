package com.ctjsoft.orderserver.order.controller;

import com.ctjsoft.orderserver.order.domain.CommonSqlType;
import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.domain.Drivers;
import com.ctjsoft.orderserver.order.service.ICommonSqlTypeService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.ctjsoft.orderserver.utils.response.Result;

import java.util.Date;
import java.util.List;

/**
 * 语句类型Controller
 *
 * @author zzz
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/system/commonSqlType")
public class CommonSqlTypeController extends BaseController {
    private String prefix = "system/commonSqlType";

    @Autowired
    private ICommonSqlTypeService commonSqlTypeService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/commonSqlType/main','system:commonSqlType:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询语句类型列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/commonSqlType/data','system:commonSqlType:data')")
    public ResultTable list(@ModelAttribute CommonSqlType commonSqlType, PageDomain pageDomain) {
        PageInfo<CommonSqlType> pageInfo = commonSqlTypeService.selectCommonSqlTypePage(commonSqlType, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 新增语句类型
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/commonSqlType/add','system:commonSqlType:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }


    /**
     * 新增语句类型
     */
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/commonSqlType/add','system:commonSqlType:add')")
    public Result list(@ModelAttribute CommonSqlType CommonSqlType) {
        Result result = new Result();
        List<CommonSqlType> list = commonSqlTypeService.selectCommonSqlTypeList(CommonSqlType);
        result.setSuccess(true);
        result.setCode(200);
        result.setData(list);
        return result;
    }


    /**
     * 新增保存语句类型
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/commonSqlType/add','system:commonSqlType:add')")
    public Result save(@RequestBody CommonSqlType commonSqlType) {
        commonSqlType.setId(StringUtils.getUUid());
        commonSqlType.setCreateTime(new Date());
        return decide(commonSqlTypeService.insertCommonSqlType(commonSqlType));
    }

    /**
     * 修改语句类型
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        CommonSqlType cron = commonSqlTypeService.selectCommonSqlTypeById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cron);
        result.setSuccess(true);
        return result;
    }


    /**
     * 修改保存语句类型
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/commonSqlType/edit','system:commonSqlType:edit')")
    public Result update(@RequestBody CommonSqlType commonSqlType) {
        return decide(commonSqlTypeService.updateCommonSqlType(commonSqlType));
    }

    /**
     * 删除语句类型
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/commonSqlType/remove','system:commonSqlType:remove')")
    public Result batchRemove(String ids) {
        return decide(commonSqlTypeService.deleteCommonSqlTypeByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/commonSqlType/remove','system:commonSqlType:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(commonSqlTypeService.deleteCommonSqlTypeById(id));
    }
}
