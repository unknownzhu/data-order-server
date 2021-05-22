package com.ctjsoft.orderserver.analyze.controller;

import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDicionaryLog;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDicionaryLogService;
import com.ctjsoft.orderserver.utils.response.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gyx
 * @date 2020-11-12
 * @desc
 */
@RequestMapping("/jxStdDatabaseDicionaryLog")
@RestController
@Api(value = "数据源变更记录", tags = "数据源变更记录")
public class JxStdDatabaseDicionaryLogController {

    @Autowired
    private JxStdDatabaseDicionaryLogService stdDatabaseDicionaryLogService;


    @GetMapping("/detail/{tableId}")
    @ApiOperation(value = "数据源详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableId", value = "tableId", paramType = "path")
    })
    public Result loadByTableId(@PathVariable(value = "tableId") String tableId) {
        List<JxStdDatabaseDicionaryLog> detail = this.stdDatabaseDicionaryLogService.detail(tableId);
        return Result.success(detail);
    }
}
