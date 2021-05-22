package com.ctjsoft.orderserver.analyze.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctjsoft.orderserver.analyze.domain.DataSourceEntity;
import com.ctjsoft.orderserver.analyze.domain.DbInfo;
import com.ctjsoft.orderserver.analyze.service.StrategyService;
import com.ctjsoft.orderserver.order.domain.Drivers;
import com.ctjsoft.orderserver.order.service.IDriversService;
import org.datagear.management.domain.Schema;
import org.datagear.management.service.SchemaService;
import org.datagear.web.controller.AbstractSchemaConnTableController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;


@Service("strategyService")
public class StrategyServiceImpl implements StrategyService  {
    @Autowired
    private SchemaService schemaService;

    @Autowired
    IDriversService driversService;

    @Override
    public Schema findById(String id ) {
       /* DataSourceEntity entity = dataSourceMapper.getById(id);
        if (entity != null) {
            entity.setFullUrl(fullUrlName(entity.getUrl(), entity.getType(), entity.getDbName(), entity.getVersion()));
        }
        return entity;*/

        Schema schema = schemaService.getById(id);

        String className = schema.getUrl().substring( schema.getUrl().indexOf(":") + 1,  schema.getUrl().indexOf(":",  schema.getUrl().indexOf(":") + 1));
        Drivers drivers = new Drivers();
        drivers.setId(id);
        List<Drivers> driversList = driversService.selectDriversList(drivers, className);
        if (driversList.size() != 0) {
            className = driversList.get(0).getDriverclassnames();
        }
        DataSourceEntity entity = new DataSourceEntity();
        if (entity != null) {
            entity.setFullUrl(fullUrlName(schema.getUrl(), className, schema.getTitle(), null));
        }
        return schema;
    }

    public String fullUrlName(String url, String type, String dbName, String version) {
        String fullUrl = "";
      /*  if (DataSourceType.ORACLE.name().equals(type)) {
            fullUrl = "jdbc:oracle:thin:@" + url + ":" + dbName;
        } else if (DataSourceType.MYSQL.name().equals(type)) {
            fullUrl = "jdbc:mysql://" + url + "/" + dbName + "?serverTimezone=GMT-8";
        } else if (DataSourceType.HIVE.name().equals(type)) {
            fullUrl = ("1.x".equals(version) ? "jdbc:hive://" : "jdbc:hive2://") + url + "/"
                    + dbName;
        } else if (DataSourceType.MSSQ.name().equals(type)) {
            fullUrl = ("2000".equals(version) ? "jdbc:microsoft.sqlserver://" : "jdbc:sqlserver://")
                    + url + ";DatabaseName=" + dbName;
        }*/

        fullUrl = "jdbc:oracle:thin:@" + url + ":" + dbName;
        return fullUrl;
    }

}
