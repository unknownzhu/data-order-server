package com.ctjsoft.orderserver.analyze.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ctjsoft.orderserver.analyze.common.ScanUtil;
import com.ctjsoft.orderserver.analyze.domain.*;
import com.ctjsoft.orderserver.analyze.mapper.*;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDataDicionaryService;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseDicionaryLogService;
import com.ctjsoft.orderserver.analyze.service.JxStdDatabaseQuestionRecordService;
import com.ctjsoft.orderserver.analyze.service.StrategyService;
import com.ctjsoft.orderserver.order.domain.OrderSqlLog;
import com.ctjsoft.orderserver.order.mapper.SchemaMapper;
import com.ctjsoft.orderserver.order.service.IOrderSqlLogService;
import com.ctjsoft.orderserver.utils.StringUtils;
import org.datagear.connection.DriverEntity;
import org.datagear.connection.DriverEntityManager;
import org.datagear.management.domain.Schema;
import org.datagear.util.StringUtil;
import org.datagear.web.controller.AbstractSchemaConnController;
import org.datagear.web.controller.AbstractSchemaConnTableController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gyx
 * @date 2020-11-11
 * @desc
 */
@Service
public class JxStdDatabaseDataDicionaryServiceImpl extends AbstractSchemaConnTableController implements JxStdDatabaseDataDicionaryService {


    // AbstractSchemaConnController connectHelpper;

    @Autowired
    private DriverEntityManager driverEntityManager;
    @Autowired
    private JxStdDatabaseMapper stdDatabaseMapper;
    @Autowired
    private SchemaMapper schemaMapper;
    @Autowired
    private JxStdScanInfoMapper jxStdScanInfoMapper;
    @Autowired
    private JxStdDatabaseDataDicionaryMapper stdDatabaseDataDicionaryMapper;
    @Autowired
    private JxStdDatabaseDataDicCRecordMapper stdDatabaseDataDicCRecordMapper;
    @Autowired
    private JxStdDatabaseDataDicColumMapper stdDatabaseDataDicColumMapper;
    @Autowired
    private JxStdDatabaseQuestionRecordMapper stdDatabaseQuestionRecordMapper;
    @Autowired
    private JxStdDatabaseDicionaryLogMapper stdDatabaseDicionaryLogMapper;
    @Autowired
    private IOrderSqlLogService orderSqlLogService;
    @Autowired
    private StrategyService strategyService;


    @Override
    public List<JxStdDatabaseDataDicionary> loadByDatabaseId(String databaseId) {
        return this.stdDatabaseDataDicionaryMapper.loadByDatabaseId(databaseId);
    }

    @Override
    public List<JxStdDatabaseDataDicionary> reScan(String databaseId) throws Exception {


        Schema dataSourceEntity = strategyService.findById(databaseId);
        List<JxStdDatabaseDataDicionary> stdDicionaryList = this.stdDatabaseDataDicionaryMapper.loadByDatabaseId(databaseId);
        if (null == stdDicionaryList || stdDicionaryList.size() == 0) {
            //初次扫描
            initDataDicionary(dataSourceEntity, databaseId);
        } else {
            //重新扫描
            EntityWrapper<ModelTargetTable> qryWrapper = new EntityWrapper<>();
            qryWrapper.eq("DATA_SOURCE_ID", databaseId);


            List<JxStdDatabaseDataDicionaryColumn> stdDicionColumnList = this.stdDatabaseDataDicColumMapper.loadByDatabaseId(databaseId);
            Map<String, List<JxStdDatabaseDataDicionaryColumn>> stdDicionColumnMap = stdDicionColumnList.stream().collect(Collectors.groupingBy(JxStdDatabaseDataDicionaryColumn::getTableName));

            DatabaseScanObject databaseScanObject = this.getScanInfo(stdDicionColumnMap, stdDicionaryList, dataSourceEntity);
            this.setScanInfo(databaseScanObject,databaseId);
            //  return null;
        }
        compareChangeLogWithOrderLog(databaseId);
        return null;
    }


    public void compareChangeLogWithOrderLog(String databaseId) {
        System.out.println("进入关联工单方法");

        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        System.out.println("当前时间为:" + today);

        OrderSqlLog orderSqlLog = new OrderSqlLog(databaseId, today);
        List<OrderSqlLog> orderSqlLogList = orderSqlLogService.selectOrderSqlLogList(orderSqlLog);


        for (OrderSqlLog tmp : orderSqlLogList) {
            String alterSql = (tmp.getSqls()).replaceAll("\\s+", " ");  //alterSql 为语句，如果修改字段名则只有一条记录，  JxStdDatabaseDicionaryLog  为修改记录，如果修改字段名则有两条：一条删除原有字段及结构，另一条为新增修改后字段及结构
            alterSql = alterSql.toUpperCase();
            Integer tt = alterSql.indexOf("TABLE");
            List<String> sqlParts = Arrays.asList(alterSql.split(" "));


            //     String tableName =    stdDatabaseDicionaryLogMapper.detailByTableIdAndDate(tmp)
      /*更新字段名	alter table TABLE_NAME rename column column_old to column_new;
            删除字段	alter table TABLE_NAME drop column COLUMN_NAME;

            添加字段	alter table TABLE_NAME add COLUMN_NAME varchar(10);
      添加字段并附值	alter table TABLE_NAME ADD COLUMN_NAME NUMBER(1) DEFAULT 1;

   修改字段数据类型	alter table tablename modify filedname varchar2(20); */


            String type = "";
            String tableName = "";
            String columnName = "";
            if (!alterSql.contains("TABLE")) {
                continue;
            }
            //  tableName = (alterSql.substring(alterSql.indexOf("TABLE"), alterSql.length() - 1)).split(" ")[1];
            if (alterSql.contains("RENAME")) {// 更新字段名
                //   columnName = (alterSql.substring(alterSql.indexOf("COLUMN"), alterSql.length() - 1)).split(" ")[1];
                type = "0  or type =  1";
            } else if (alterSql.contains("DROP")) {   // 删除字段名
                //   columnName = (alterSql.substring(alterSql.indexOf("COLUMN"), alterSql.length() - 1)).split(" ")[1];
                type = "1";
            } else if (alterSql.contains("ADD")) {    // 添加字段名
                //   columnName = (alterSql.substring(alterSql.indexOf("ADD"), alterSql.length() - 1)).split(" ")[1];
                type = "0";
            } else if (alterSql.contains("MODIFY")) { // 更新字段配置
                //   columnName = (alterSql.substring(alterSql.indexOf("MODIFY"), alterSql.length() - 1)).split(" ")[1];
                type = "2";
            }

            System.out.println(" 语句：" + alterSql);

            List<JxStdDatabaseDicionaryLog> stdDatabaseDicionaryLogList = stdDatabaseDicionaryLogMapper.detailBySqls(alterSql, databaseId, today);

            for (JxStdDatabaseDicionaryLog tmpStdDicLog : stdDatabaseDicionaryLogList) {

                System.out.println("开始关联工单");
                System.out.println("日志id：" + tmpStdDicLog.getId());
                System.out.println("工单id：" + tmp.getOrderId());
                stdDatabaseDicionaryLogMapper.update(tmpStdDicLog.getId(), tmp.getOrderId());
                //   stdDatabaseQuestionRecordMapper.updateOrderID(tmpStdDicLog.getRecordId(), tmp.getOrderId());
                JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord = new JxStdDatabaseQuestionRecord();
                jxStdDatabaseQuestionRecord.setId(tmpStdDicLog.getRecordId());
                jxStdDatabaseQuestionRecord.setOrderId(tmp.getOrderId());

                stdDatabaseQuestionRecordMapper.update(jxStdDatabaseQuestionRecord);

                System.out.println("成功关联工单");

            }


        }


    }

