package com.ctjsoft.orderserver.analyze.common;

import com.ctjsoft.orderserver.analyze.domain.*;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.datagear.web.controller.AbstractSchemaConnTableController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zyx
 * @date 2020/12/21
 */

public class ScanUtil {

    @Autowired
    AbstractSchemaConnTableController connService;

    public static DatabaseScanObject firstScan(Map<String, List<JxStdDatabaseDataDicionaryColumn>> stdDicionColumnMap,
                                               List<JxStdDatabaseDataDicionary> stdDicionaryList,
                                               //  String databaseId, String driver, String url, String username, String pwd,

                                               DbInfo dbInfo) {

        return null;


    }

}
