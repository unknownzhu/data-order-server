package com.ctjsoft.orderserver.analyze.controller;


import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseDataDicionaryColumn;
import com.ctjsoft.orderserver.analyze.domain.JxStdScanInfo;
import com.ctjsoft.orderserver.analyze.mapper.JxStdScanInfoMapper;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicColumService;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.ctjsoft.util.ReturnMessage;
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
@RequestMapping("/jxStdScanInfo")
@RestController
@Api(value = "字段", tags = "字段")
public class JxStdScanInfoController {

    @Autowired
    private JxStdScanInfoMapper jxStdScanInfoMapper;

    @GetMapping("/selectByPrimaryKey")
    @ApiOperation(value = "字段")
    public ResultTable selectByPrimaryKey( String id) {
        ResultTable returnMsg = new ResultTable();
        JxStdScanInfo jxStdScanInfo = jxStdScanInfoMapper.selectByPrimaryKey(id);
        returnMsg.setData(jxStdScanInfo);
        return returnMsg;
    }

    @GetMapping("/list")
    @ApiOperation(value = "字段")
    public ResultTable list(  String databaseId) {
        ResultTable returnMsg = new ResultTable();
        JxStdScanInfo record = new JxStdScanInfo();
        record.setDbid(databaseId);
        List<JxStdScanInfo> list = jxStdScanInfoMapper.list(record);
        returnMsg.setData(list);
        returnMsg.setCode(0);
        return returnMsg;
    }

    @GetMapping("/selectById")
    @ApiOperation(value = "字段")
    public ReturnMessage selectById(String id) {
        ReturnMessage returnMsg = new ReturnMessage();
        JxStdScanInfo record   = jxStdScanInfoMapper.selectByPrimaryKey(id);
        returnMsg.setData(record);
        returnMsg.setCode("0000");
        return returnMsg;
    }



}
