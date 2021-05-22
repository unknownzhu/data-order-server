package com.ctjsoft.orderserver.order.controller;


import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.order.domain.CommonSqlType;
import com.ctjsoft.orderserver.order.domain.Drivers;
import com.ctjsoft.orderserver.order.domain.HistorySql;
import com.ctjsoft.orderserver.order.domain.HistorySqlVo;
import com.ctjsoft.orderserver.order.service.ICommonSqlTypeService;
import com.ctjsoft.orderserver.order.service.IDriversService;
import com.ctjsoft.orderserver.order.service.IHistorySqlService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.datagear.connection.DriverEntityManager;
import org.datagear.management.domain.Schema;
import org.datagear.management.service.SchemaService;
import org.datagear.web.sqlpad.SqlpadExecutionService;
import org.datagear.web.util.OperationMessage;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Driver;
import java.util.Date;
import java.util.List;

/**
 * 常用语句Controller
 *
 * @author zzz
 * @date 2021-03-24
 */
@RestController
@RequestMapping("/system/historySql")
public class HistorySqlController extends BaseController {
    private String prefix = "system/historySql";

    @Autowired
    private IHistorySqlService historySqlService;

    @Autowired
    private ICommonSqlTypeService commonSqlTypeService;

    @Autowired
    private SchemaService schemaService;

    @Autowired
    private IDriversService driversService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/historySql/main','system:historySql:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询常用语句列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/historySql/data','system:historySql:data')")
    public ResultTable list(@ModelAttribute HistorySql historySql, PageDomain pageDomain) {
        PageInfo<HistorySql> pageInfo = historySqlService.selectHistorySqlPage(historySql, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    @ResponseBody
    @GetMapping("/linkData")
    //@PreAuthorize("hasPermission('/system/historySql/data','system:historySql:data')")
    public ResultTable linkData(@ModelAttribute HistorySql historySql, PageDomain pageDomain) {
        PageInfo<HistorySqlVo> pageInfo = historySqlService.selectCommonVoSqlPage(historySql, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 新增常用语句
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/historySql/add','system:historySql:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存常用语句
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/historySql/add','system:historySql:add')")
    public Result save(@RequestBody HistorySql historySql) {
        historySql.setId(StringUtils.getUUid());
        historySql.setCreatorId(WebUtils.getUser().getId());
        historySql.setCreateTime(new Date());
        return decide(historySqlService.insertHistorySql(historySql));
    }

    @PostMapping(value = "/{schemaId}/execute")
    @ResponseBody
    public Result executeSql(HttpServletRequest request, HttpServletResponse response,
                                                       org.springframework.ui.Model springModel, @PathVariable("schemaId") String schemaId,
                                                       @RequestParam("sqlpadId") String sqlpadId, @RequestParam("sql") String sql,
                                                       @RequestParam(value = "sqlDelimiter", required = false) String sqlDelimiter,
                                                       @RequestParam(value = "sqlStartRow", required = false) Integer sqlStartRow,
                                                       @RequestParam(value = "sqlStartColumn", required = false) Integer sqlStartColumn,
                                                       @RequestParam(value = "commitMode", required = false) SqlpadExecutionService.CommitMode commitMode,
                                                       @RequestParam(value = "exceptionHandleMode", required = false) SqlpadExecutionService.ExceptionHandleMode exceptionHandleMode,
                                                       @RequestParam(value = "overTimeThreashold", required = false) Integer overTimeThreashold,
                                                       @RequestParam(value = "resultsetFetchSize", required = false) Integer resultsetFetchSize) throws Throwable
    {
        HistorySql historySql = new HistorySql();
        historySql.setId(StringUtils.getUUid());
        historySql.setCreatorId(WebUtils.getUser().getId());
        historySql.setCreateTime(new Date());
        historySql.setShowType("2");
        historySql.setName("查询历史");
        historySql.setDbId(schemaId);
        historySql.setSqls(sql);
        CommonSqlType commonSqlType = new CommonSqlType();
        commonSqlType.setIsNormal("1");

        String url = schemaService.getById(schemaId).getUrl();
        String className = url.substring(url.indexOf(":") + 1, url.indexOf(":", url.indexOf(":") + 1));

        Drivers drivers = new Drivers();
        drivers.setDriverclassnames(className);
        List<Drivers> driversList = driversService.selectDriversList(drivers);
        String dbTypeId = "";
        if (driversList.size() > 0) {
            dbTypeId = driversList.get(0).getId();
        }
        historySql.setDbType(dbTypeId);
        List<CommonSqlType> types = commonSqlTypeService.selectCommonSqlTypeList(commonSqlType);
        String typeId = "";
        if (types.size() > 0) {
            historySql.setTypeId(types.get(0).getId());
        }
        return decide(historySqlService.insertHistorySql(historySql));


    }


    /**
     * 修改常用语句
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        HistorySql cron = historySqlService.selectHistorySqlById(id);
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
    //@PreAuthorize("hasPermission('/system/historySql/edit','system:historySql:edit')")
    public Result update(@RequestBody HistorySql historySql) {
        return decide(historySqlService.updateHistorySql(historySql));
    }

    /**
     * 删除常用语句
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/historySql/remove','system:historySql:remove')")
    public Result batchRemove(String ids) {
        return decide(historySqlService.deleteHistorySqlByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/historySql/remove','system:historySql:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(historySqlService.deleteHistorySqlById(id));
    }
}
