package com.ctjsoft.orderserver.utils;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * 【请填写功能名称】对象 phone_list
 * 
 * @author jmys
 * @date 2021-05-10
 */
@Data
public class ExportVo extends   BaseRowModel
{
    private static final long serialVersionUID = 1L;



    @ColumnWidth(10)
    @ExcelProperty(value = "序号", index = 0)
    private String number;

    @ColumnWidth(50)
    @ExcelProperty(value = "表名", index = 1)
    private String tableName;

    @ColumnWidth(70)
    @ExcelProperty(value = "中文名（备注）", index = 2)
    private String chineseName;

    @ColumnWidth(20)
    @ExcelProperty(value = "数据行数", index = 3 )
    private String tableRowSize;

    @ColumnWidth(30)
    @ExcelProperty(value = "最新变更时间", index = 4)
    private String tableRowChangeTime;

    @ColumnWidth(20)
    @ExcelProperty(value = "检测结果", index = 5)
    private String detectionResult;


}
