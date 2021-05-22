package com.ctjsoft.orderserver.analyze.controller;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionaryColumn;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicColumService;
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
@RequestMapping("/jxStdDatabaseDataDicColum")
@RestController
@Api(value = "字段", tags = "字段")
public class JxStdDatabaseDataDicColumController {

    @Autowired
    private JxStdDatabaseDataDicColumService jxStdDatabaseDataDicColumService;

    @GetMapping("/loadByTableId/{tableId}")
    @ApiOperation(value = "字段")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "tableId", paramType = "path")
    })
    public ResultTable loadByTableId(@PathVariable(value = "tableId") String tableId) {
        ResultTable returnMsg = new ResultTable();
        List<JxStdDatabaseDataDicionaryColumn> list = new ArrayList<>();
        try {
            list = this.jxStdDatabaseDataDicColumService.selectByTableId(tableId);
         //   PageInfo<Object> pageInfo = new PageInfo(list);
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




    @PostMapping("save")
    public Result save(@RequestBody List<JxStdDatabaseDataDicionaryColumn> list) {
        try {
            this.jxStdDatabaseDataDicColumService.save(list);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
}
