package com.ctjsoft.orderserver.analyze.controller;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionary;
import com.ctjsoft.orderserver.analyze.domain.StdTable;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicionaryService;
import com.ctjsoft.orderserver.order.mapper.SchemaMapper;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.ExcelUtil;
import com.ctjsoft.orderserver.utils.ExportVo;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.datagear.management.domain.Schema;
import org.datagear.management.domain.User;
import org.datagear.management.service.SchemaService;
import org.datagear.web.controller.SchemaController;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@RequestMapping("/jxStdDatabaseDataDicionary")
@RestController
@Api(value = "数据源注册", tags = "数据源注册")
public class JxStdDatabaseDataDicionaryController extends BaseController {

    @Autowired
    private JxStdDatabaseDataDicionaryService stdDatabaseDataDicionaryService;

    @Autowired
    private SchemaService getSchemaService;

    @Autowired
    private SchemaMapper schemaMapper;

    @GetMapping("/detail")
    public ResultTable loadByDatabaseId(@RequestParam(value = "page") String page,
                                        @RequestParam(value = "limit") String limit,
                                        @RequestParam(value = "databaseId") String databaseId,
                                        @RequestParam(value = "keyword", required = false) String keyword) {

        keyword = keyword == null ? "" : keyword.toUpperCase();

        ResultTable returnMsg = new ResultTable();
        List<JxStdDatabaseDataDicionary> list = new ArrayList<>();

        try {

            list = this.stdDatabaseDataDicionaryService.loadByDbIdAndKeywordPage(databaseId, keyword, page, limit);
            String count = this.stdDatabaseDataDicionaryService.loadByDbIdAndKeywordCount(databaseId, keyword);
            returnMsg.setCount(Long.parseLong(count));
            returnMsg.setData(0);
            returnMsg.setCode(0);
            returnMsg.setMsg("操作成功");
            returnMsg.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setMsg("操作失败");
        }

        return returnMsg;
    }

    @GetMapping("/export/{databaseId}")
    public void export(HttpServletRequest request, HttpServletResponse response, @PathVariable(value = "databaseId") String databaseId) throws Exception {

        List<JxStdDatabaseDataDicionary> list = new ArrayList<>();
        List<ExportVo> resultList = new ArrayList<>();
        list = this.stdDatabaseDataDicionaryService.loadByDbIdAndKeyword(databaseId, null);


        User user = WebUtils.getUser(request, response);

        Schema schema = this.getSchemaService.getById(user, databaseId);


        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);

        String fileName = schema.getTitle() + "数据字典导出" + dateString;


        for (int i = 0; i < list.size(); i++) {
            JxStdDatabaseDataDicionary tmp =   list.get(i);
            if (tmp.getTableRowSize() == null || tmp.getTableRowSize().equals("null")) {
                tmp.setTableRowSize("未获取");
            }
            if (tmp.getTableRowChangeTime() == null || tmp.getTableRowChangeTime().equals("null")) {
                tmp.setTableRowChangeTime("未获取");
            }
            if (tmp.getDetectionResult() != null && tmp.getDetectionResult().equals("1")) {
                tmp.setDetectionResult("已变更");
            } else if (tmp.getDetectionResult() != null && tmp.getDetectionResult().equals("0")) {
                tmp.setDetectionResult("未变更");
            }
            ExportVo exportVo = new ExportVo();
            BeanUtils.copyProperties(tmp, exportVo);
            exportVo.setNumber(""+(i+1));
            resultList.add(exportVo);
        }

        ExcelUtil.writeExcel(response, resultList, fileName, "Sheet1", ExportVo.class);

    }

    @GetMapping({"/reScan/{databaseId}"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "databaseId", paramType = "path")
    })
    public Result reScan( HttpServletRequest request, HttpServletResponse response,@PathVariable(value = "databaseId") String databaseId
            , @RequestParam(value = "url", required = false) String url) {

        Map<String, String> map = new HashMap<>();
        map.put("msg", "扫描通过");
        map.put("status", "1");


        try {
            this.schemaMapper.updateDbStatus(databaseId,"0");
            stdDatabaseDataDicionaryService.reScan(databaseId);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "扫描错误");
            map.put("errofInfo", e.getMessage());
            map.put("status", "2");
            this.schemaMapper.updateDbStatus(databaseId,"1");
        } finally {
            this.schemaMapper.updateDbStatus(databaseId,"1");
        }
        return Result.success(map);
    }

    @PostMapping("save")
    public Result save(@RequestBody List<JxStdDatabaseDataDicionary> list) {
        try {
            this.stdDatabaseDataDicionaryService.save(list);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

/*    @PostMapping("selectCzbTable")
    public Result selectCzbTable() {
        try {
            List<StdTable> list = this.stdDatabaseDataDicionaryService.getCzbTable();
            return Result.success(list);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }*/
}
