package com.ctjsoft.orderserver.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.order.domain.CommonSql;
import com.ctjsoft.orderserver.order.domain.CommonSqlVo;
import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.domain.HistorySql;
import com.ctjsoft.orderserver.order.service.ICommonSqlService;
import com.ctjsoft.orderserver.order.service.IHistorySqlService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.Map;

import static org.datagear.web.controller.AbstractController.CONTENT_TYPE_JSON;

/**
 * 常用语句Controller
 *
 * @author zzz
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/system/commonSql")
public class CommonSqlController extends BaseController {
    private String prefix = "system/commonSql";

    @Autowired
    private ICommonSqlService commonSqlService;
    @Autowired
    private IHistorySqlService historySqlService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/commonSql/main','system:commonSql:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询常用语句列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/commonSql/data','system:commonSql:data')")
    public ResultTable list(@ModelAttribute CommonSql commonSql, PageDomain pageDomain) {
        PageInfo<CommonSql> pageInfo = commonSqlService.selectCommonSqlPage(commonSql, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    @ResponseBody
    @GetMapping("/linkData")
    //@PreAuthorize("hasPermission('/system/commonSql/data','system:commonSql:data')")
    public ResultTable linkData(@ModelAttribute CommonSql commonSql, PageDomain pageDomain/*, @RequestBody PageDomain pageDomain1*/) {
        /*if (pageDomain.getPage() == null || pageDomain.getLimit() == null) {
            BeanUtils.copyProperties(pageDomain1, pageDomain);
        }*/
        PageInfo<CommonSqlVo> pageInfo = commonSqlService.selectCommonVoSqlPage(commonSql, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

/*    @ResponseBody
    @PostMapping("/linkDataForSqlPad")*//*


    @RequestMapping(value = "/linkDataForSqlPad", produces = CONTENT_TYPE_JSON)
    @ResponseBody
    //@PreAuthorize("hasPermission('/system/commonSql/data','system:commonSql:data')")
    public ResultTable linkDataForSqlPad(@RequestBody PageDomain pageDomain1 */
/*, @RequestBody PageDomain pageDomain1*//*
) {
        */
/*if (pageDomain.getPage() == null || pageDomain.getLimit() == null) {
            BeanUtils.copyProperties(pageDomain1, pageDomain);
        }*//*


        PageDomain pageDomain = new PageDomain();
   */
/*     pageDomain.setPage(Integer.parseInt(map.get("page")));
        pageDomain.setPage(map.get("li"));*//*


        CommonSql commonSql = new CommonSql();
        PageInfo<CommonSqlVo> pageInfo = commonSqlService.selectCommonVoSqlPage(commonSql, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }
*/


    /**
     * 新增常用语句
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/commonSql/add','system:commonSql:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存常用语句
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/commonSql/add','system:commonSql:add')")
    public Result save(@RequestBody CommonSql commonSql) {
        commonSql.setId(StringUtils.getUUid());
        commonSql.setCreatorId(WebUtils.getUser().getId());
        commonSql.setCreateTime(new Date());
        return decide(commonSqlService.insertCommonSql(commonSql));
    }


    /**
     * 新增保存常用语句
     */
    @ResponseBody
    @PostMapping("/insertFromHistorySql")
    //@PreAuthorize("hasPermission('/system/commonSql/add','system:commonSql:add')")
    public Result insertFromHistorySql(@RequestParam(value = "historySqlId") String historySqlId, @RequestParam(value = "showType") String showType) {
        HistorySql historySql = historySqlService.selectHistorySqlById(historySqlId);
        CommonSql commonSql = new CommonSql();
        commonSql.setSource(historySqlId);
        commonSql.setCreatorId(WebUtils.getUser().getId());
        if (commonSqlService.selectCommonSqlList(commonSql).size() > 0) {
            return Result.failure("当前用户已收藏该记录");
        }
        BeanUtils.copyProperties(historySql, commonSql);
        commonSql.setSource(historySqlId);
        commonSql.setCreateTime(new Date());
        commonSql.setId(StringUtils.getUUid());
        commonSql.setShowType(showType);
        commonSql.setSource(historySqlId);
        return decide(commonSqlService.insertCommonSql(commonSql));
    }


    /**
     * 修改常用语句
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        CommonSql cron = commonSqlService.selectCommonSqlById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(cron);
        result.setSuccess(true);
        return result;
    }

    /**
     * 修改保存常用语句
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/commonSql/edit','system:commonSql:edit')")
    public Result update(@RequestBody CommonSql commonSql) {
        return decide(commonSqlService.updateCommonSql(commonSql));
    }

    /**
     * 删除常用语句
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/commonSql/remove','system:commonSql:remove')")
    public Result batchRemove(String ids) {
        return decide(commonSqlService.deleteCommonSqlByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/commonSql/remove','system:commonSql:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(commonSqlService.deleteCommonSqlById(id));
    }
}
