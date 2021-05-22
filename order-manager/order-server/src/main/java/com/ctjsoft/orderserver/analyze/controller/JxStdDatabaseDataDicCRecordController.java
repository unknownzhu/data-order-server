package com.ctjsoft.orderserver.analyze.controller;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicChangeRecord;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicCRecordService;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gyx
 * @date 2020-11-12
 * @desc
 */
@RequestMapping("/jxStdDatabaseDataDicCRecord")
@RestController
@Api(value = "数据源变更记录", tags = "数据源变更记录")
public class JxStdDatabaseDataDicCRecordController {

    @Autowired
    private JxStdDatabaseDataDicCRecordService stdDatabaseDataDicCRecordService;

    @PostMapping()
    @ApiOperation(value = "新增（不包含ID）/编辑（包含ID）数据源信息")
    public Result save(@RequestBody JxStdDatabaseDataDicChangeRecord stdDatabaseDataDicChangeRecord) {
            this.stdDatabaseDataDicCRecordService.save(stdDatabaseDataDicChangeRecord);
            return Result.success();
    }

    @GetMapping("search")
    @ApiOperation(value = "根据条件查询变更记录")
    public Result search(@RequestParam(value = "databaseId") String databaseId,@RequestParam(value = "keyword") String keyword) {
        Map<String,String> params = new HashMap();
        params.put("databaseId",databaseId);
        List<JxStdDatabaseDataDicChangeRecord> list = this.stdDatabaseDataDicCRecordService.search(params);
        return Result.success(list);
    }


    @GetMapping("/detail")
    @ApiOperation(value = "数据源详情")
    public ResultTable loadByDatabaseId(@RequestParam(value = "page") String page,
                                        @RequestParam(value = "limit") String limit,
                                        @RequestParam(value = "databaseId") String databaseId,
                                        @RequestParam(value = "keyword", required = false) String keyword) {
        ResultTable returnMsg = new ResultTable();

        List<JxStdDatabaseDataDicChangeRecord> list;

        try {
           // if(StringUtils.isNotEmpty(keyword)){
                list = this.stdDatabaseDataDicCRecordService.loadByDbIdAndKeyword(databaseId,keyword,page,limit);
            String count = this.stdDatabaseDataDicCRecordService.loadByDbIdAndKeywordCount(databaseId, keyword);
          //  }else{
          //      list = this.stdDatabaseDataDicCRecordService.loadByDatabaseId(databaseId);
          //  }

            returnMsg.setCount(Long.parseLong(list.size()+""));
            returnMsg.setData(0);
            returnMsg.setCode(0);
            returnMsg.setMsg("操作成功");
            // returnMsg.setLimit(limit);
            returnMsg.setData(list);
        } catch (Exception e) {
            e.printStackTrace();
            returnMsg.setMsg("操作失败");
        }

        return returnMsg;


    }
}