    public DatabaseScanObject getScanInfo(Map<String, List<JxStdDatabaseDataDicionaryColumn>> stdDicionColumnMap,
                                          List<JxStdDatabaseDataDicionary> stdDicionaryList,
                                          Schema dataSourceEntity) {
        DatabaseScanObject databaseScanObject = new DatabaseScanObject();
        Map<String, JxStdDatabaseDataDicionary> stdDicionMap = stdDicionaryList.stream().collect(Collectors.toMap(JxStdDatabaseDataDicionary::getTableName, StdDatabaseDataDicionary -> StdDatabaseDataDicionary));

        Connection connection = null;
        try { //getDicionaryLogRecords

            // 用户选定驱动程序时
            if (!isEmpty(dataSourceEntity.getDriverEntity()) && !isEmpty(dataSourceEntity.getDriverEntity().getId()))
            {
                DriverEntity driverEntity = this.driverEntityManager.get(dataSourceEntity.getDriverEntity().getId());
                dataSourceEntity.setDriverEntity(driverEntity);
            }




            connection = getSchemaConnection(dataSourceEntity);
            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();

            Map<String, String> commentTabMap = new HashMap<>();
            String sql = "select table_name,comments from all_tab_comments where owner=" + "'" + dataSourceEntity.getUser().toUpperCase() + "'";
            ResultSet commentTab = statement.executeQuery(sql);
            while (commentTab.next()) {
                commentTabMap.put(commentTab.getString("table_name"), commentTab.getString("comments"));
            }
            ResultSet refreshDbSpace = statement.executeQuery("" +

                    "\n" +
                    " SELECT a.tablespace_name  ,\n" +
                    "total  ,\n" +
                    "free  ,\n" +
                    "(total - free)  used  \n" +
                    "FROM (SELECT tablespace_name, SUM(bytes) free\n" +
                    "FROM dba_free_space\n" +
                    "GROUP BY tablespace_name) a,\n" +
                    "(SELECT tablespace_name, SUM(bytes) total\n" +
                    "FROM dba_data_files\n" +
                    "GROUP BY tablespace_name) b    \n" +
                    "WHERE a.tablespace_name = b.tablespace_name\n" +
                    "and a.tablespace_name in (select default_tablespace from dba_users where username =" + "'" + dataSourceEntity.getUser().toUpperCase() + "')"
            );
            String tablespace_name = "";
            String total = "";
            String used = "";
            String free = "";
            while (refreshDbSpace.next()) {
                tablespace_name = refreshDbSpace.getString("tablespace_name");
                total = refreshDbSpace.getString("total");
                used = refreshDbSpace.getString("used");
                free = refreshDbSpace.getString("free");

            }
            schemaMapper.updateDbSpsce(dataSourceEntity.getId(), tablespace_name, total, used, free);


            List<JxStdDatabaseDataDicionary> tableRowNumChange = new ArrayList<>();
            ResultSet ttt = statement.executeQuery(" select  t.table_name,t.num_rows from user_tables t ORDER BY NUM_ROWS DESC   ");
            while (ttt.next()) {
                JxStdDatabaseDataDicionary jxStdDatabaseDataDicionary = new JxStdDatabaseDataDicionary();
                jxStdDatabaseDataDicionary.setTableName(ttt.getString(1) + "");
                jxStdDatabaseDataDicionary.setTableRowSize(ttt.getString(2));
                jxStdDatabaseDataDicionary.setDatabaseId(dataSourceEntity.getId());
                tableRowNumChange.add(jxStdDatabaseDataDicionary);
            }

            for (JxStdDatabaseDataDicionary tmp : tableRowNumChange) {
                String tableChangeSql = "   select   to_char(scn_to_timestamp(max(ora_rowscn)),'YYYY-MM-DD HH:MM:SS')    as tableRowChangeTime from    " + tmp.getTableName();
                String tableRowChangeTime = "";
                try {
                    ResultSet tmpResult = statement.executeQuery(tableChangeSql); //获取数据库所有的数据库名
                    while (tmpResult.next()) { //这里必返回一行或者报错
                        tableRowChangeTime = tmpResult.getString(1) + "";
                    }
                    System.out.println(tableChangeSql);
                    System.out.println(tableRowChangeTime);
                } catch (Exception e) {
                    //  e.printStackTrace();
                    tableRowChangeTime = "5天以上未使用";
                }
                tmp.setTableRowChangeTime(tableRowChangeTime);

            }
            databaseScanObject.getTableRowNumChange().addAll(tableRowNumChange);


            ResultSet show_databases = statement.executeQuery("select table_name from all_tables where owner=" + "'" + dataSourceEntity.getUser().toUpperCase() + "'" + " order by table_name"); //获取数据库所有的数据库名
            JxStdDatabaseDataDicionary record;
            List<JxStdDatabaseDataDicionary> list = new ArrayList<>();
            List<JxStdDatabaseDataDicionaryColumn> orignDicionaryColumn = new ArrayList<>();
            Map<String, List<JxStdDatabaseDataDicionaryColumn>> originColumnMap = new HashMap<>();
            List<JxStdDatabaseDataDicionaryColumn> lstTempColumn;
            JxStdDatabaseDataDicionaryColumn dicionaryColumnRecord;
            Set<String> tableNameSet = new HashSet<>();
            while (show_databases.next()) {
                record = new JxStdDatabaseDataDicionary();
                record.setId(StringUtils.getUUid());
                //   record.setTableName(show_databases.getString(1));
                String tableName = show_databases.getString(1);
                List<JxStdDatabaseDataDicionary> jxStdDatabaseDataDicionaryList = this.selectByDbIdAndTableName(dataSourceEntity.getId(), tableName);
                if (jxStdDatabaseDataDicionaryList != null && jxStdDatabaseDataDicionaryList.size() == 1 && jxStdDatabaseDataDicionaryList.get(0).getId() != null) {
                    record.setId(jxStdDatabaseDataDicionaryList.get(0).getId());
                }
                //   listDistinctColumnNameByDbIdAndTbaleName

                record.setTableName(tableName);
                record.setDatabaseId(dataSourceEntity.getId());
                if (null != commentTabMap.get(record.getTableName())) {
                    record.setChineseName(commentTabMap.get(record.getTableName()));
                } else {
                    record.setChineseName("");
                }
                list.add(record);
                tableNameSet.add(show_databases.getString(1));
            }

            ResultSet column_remark = statement.executeQuery("select * from all_col_comments col where owner=" + "'" + dataSourceEntity.getUser().toUpperCase() + "'");
            Map<String, String> commentMap = new HashMap<>();//将主键存入map中 将子段为主键的存入数据库
            while (column_remark.next()) {
                commentMap.put(column_remark.getString("TABLE_NAME") + "-" + column_remark.getString("COLUMN_NAME"), column_remark.getString("COMMENTS"));
            }

            ResultSet column_PK = statement.executeQuery("select col.table_name TABLE_NAME,col.column_name COLUMN_NAME from all_constraints con,all_cons_columns col where con.constraint_name = col.constraint_name and con.constraint_type='P' and con.owner = " + "'" + dataSourceEntity.getUser().toUpperCase() + "'");
            Map<String, String> pkMap = new HashMap<>();//将主键存入map中 将子段为主键的存入数据库
            while (column_PK.next()) {
                pkMap.put(column_PK.getString("TABLE_NAME"), column_PK.getString("COLUMN_NAME"));
            }
            String isPk;
            String tempId;
            String isNull;
            String remark;
            for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : list) {
                String table = stdDatabaseDataDicionary.getTableName();
                tempId = pkMap.get(table);
                lstTempColumn = new ArrayList();
               // ResultSet column_data = statement.executeQuery("select column_id,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,data_scale,NULLABLE,DATA_DEFAULT,TABLE_NAME from all_tab_columns where TABLE_NAME=" + "'" + table + "'" + " and owner=" + "'" + dataSourceEntity.getUser().toUpperCase() + "'" + " order by column_id");
                ResultSet column_data = statement.executeQuery("select column_id,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,data_scale,NULLABLE,DATA_DEFAULT,TABLE_NAME from user_tab_columns where TABLE_NAME=" + "'" + table + "'"   );

                while (column_data.next()) {
                    if (column_data.getString("COLUMN_NAME").equalsIgnoreCase(tempId)) {
                        isPk = "1";
                    } else {
                        isPk = "0";
                    }
                    if (("Y").equalsIgnoreCase(column_data.getString("NULLABLE").toUpperCase())) {
                        isNull = "0";
                    } else {
                        isNull = "1";
                    }
                    remark = commentMap.get(table + "-" + column_data.getString("COLUMN_NAME"));
                    if (null == remark) {
                        remark = "";
                    }

                    dicionaryColumnRecord = new JxStdDatabaseDataDicionaryColumn(StringUtils.getUUid(), stdDatabaseDataDicionary.getId(), column_data.getString("COLUMN_NAME"),
                            column_data.getString("DATA_TYPE"), column_data.getInt("DATA_LENGTH"), (null == column_data.getString("DATA_PRECISION") ? "" : column_data.getString("DATA_PRECISION")), isPk,
                            isNull, "", remark, column_data.getString("TABLE_NAME"), dataSourceEntity.getId());
                    lstTempColumn.add(dicionaryColumnRecord);

                }
                originColumnMap.put(stdDatabaseDataDicionary.getTableName(), lstTempColumn);
            }

            //先判断是否删除表
            for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : stdDicionaryList) {
                if (!tableNameSet.contains(stdDatabaseDataDicionary.getTableName())) {
                    JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), "", "删除表", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                    JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), 2, "删除表", stdDatabaseDataDicionary.getDatabaseId());

                    databaseScanObject.getQuestionRecords().add(questionRecord);
                    databaseScanObject.getChangeRecords().add(changeRecord);
                    databaseScanObject.getDeleteDicRecords().add(stdDatabaseDataDicionary);

                    //  questionRecords.add(questionRecord);
                    //  changeRecords.add(changeRecord);
                    //  deleteDicRecords.add(stdDatabaseDataDicionary);
                }
            }

            //先判断是否新增表
            for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : list) {
                JxStdDatabaseDataDicionary tempDicionary = stdDicionMap.get(stdDatabaseDataDicionary.getTableName());
                if (null == tempDicionary) {
                    JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), "", "创建表", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                    JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), 0, "创建表", stdDatabaseDataDicionary.getDatabaseId());


                    databaseScanObject.getQuestionRecords().add(questionRecord);
                    databaseScanObject.getChangeRecords().add(changeRecord);
                    databaseScanObject.getInsertDicRecords().add(stdDatabaseDataDicionary);
                    databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(
                            StringUtils.getUUid(), new Date(), 0, "导入该表结构", stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), null, null, questionRecord.getId())
                    );

                } else {
                    if (!(StringUtils.isEmpty(stdDatabaseDataDicionary.getChineseName()) && StringUtils.isEmpty(tempDicionary.getChineseName()))) {
                        if (!stdDatabaseDataDicionary.getChineseName().equals(tempDicionary.getChineseName())) { //判断表备注是否已经修改
                            JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), "", "修改表", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                            JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), 1, "修改表", stdDatabaseDataDicionary.getDatabaseId());

                            tempDicionary.setChineseName(stdDatabaseDataDicionary.getChineseName());
                            databaseScanObject.getQuestionRecords().add(questionRecord);
                            databaseScanObject.getChangeRecords().add(changeRecord);
                            databaseScanObject.getUpdateDicRecords().add(tempDicionary);
                            databaseScanObject.getDicionaryLogRecords().add(
                                    new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改表备注为 " + tempDicionary.getChineseName(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), null, null, questionRecord.getId())
                            );

                        }
                    }
                }
            }
            // 这里是获取
            for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : list) {

            }


            //判断表 循环遍历list
            for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : list) {
                orignDicionaryColumn = originColumnMap.get(stdDatabaseDataDicionary.getTableName());//链接数据库的字段
                String table = stdDatabaseDataDicionary.getTableName();
                JxStdDatabaseDataDicionary tempDicionary = stdDicionMap.get(table);
                if (null == tempDicionary) { //如果本地库数据字典为空 直接continue
                    for (JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn : orignDicionaryColumn) {
                        //JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), stdDatabaseDataDicionaryColumn.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                stdDatabaseDataDicionaryColumn.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn.getTableId()

                        );

                        questionRecord.setId(StringUtils.getUUid());

                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn.getTableId(), stdDatabaseDataDicionary.getTableName(), 0, "创建字段", stdDatabaseDataDicionary.getDatabaseId());

                        databaseScanObject.getQuestionRecords().add(questionRecord);
                        databaseScanObject.getChangeRecords().add(changeRecord);
                        databaseScanObject.getInsertColumnsRecord().add(stdDatabaseDataDicionaryColumn);
                        databaseScanObject.getDicionaryLogRecords().add(
                                new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 0, "增加字段 ", stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), null, questionRecord.getId())
                        );
                    }
                    continue;
                }
                tempDicionary.setDetectionResult("1");
                List<JxStdDatabaseDataDicionaryColumn> tempStdDicionColumnList = stdDicionColumnMap.get(table); //数据库中存在的子段
                if (null == tempStdDicionColumnList) {//如果本地库表字段为空 continue
                    for (JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn : orignDicionaryColumn) {
                        //JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), stdDatabaseDataDicionaryColumn.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                stdDatabaseDataDicionaryColumn.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn.getTableId()

                        );

                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn.getTableId(), stdDatabaseDataDicionary.getTableName(), 0, "创建字段", stdDatabaseDataDicionary.getDatabaseId());

                        databaseScanObject.getQuestionRecords().add(questionRecord);
                        databaseScanObject.getChangeRecords().add(changeRecord);
                        databaseScanObject.getInsertColumnsRecord().add(stdDatabaseDataDicionaryColumn);
                        databaseScanObject.getDicionaryLogRecords().add(
                                new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 0, "增加字段 ", stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), null, questionRecord.getId())
                        );

                    }
                    continue;
                }
                Set<String> orignColumnSet = new HashSet();
                for (JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn : orignDicionaryColumn) {
                    orignColumnSet.add(stdDatabaseDataDicionaryColumn.getName());
                }
                Set<String> stdColumnSet = new HashSet();
                for (JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn : tempStdDicionColumnList) {
                    stdColumnSet.add(stdDatabaseDataDicionaryColumn.getName());
                }
                for (JxStdDatabaseDataDicionaryColumn tempColumn : orignDicionaryColumn) { //先判断 是否增加字段
                    if (!stdColumnSet.contains(tempColumn.getName())) {
                        //  JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), tempColumn.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                tempColumn.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                tempColumn.getIsEmpty(), tempColumn.getIsPk(), tempColumn.getLength(),
                                tempColumn.getDefaultValue(), tempColumn.getType(), tempColumn.getTableId()

                        );

                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), tempColumn.getTableId(), stdDatabaseDataDicionary.getTableName(), 0, "创建字段", stdDatabaseDataDicionary.getDatabaseId());

                        databaseScanObject.getQuestionRecords().add(questionRecord);
                        databaseScanObject.getChangeRecords().add(changeRecord);
                        databaseScanObject.getInsertColumnsRecord().add(tempColumn);
                        databaseScanObject.getDicionaryLogRecords().add(
                                new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 0, "增加字段 " + tempColumn.getName(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), tempColumn.getName(), null, questionRecord.getId())
                        );

                    }
                }

                for (JxStdDatabaseDataDicionaryColumn tempColumn : tempStdDicionColumnList) { //先判断 是否减少了字段
                    if (!orignColumnSet.contains(tempColumn.getName())) {
                        //  JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), tempColumn.getName(), "删除字段", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), tempColumn.getTableId(), stdDatabaseDataDicionary.getTableName(), 2, "删除字段", stdDatabaseDataDicionary.getDatabaseId());

                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                tempColumn.getName(), "删除字段", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                tempColumn.getIsEmpty(), tempColumn.getIsPk(), tempColumn.getLength(),
                                tempColumn.getDefaultValue(), tempColumn.getType(), tempColumn.getTableId()

                        );


                        databaseScanObject.getQuestionRecords().add(questionRecord);
                        databaseScanObject.getChangeRecords().add(changeRecord);
                        databaseScanObject.getDeleteColumnsRecord().add(tempColumn);
                        databaseScanObject.getDicionaryLogRecords().add(
                                new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 1, "删除字段 " + tempColumn.getName(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), null, tempColumn.getName(), questionRecord.getId())
                        );

                    }
                }

                if (null != tempStdDicionColumnList) {
                    for (JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn : orignDicionaryColumn) {
                        for (JxStdDatabaseDataDicionaryColumn stdDatabaseDataDicionaryColumn1 : tempStdDicionColumnList) {
                            if (stdDatabaseDataDicionaryColumn.getName().equals(stdDatabaseDataDicionaryColumn1.getName())) {
                                //长度
                                if (stdDatabaseDataDicionaryColumn.getLength().intValue() != stdDatabaseDataDicionaryColumn1.getLength().intValue()) {
                                //    tempDicionary.setDetectionResult("1");
                                    JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                            stdDatabaseDataDicionaryColumn.getName(), "字段长度修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                            stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                            stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()

                                    );
                                    databaseScanObject.getDicionaryLogRecords().add(
                                            new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 长度为 " + stdDatabaseDataDicionaryColumn.getLength() + " 原 " + stdDatabaseDataDicionaryColumn1.getLength(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));


                                    JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段长度修改", stdDatabaseDataDicionary.getDatabaseId());
                                    stdDatabaseDataDicionaryColumn1.setLength(stdDatabaseDataDicionaryColumn.getLength());

                                    databaseScanObject.getQuestionRecords().add(questionRecord);
                                    databaseScanObject.getChangeRecords().add(changeRecord);
                                    databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                    // questionRecords.add(questionRecord);
                                    // changeRecords.add(changeRecord);
                                    // updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                }
                                //类型
                                if (!(StringUtils.isEmpty(stdDatabaseDataDicionaryColumn.getType()) && StringUtils.isEmpty(stdDatabaseDataDicionaryColumn1.getType()))) {
                                    if (!stdDatabaseDataDicionaryColumn.getType().equals(stdDatabaseDataDicionaryColumn1.getType())) {
                                      //  tempDicionary.setDetectionResult("1");
                                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                                stdDatabaseDataDicionaryColumn.getName(), "字段类型修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                                stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                                stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()

                                        );
                                        databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 类型为 " + stdDatabaseDataDicionaryColumn.getType() + " 原 " + stdDatabaseDataDicionaryColumn1.getType(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));


                                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段类型修改", stdDatabaseDataDicionary.getDatabaseId());
                                        stdDatabaseDataDicionaryColumn1.setType(stdDatabaseDataDicionaryColumn.getType());


                                        databaseScanObject.getQuestionRecords().add(questionRecord);
                                        databaseScanObject.getChangeRecords().add(changeRecord);
                                        databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                        //   questionRecords.add(questionRecord);
                                        //   changeRecords.add(changeRecord);
                                        //   updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                    }
                                }
                                //精确度

                                if (!(StringUtils.isEmpty(stdDatabaseDataDicionaryColumn.getAccuracy()) && StringUtils.isEmpty(stdDatabaseDataDicionaryColumn1.getAccuracy()))) {
                                    if (!stdDatabaseDataDicionaryColumn.getAccuracy().equals(stdDatabaseDataDicionaryColumn1.getAccuracy())) {
                                       // tempDicionary.setDetectionResult("1");
                                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                                stdDatabaseDataDicionaryColumn.getName(), "字段精确度修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                                stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                                stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()
                                        );
                                        databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 精确度为 " + stdDatabaseDataDicionaryColumn.getAccuracy() + " 原 " + stdDatabaseDataDicionaryColumn1.getAccuracy(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));

                                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段精确度修改", stdDatabaseDataDicionary.getDatabaseId());
                                        stdDatabaseDataDicionaryColumn1.setAccuracy(stdDatabaseDataDicionaryColumn.getAccuracy());

                                        databaseScanObject.getQuestionRecords().add(questionRecord);
                                        databaseScanObject.getChangeRecords().add(changeRecord);
                                        databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                        //   questionRecords.add(questionRecord);
                                        //   changeRecords.add(changeRecord);
                                        //   updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                    }
                                }

                                //是否属于主键
                                if (!stdDatabaseDataDicionaryColumn.getIsPk().equals(stdDatabaseDataDicionaryColumn1.getIsPk())) {
                                  //  tempDicionary.setDetectionResult("1");
                                    JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                            stdDatabaseDataDicionaryColumn.getName(), "字段主键修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                            stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                            stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()

                                    );
                                    databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 主键为 " + stdDatabaseDataDicionaryColumn.getIsPk() + " 原 " + stdDatabaseDataDicionaryColumn1.getIsPk(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));

                                    JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段主键修改", stdDatabaseDataDicionary.getDatabaseId());
                                    stdDatabaseDataDicionaryColumn1.setIsPk(stdDatabaseDataDicionaryColumn.getIsPk());

                                    databaseScanObject.getQuestionRecords().add(questionRecord);
                                    databaseScanObject.getChangeRecords().add(changeRecord);
                                    databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                    //  questionRecords.add(questionRecord);
                                    //  changeRecords.add(changeRecord);
                                    //  updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                }

                                //是否为空
                                if (!stdDatabaseDataDicionaryColumn.getIsEmpty().equals(stdDatabaseDataDicionaryColumn1.getIsEmpty())) {
                                  //  tempDicionary.setDetectionResult("1");
                                    JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                            stdDatabaseDataDicionaryColumn.getName(), "字段可为空修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                            stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                            stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()

                                    );
                                    String isEmpty = (stdDatabaseDataDicionaryColumn.getIsEmpty()).equals("0") ? "否" : "是";
                                    String isEmpty1 = (stdDatabaseDataDicionaryColumn1.getIsEmpty()).equals("0") ? "否" : "是";
                                    databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 可为空为 " + isEmpty + " 原 " + isEmpty1, stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));

                                    JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段为空修改", stdDatabaseDataDicionary.getDatabaseId());
                                    stdDatabaseDataDicionaryColumn1.setIsEmpty(stdDatabaseDataDicionaryColumn.getIsEmpty());

                                    databaseScanObject.getQuestionRecords().add(questionRecord);
                                    databaseScanObject.getChangeRecords().add(changeRecord);
                                    databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                    // questionRecords.add(questionRecord);
                                    // changeRecords.add(changeRecord);
                                    // updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                }

                                if (!(StringUtils.isEmpty(stdDatabaseDataDicionaryColumn.getDefaultValue()) && StringUtils.isEmpty(stdDatabaseDataDicionaryColumn1.getDefaultValue()))) {
                                    if (!stdDatabaseDataDicionaryColumn.getDefaultValue().equals(stdDatabaseDataDicionaryColumn1.getDefaultValue())) {
                                     //   tempDicionary.setDetectionResult("1");
                                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                                stdDatabaseDataDicionaryColumn.getName(), "字段默认值修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                                stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                                stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()

                                        );
                                        databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 默认值为 " + stdDatabaseDataDicionaryColumn.getDefaultValue() + " 原 " + stdDatabaseDataDicionaryColumn1.getDefaultValue(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));


                                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段默认值修改", stdDatabaseDataDicionary.getDatabaseId());
                                        stdDatabaseDataDicionaryColumn1.setDefaultValue(stdDatabaseDataDicionaryColumn.getDefaultValue());

                                        databaseScanObject.getQuestionRecords().add(questionRecord);
                                        databaseScanObject.getChangeRecords().add(changeRecord);
                                        databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                        //   questionRecords.add(questionRecord);
                                        //   changeRecords.add(changeRecord);
                                        //   updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                    }
                                }

                                //中文名
                                if (!(StringUtils.isEmpty(stdDatabaseDataDicionaryColumn.getChineseName()) && StringUtils.isEmpty(stdDatabaseDataDicionaryColumn1.getChineseName()))) {
                                    if (!stdDatabaseDataDicionaryColumn.getChineseName().equals(stdDatabaseDataDicionaryColumn1.getChineseName())) {
                                       // tempDicionary.setDetectionResult("1");
                                        JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                                                stdDatabaseDataDicionaryColumn.getName(), "字段备注修改", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                                                stdDatabaseDataDicionaryColumn.getIsEmpty(), stdDatabaseDataDicionaryColumn.getIsPk(), stdDatabaseDataDicionaryColumn.getLength(),
                                                stdDatabaseDataDicionaryColumn.getDefaultValue(), stdDatabaseDataDicionaryColumn.getType(), stdDatabaseDataDicionaryColumn1.getTableId()

                                        );
                                        databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 2, "修改字段 " + stdDatabaseDataDicionaryColumn.getName() + " 备注为 " + stdDatabaseDataDicionaryColumn.getChineseName() + " 原 " + stdDatabaseDataDicionaryColumn1.getChineseName(), stdDatabaseDataDicionary.getId(), stdDatabaseDataDicionary.getTableName(), dataSourceEntity.getId(), stdDatabaseDataDicionaryColumn.getName(), stdDatabaseDataDicionaryColumn1.getName(), questionRecord.getId()));


                                        JxStdDatabaseDataDicChangeRecord changeRecord = new JxStdDatabaseDataDicChangeRecord(StringUtils.getUUid(), stdDatabaseDataDicionaryColumn1.getTableId(), stdDatabaseDataDicionary.getTableName(), 1, "字段备注修改", stdDatabaseDataDicionary.getDatabaseId());
                                        stdDatabaseDataDicionaryColumn1.setChineseName(stdDatabaseDataDicionaryColumn.getChineseName());

                                        databaseScanObject.getQuestionRecords().add(questionRecord);
                                        databaseScanObject.getChangeRecords().add(changeRecord);
                                        databaseScanObject.getUpdateColumnsRecord().add(stdDatabaseDataDicionaryColumn1);

                                        //   questionRecords.add(questionRecord);
                                        //   changeRecords.add(changeRecord);
                                        //   updateColumnsRecord.add(stdDatabaseDataDicionaryColumn1);
                                    }
                                }

                            }
                        }
                    }
                }
                if ("1".equals(tempDicionary.getDetectionResult())) {

                    databaseScanObject.getUpdateDicRecords().add(tempDicionary);
                    // databaseScanObject.getDicionaryLogRecords().add(new JxStdDatabaseDicionaryLog(StringUtils.getUUid(), new Date(), 1, "修改字段 "+tempColumn.getName(), stdDatabaseDataDicionary.getId()));

                }
            }
            databaseScanObject.setScanStatus("1");
            databaseScanObject.setInfo("");

            //return dataSource.isRunning();
        } catch (Exception e) {
            e.printStackTrace();
            databaseScanObject.setScanStatus("0");
            databaseScanObject.setInfo( e.getMessage().toString().length() < 250 ? e.getMessage().toString() : e.getMessage().toString().substring(0, 250) );
            // throw new ScException("数据库连接异常");
        } finally {
            try {
                if (connection != null) {
                    connection.close();

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //  DataSourceUtils.closeAll(connection, null, null);

        }

        return databaseScanObject;
    }

    public void setScanInfo(DatabaseScanObject databaseScanObject,String dbid) {
        JxStdScanInfo jxStdScanInfo = new JxStdScanInfo();
        String scanInfoId = StringUtils.getUUid();
        jxStdScanInfo.setId(scanInfoId);
        jxStdScanInfo.setDbid(dbid);

        String scanStatus=databaseScanObject.getScanStatus();
        String info = databaseScanObject.getInfo();

        try {


            List<JxStdDatabaseQuestionRecord> questionRecords = databaseScanObject.getQuestionRecords();
            List<JxStdDatabaseDataDicChangeRecord> changeRecords = databaseScanObject.getChangeRecords();
            List<JxStdDatabaseDataDicionary> updateDicRecords = databaseScanObject.getUpdateDicRecords();
            List<JxStdDatabaseDataDicionary> insertDicRecords = databaseScanObject.getInsertDicRecords();
            List<JxStdDatabaseDataDicionary> deleteDicRecords = databaseScanObject.getDeleteDicRecords();
            List<JxStdDatabaseDataDicionaryColumn> updateColumnsRecord = databaseScanObject.getUpdateColumnsRecord();
            List<JxStdDatabaseDataDicionaryColumn> insertColumnsRecord = databaseScanObject.getInsertColumnsRecord();
            List<JxStdDatabaseDataDicionaryColumn> deleteColumnsRecord = databaseScanObject.getDeleteColumnsRecord();
            List<JxStdDatabaseDicionaryLog> dicionaryLogRecords = databaseScanObject.getDicionaryLogRecords();


            for (JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord : questionRecords) {
                jxStdDatabaseQuestionRecord.setScanId(scanInfoId);
                jxStdDatabaseQuestionRecord.setStatus("0");
            }


            List<JxStdDatabaseDataDicionary> tableRowNumChange = databaseScanObject.getTableRowNumChange();

            if (null != tableRowNumChange && tableRowNumChange.size() > 0) {
                stdDatabaseDataDicionaryMapper.tableRowNumChange(tableRowNumChange);
            }

            questionRecords = this.transJxStdDatabaseQuestionRecord(questionRecords);
            if (null != questionRecords && questionRecords.size() > 0) {
                if (questionRecords.size() <= 2000) {
                    stdDatabaseQuestionRecordMapper.batchInsert(questionRecords);
                } else {
                    for (int i = 0; i < (int) Math.ceil(questionRecords.size() / 2000.0); i++) {
                        List<JxStdDatabaseQuestionRecord> insertSqlList = questionRecords.subList(i * 2000, Math.min((i + 1) * 2000, questionRecords.size() - 1));

                        stdDatabaseQuestionRecordMapper.batchInsert(insertSqlList);

                    }
                }
            }


            if (null != changeRecords && changeRecords.size() > 0) {//批量新增改变记录
                if (changeRecords.size() <= 2000) {
                    stdDatabaseDataDicCRecordMapper.batchInsert(changeRecords);
                } else {
                    int times = (int) Math.ceil(changeRecords.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDataDicCRecordMapper.batchInsert(changeRecords.subList(i * 2000, Math.min((i + 1) * 2000, changeRecords.size() - 1)));
                    }
                }
            }
            if (null != updateDicRecords && updateDicRecords.size() > 0) {//批量新增修改数据字典
                stdDatabaseDataDicionaryMapper.batchUpdate(updateDicRecords);
            }
            if (null != insertDicRecords && insertDicRecords.size() > 0) {//批量新增数据字典
                if (insertDicRecords.size() <= 2000) {
                    stdDatabaseDataDicionaryMapper.batchInsert(insertDicRecords);
                } else {
                    int times = (int) Math.ceil(insertDicRecords.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDataDicionaryMapper.batchInsert(insertDicRecords.subList(i * 2000, Math.min((i + 1) * 2000, insertDicRecords.size() - 1)));
                    }
                }
            }
            if (null != deleteDicRecords && deleteDicRecords.size() > 0) {
                stdDatabaseDataDicionaryMapper.batchDelete(deleteDicRecords);//批量删除数据字典
            }
            if (null != updateColumnsRecord && updateColumnsRecord.size() > 0) {
                stdDatabaseDataDicColumMapper.batchUpdate(updateColumnsRecord);//批量修改数据字段
            }
            if (null != insertColumnsRecord && insertColumnsRecord.size() > 0) {//批量新增数据字段
                if (insertColumnsRecord.size() <= 2000) {
                    stdDatabaseDataDicColumMapper.batchInsert(insertColumnsRecord);
                } else {
                    int times = (int) Math.ceil(insertColumnsRecord.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDataDicColumMapper.batchInsert(insertColumnsRecord.subList(i * 2000, Math.min((i + 1) * 2000, insertColumnsRecord.size() - 1)));
                    }
                }
            }
            if (null != deleteColumnsRecord && deleteColumnsRecord.size() > 0) {
                stdDatabaseDataDicColumMapper.batchDelete(deleteColumnsRecord);//批量删除字段
            }
            if (null != deleteColumnsRecord && deleteColumnsRecord.size() > 0) {
                stdDatabaseDataDicColumMapper.batchDelete(deleteColumnsRecord);//批量删除字段
            }
            if (null != dicionaryLogRecords && dicionaryLogRecords.size() > 0) {//变更记录
                dicionaryLogRecords = transJxStdDatabaseDicionaryLog(dicionaryLogRecords);
                if (dicionaryLogRecords.size() <= 2000) {
                    stdDatabaseDicionaryLogMapper.batchInsert(dicionaryLogRecords);
                } else {
                    int times = (int) Math.ceil(dicionaryLogRecords.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDicionaryLogMapper.batchInsert(dicionaryLogRecords.subList(i * 2000, Math.min((i + 1) * 2000, dicionaryLogRecords.size() - 1)));
                    }
                }
            }
            jxStdScanInfo = new JxStdScanInfo(scanInfoId, new Date(), "", "",  updateDicRecords.size() +insertDicRecords.size()+deleteDicRecords.size() ,
                    jxStdScanInfoMapper.selcountByDbId(dbid)+1,
                    updateDicRecords.size(),insertDicRecords.size(),deleteDicRecords.size(),updateColumnsRecord.size(),
                    insertColumnsRecord.size(),deleteColumnsRecord.size(),dbid,scanStatus,info);
        } catch (Exception e) {
            jxStdScanInfo = new JxStdScanInfo(scanInfoId, dbid, "0", e.getMessage().toString().length() < 250 ? e.getMessage().toString() : e.getMessage().toString().substring(0, 250));
        }finally {
            jxStdScanInfoMapper.insert(jxStdScanInfo);

        }

    }


    @Transactional
    public void save(List<JxStdDatabaseDataDicionary> list) {
        for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : list) {
            this.stdDatabaseDataDicionaryMapper.update(stdDatabaseDataDicionary);
        }
    }

    @Override
    //@Transactional
    public List<JxStdDatabaseDataDicionary> loadByDbIdAndKeyword(String databaseId, String keyword) {
        return this.stdDatabaseDataDicionaryMapper.loadByDbIdAndKeyword(databaseId, keyword);
    }

    @Override
    //@Transactional
    public List<JxStdDatabaseDataDicionary> loadByDbIdAndKeywordPage(String databaseId, String keyword, String page, String limit) {
        return this.stdDatabaseDataDicionaryMapper.loadByDbIdAndKeywordPage(databaseId, keyword, page, limit);
    }

    @Override
    //@Transactional
    public String loadByDbIdAndKeywordCount(String databaseId, String keyword) {
        return this.stdDatabaseDataDicionaryMapper.loadByDbIdAndKeywordCount(databaseId, keyword);
    }

    @Override
    //@Transactional
    public List<JxStdDatabaseDataDicionary> selectByDbIdAndTableName(String databaseId, String keyword) {
        return this.stdDatabaseDataDicionaryMapper.selectByDbIdAndTableName(databaseId, keyword);
    }


    private void initDataDicionary(Schema schema, String databaseId) throws Exception {
        JxStdScanInfo jxStdScanInfo = new JxStdScanInfo();
        String scanInfoId = StringUtils.getUUid();
        jxStdScanInfo.setId(scanInfoId);
        jxStdScanInfo.setDbid(databaseId);
        try {
            DbInfo dbInfo = new DbInfo();
            BeanUtils.copyProperties(schema, dbInfo);
            dbInfo.setUserName(schema.getUser());

            Connection connection = null;

            if (!isEmpty(schema.getDriverEntity()) && !isEmpty(schema.getDriverEntity().getId()))
            {
                DriverEntity driverEntity = this.driverEntityManager.get(schema.getDriverEntity().getId());
                schema.setDriverEntity(driverEntity);
            }


            connection = getSchemaConnection(schema);
            connection.setAutoCommit(false);

            System.out.println("createConnection");

            Statement statement = connection.createStatement();

            ResultSet refreshDbSpace = statement.executeQuery("" +
                    " \n" +
                    "SELECT a.tablespace_name, \n" +
                    "a.bytes total, \n" +
                    "b.bytes used, \n" +
                    "c.bytes free, \n" +
                    "(b.bytes * 100) / a.bytes \"% USED \", \n" +
                    "(c.bytes * 100) / a.bytes \"% FREE \" \n" +
                    "FROM sys.sm$ts_avail a, sys.sm$ts_used b, sys.sm$ts_free c \n" +
                    "WHERE a.tablespace_name = b.tablespace_name \n" +
                    "AND a.tablespace_name = c.tablespace_name \n" +
                    "and a.tablespace_name in (select default_tablespace from dba_users where username =" + "'" + schema.getUser().toUpperCase() + "')"
            );
            String tablespace_name = "";
            String total = "";
            String used = "";
            String free = "";
            while (refreshDbSpace.next()) {
                tablespace_name = refreshDbSpace.getString("tablespace_name");
                total = refreshDbSpace.getString("total");
                used = refreshDbSpace.getString("used");
                free = refreshDbSpace.getString("free");

            }
            schemaMapper.updateDbSpsce(databaseId, tablespace_name, total, used, free);


            List<JxStdDatabaseDataDicionary> tableRowNumChange = new ArrayList<>();
            ResultSet ttt = statement.executeQuery(" select  t.table_name,t.num_rows from user_tables t ORDER BY NUM_ROWS DESC   ");
            while (ttt.next()) {
                JxStdDatabaseDataDicionary jxStdDatabaseDataDicionary = new JxStdDatabaseDataDicionary();
                jxStdDatabaseDataDicionary.setTableName(ttt.getString(1) + "");
                jxStdDatabaseDataDicionary.setTableRowSize(ttt.getString(2));
                jxStdDatabaseDataDicionary.setDatabaseId(databaseId);
                tableRowNumChange.add(jxStdDatabaseDataDicionary);
            }

            for (JxStdDatabaseDataDicionary tmp : tableRowNumChange) {
                String tableChangeSql = "   select   to_char(scn_to_timestamp(max(ora_rowscn)),'YYYY-MM-DD HH:MM:SS')    as tableRowChangeTime from    " + tmp.getTableName();
                String tableRowChangeTime = "";
                try {
                    ResultSet tmpResult = statement.executeQuery(tableChangeSql); //获取数据库所有的数据库名
                    while (tmpResult.next()) { //这里必返回一行或者报错
                        tableRowChangeTime = tmpResult.getString(1) + "";
                    }
                    System.out.println(tableChangeSql);
                    System.out.println(tableRowChangeTime);
                } catch (Exception e) {
                    //  e.printStackTrace();
                    tableRowChangeTime = "5天以上未使用";
                }
                tmp.setTableRowChangeTime(tableRowChangeTime);

            }
            if (null != tableRowNumChange && tableRowNumChange.size() > 0) {
                stdDatabaseDataDicionaryMapper.tableRowNumChange(tableRowNumChange);
            }





            Map<String, String> commentTabMap = new HashMap<>();
            ResultSet commentTab = statement.executeQuery("select table_name,comments from all_tab_comments where owner=" + "'" + schema.getUser().toUpperCase() + "'");
            while (commentTab.next()) {
                commentTabMap.put(commentTab.getString("table_name"), commentTab.getString("comments"));
            }

            ResultSet show_databases = statement.executeQuery("select table_name from all_tables where owner=" + "'" + schema.getUser().toUpperCase() + "'" + " ORDER BY TABLE_NAME"); //获取数据库所有的数据库名
            JxStdDatabaseDataDicionary record;
            List<JxStdDatabaseDataDicionary> list = new ArrayList<>();
            List<JxStdDatabaseDataDicionaryColumn> lstDicionaryColumn = new ArrayList<>();
            JxStdDatabaseDataDicionaryColumn dicionaryColumnRecord;
            List<JxStdDatabaseDicionaryLog> stdDatabaseDicionaryLogList = new ArrayList();
            List<JxStdDatabaseQuestionRecord> questionRecords = new ArrayList();
            while (show_databases.next()) {
                record = new JxStdDatabaseDataDicionary();
                record.setId(StringUtils.getUUid());
                record.setTableName(show_databases.getString(1));
                record.setDatabaseId(databaseId);
                if (null != commentTabMap.get(record.getTableName())) {
                    record.setChineseName(commentTabMap.get(record.getTableName()));
                } else {
                    record.setChineseName("");
                }
                list.add(record);
            }
            ResultSet column_remark = statement.executeQuery("select * from all_col_comments col where owner=" + "'" + schema.getUser().toUpperCase() + "'");
            Map<String, String> commentMap = new HashMap<>();//将主键存入map中 将子段为主键的存入数据库
            while (column_remark.next()) {
                commentMap.put(column_remark.getString("TABLE_NAME") + "-" + column_remark.getString("COLUMN_NAME"), column_remark.getString("COMMENTS"));
            }

            ResultSet column_PK = statement.executeQuery("select col.table_name TABLE_NAME,col.column_name COLUMN_NAME from all_constraints con,all_cons_columns col where con.constraint_name = col.constraint_name and con.constraint_type='P' and con.owner = " + "'" + schema.getUser().toUpperCase() + "'");
            Map<String, String> pkMap = new HashMap<>();//将主键存入map中 将子段为主键的存入数据库
            while (column_PK.next()) {
                pkMap.put(column_PK.getString("table_name"), column_PK.getString("column_name"));
            }
            String isPk;
            String tempId;
            String isNull;
            String remark;
            for (JxStdDatabaseDataDicionary stdDatabaseDataDicionary : list) {
                String table = stdDatabaseDataDicionary.getTableName();
                tempId = pkMap.get(table);
                //ResultSet column_data = statement.executeQuery("select column_id,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,data_scale,NULLABLE,DATA_DEFAULT,TABLE_NAME from all_tab_columns where TABLE_NAME=" + "'" + table + "'" + " and owner=" + "'" + schema.getUser().toUpperCase() + "'" + " order by column_id");
                ResultSet column_data = statement.executeQuery("select column_id,COLUMN_NAME,DATA_TYPE,DATA_LENGTH,DATA_PRECISION,data_scale,NULLABLE,DATA_DEFAULT,TABLE_NAME from user_tab_columns where TABLE_NAME=" + "'" + table + "'"   );
                while (column_data.next()) {
                    if (column_data.getString("COLUMN_NAME").equals(tempId)) {
                        isPk = "1";
                    } else {
                        isPk = "0";
                    }
                    if (("Y").equals(column_data.getString("NULLABLE").toUpperCase())) {
                        isNull = "0";
                    } else {
                        isNull = "1";
                    }
                    remark = commentMap.get(table + "-" + column_data.getString("COLUMN_NAME"));
                    if (null == remark) {
                        remark = "";
                    }
                    dicionaryColumnRecord = new JxStdDatabaseDataDicionaryColumn(StringUtils.getUUid(), stdDatabaseDataDicionary.getId(), column_data.getString("COLUMN_NAME"),
                            column_data.getString("DATA_TYPE"), column_data.getInt("DATA_LENGTH"), (null == column_data.getString("DATA_PRECISION") ? "" : column_data.getString("DATA_PRECISION")), isPk,
                            isNull, "", remark, column_data.getString("TABLE_NAME"), databaseId);
                    lstDicionaryColumn.add(dicionaryColumnRecord);

                    JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(),
                            dicionaryColumnRecord.getName(), "创建字段", stdDatabaseDataDicionary.getDatabaseId(), new Date(),
                            dicionaryColumnRecord.getIsEmpty(), dicionaryColumnRecord.getIsPk(), dicionaryColumnRecord.getLength(),
                            dicionaryColumnRecord.getDefaultValue(), dicionaryColumnRecord.getType(), stdDatabaseDataDicionary.getId()
                    );
                    questionRecords.add(questionRecord);
                }

                JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord(StringUtils.getUUid(), stdDatabaseDataDicionary.getTableName(), "", "创建表", stdDatabaseDataDicionary.getDatabaseId(), new Date());
                questionRecords.add(questionRecord);

            }

            JxStdDatabaseQuestionRecord questionRecord = new JxStdDatabaseQuestionRecord();


            if (null != list && list.size() > 0) {

                if (list.size() <= 2000) {//批量新增
                    stdDatabaseDataDicionaryMapper.batchInsert(list);

                } else {
                    int times = (int) Math.ceil(list.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDataDicionaryMapper.batchInsert(list.subList(i * 2000, Math.min((i + 1) * 2000, list.size() - 1)));
                    }
                }
            }
            if (null != lstDicionaryColumn && lstDicionaryColumn.size() > 0) {
                if (lstDicionaryColumn.size() <= 2000) {//批量新增
                    stdDatabaseDataDicColumMapper.batchInsert(lstDicionaryColumn);
                } else {
                    int times = (int) Math.ceil(lstDicionaryColumn.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDataDicColumMapper.batchInsert(lstDicionaryColumn.subList(i * 2000, Math.min((i + 1) * 2000, lstDicionaryColumn.size() - 1)));
                    }
                }
            }
            if (null != stdDatabaseDicionaryLogList && stdDatabaseDicionaryLogList.size() > 0) {
                if (stdDatabaseDicionaryLogList.size() <= 2000) {//批量新增
                    stdDatabaseDicionaryLogMapper.batchInsert(stdDatabaseDicionaryLogList);
                } else {
                    int times = (int) Math.ceil(stdDatabaseDicionaryLogList.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseDicionaryLogMapper.batchInsert(stdDatabaseDicionaryLogList.subList(i * 2000, Math.min((i + 1) * 2000, stdDatabaseDicionaryLogList.size() - 1)));
                    }
                }
            }


            for (JxStdDatabaseQuestionRecord jxStdDatabaseQuestionRecord : questionRecords) {
                jxStdDatabaseQuestionRecord.setScanId(scanInfoId);
                jxStdDatabaseQuestionRecord.setStatus("-1");
            }


            if (null != questionRecords && questionRecords.size() > 0) {

                questionRecords = this.transJxStdDatabaseQuestionRecord(questionRecords);

                if (questionRecords.size() <= 2000) {
                    stdDatabaseQuestionRecordMapper.batchInsert(questionRecords);
                } else {
                    int times = (int) Math.ceil(questionRecords.size() / 2000.0);
                    for (int i = 0; i < times; i++) {
                        stdDatabaseQuestionRecordMapper.batchInsert(questionRecords.subList(i * 2000, Math.min((i + 1) * 2000, questionRecords.size() - 1)));
                    }
                }
            }

            connection.close();

           jxStdScanInfo = new JxStdScanInfo(scanInfoId, new Date(), "", "", questionRecords.size(),jxStdScanInfoMapper.selcountByDbId(databaseId)+1,
                    0,list.size(),0,0,
                    questionRecords.size()-list.size(),0,databaseId,"1",null);

        } catch (Exception e) {

            e.printStackTrace();
            jxStdScanInfo = new JxStdScanInfo(scanInfoId, databaseId, "0", e.getMessage().toString().length() < 250 ? e.getMessage().toString() : e.getMessage().toString().substring(0, 250));


        }finally {

            jxStdScanInfoMapper.insert(jxStdScanInfo);

        }
    }

    public List<JxStdDatabaseQuestionRecord> transJxStdDatabaseQuestionRecord(List<JxStdDatabaseQuestionRecord> questionRecords) {
        for (JxStdDatabaseQuestionRecord tmpQuestionRecord : questionRecords) {
            if (tmpQuestionRecord.getKeyword() == null) {
                tmpQuestionRecord.setKeyword("");
            }
            if (tmpQuestionRecord.getDefaultValue() == null) {
                tmpQuestionRecord.setDefaultValue("");
            }
            if (tmpQuestionRecord.getIsEmpty() == null) {
                tmpQuestionRecord.setIsEmpty("");
            }
            if (tmpQuestionRecord.getIsPk() == null) {
                tmpQuestionRecord.setIsPk("");
            }
            if (tmpQuestionRecord.getLength() == null) {
                tmpQuestionRecord.setLength(0);
            }
            if (tmpQuestionRecord.getDefaultValue() == null) {
                tmpQuestionRecord.setDefaultValue("");
            }
            if (tmpQuestionRecord.getColumnType() == null) {
                tmpQuestionRecord.setColumnType("");
            }
            if (tmpQuestionRecord.getTableId() == null) {
                tmpQuestionRecord.setTableId("");
            }
            if (tmpQuestionRecord.getDefaultValue() == null) {
                tmpQuestionRecord.setDefaultValue("");
            }


        }
        return questionRecords;
    }

    public List<JxStdDatabaseDicionaryLog> transJxStdDatabaseDicionaryLog(List<JxStdDatabaseDicionaryLog> dicionaryLogRecords) {
        for (JxStdDatabaseDicionaryLog databaseDicionaryLog : dicionaryLogRecords) {
            if (databaseDicionaryLog.getOldColumnName() == null) {
                databaseDicionaryLog.setOldColumnName("");
            }
            if (databaseDicionaryLog.getColumnName() == null) {
                databaseDicionaryLog.setColumnName("");
            }


        }
        return dicionaryLogRecords;
    }
}
