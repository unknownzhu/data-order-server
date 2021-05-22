package com.ctjsoft.orderserver.analyze.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.ctjsoft.orderserver.analyze.domain.JxCompareVo;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseQuestionRecord;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseQuestionRecordService;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@RequestMapping("/jxStdDatabaseQuestionRecord")  //  jxStdDatabaseQuestionRecord/detail
@RestController
@Api(value = "数据源注册", tags = "数据源注册")
public class JxStdDatabaseQuestionRecordController {

    @Autowired
    private JxStdDatabaseQuestionRecordService stdDatabaseQuestionRecordService;


    @GetMapping("/listDistinctTableNameByDbId")
    public Object listDistinctTableNameByDbId(
            @RequestParam(value = "databaseId") String databaseId,
            @RequestParam(value = "keyword", required = false) String keyword) {
        Result returnMsg = new Result();
        List<JxStdDatabaseQuestionRecord> list = new ArrayList<>();
        try {
            list = this.stdDatabaseQuestionRecordService.listDistinctTableNameByDbId(databaseId, keyword);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setMsg("操作失败");
        }

        return list;
    }

    @GetMapping("/listDistinctColumnNameByDbIdAndTbaleName")
    public Object listDistinctColumnNameByDbIdAndTbaleName(
            @RequestParam(value = "databaseId") String databaseId,
            @RequestParam(value = "tableName", required = false) String tableName) {
        Result returnMsg = new Result();
        List<JxStdDatabaseQuestionRecord> list = new ArrayList<>();
        try {
            list = this.stdDatabaseQuestionRecordService.listDistinctColumnNameByDbIdAndTbaleName(databaseId, tableName);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setMsg("操作失败");
        }
        return list;
    }


    @GetMapping("/loadJxCompareVo")
    @ApiOperation(value = "字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "databaseId", value = "databaseId", paramType = "path")
    })
    public ResultTable loadJxCompareVo(@RequestParam(value = "page") Integer page,
                                       @RequestParam(value = "limit") Integer limit,
                                       @RequestParam(value = "databaseId") String databaseId,
                                       @RequestParam(value = "columnName", required = false) String columnName,
                                       @RequestParam(value = "tableName", required = false) String tableName,
                                       @RequestParam(value = "keyword", required = false) String keyword ,
                                       @RequestParam(value = "status", required = false,defaultValue = "0") String status ) {
        ResultTable returnMsg = new ResultTable();
        try {
            keyword = keyword == null ? "" : keyword.toUpperCase();
            List<JxCompareVo> list = stdDatabaseQuestionRecordService.loadJxCompareVoPlus(
                    databaseId, keyword, tableName, columnName, page, limit,status);
            String count = stdDatabaseQuestionRecordService.getCount(databaseId, keyword, tableName, columnName,status);

            returnMsg.setCount(Long.parseLong(Integer.parseInt(count) + ""));
            returnMsg.setCode(0);
            returnMsg.setMsg("操作成功");

            List<JSONObject> ttt = new ArrayList<>();

            for (int i = 0; i < list.size(); i++  ) {
             String rthtrte = JSONObject.toJSONString(list.get(i), SerializerFeature.WriteMapNullValue);
                rthtrte = rthtrte.replace("null", "\"\"");
                rthtrte = rthtrte.replace("\"length\"", "\"columnLength\"");
                JSONObject jsonObject = JSONObject.parseObject(rthtrte);
                ttt.add(jsonObject);

            }


            returnMsg.setData(ttt);
            
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setMsg("操作失败");
        }
        return returnMsg;
    }


    @GetMapping("/detail")
    public ResultTable loadByDatabaseId(@RequestParam(value = "page") Integer page,
                                        @RequestParam(value = "limit") Integer limit,
                                        @RequestParam(value = "databaseId") String databaseId,
                                        @RequestParam(value = "keyword", required = false) String keyword,
                                        @RequestParam(value = "columnName", required = false) String columnName,
                                        @RequestParam(value = "tableName", required = false) String tableName) {
        ResultTable returnMsg = new ResultTable();
        List<JxStdDatabaseQuestionRecord> list;
        try {
            JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord = new JxStdDatabaseQuestionRecord();
            jxStdDatabaseQuestionRecord.setDatabaseId(databaseId);
            jxStdDatabaseQuestionRecord.setKeyword(keyword);
            jxStdDatabaseQuestionRecord.setTableName(tableName);
            jxStdDatabaseQuestionRecord.setColumnName(columnName);

            list = this.stdDatabaseQuestionRecordService.loadByDbIdAndKeyword(jxStdDatabaseQuestionRecord);
            String count = stdDatabaseQuestionRecordService.loadByDbIdAndKeywordCount(jxStdDatabaseQuestionRecord);

            returnMsg.setCount(Long.parseLong(Integer.parseInt(count) + ""));
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


    @GetMapping("/solve/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "path")
    })
    public Result solveById(@PathVariable(value = "id") String id) {
        this.stdDatabaseQuestionRecordService.solveById(id);
        return Result.success();
    }
}
