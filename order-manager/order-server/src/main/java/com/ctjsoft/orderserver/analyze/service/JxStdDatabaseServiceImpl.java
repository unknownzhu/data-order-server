/*
package com.ctjsoft.orderserver.analyze.service;


import com.ctjsoft.orderserver.analyze.common.SchedulerQuartz;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabase;
import com.ctjsoft.orderserver.analyze.domain.JxStdDatabaseExtendRelationVo;
import com.ctjsoft.orderserver.analyze.mapper.*;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

*/
/**
 * @author zyx
 * @date 2020/11/26
 *//*

@Service
public class JxStdDatabaseServiceImpl {
    @Autowired
    private JxStdDatabaseMapper stdDatabaseMapper;
    @Autowired
    private JxStdDatabaseMapper jxStdDatabaseMapper;
    @Autowired
    private StdCompanyMapper stdCompanyMapper;
    @Autowired
    private StdDatabaseRelationMapper stdDatabaseRelationMapper;
    @Autowired
    private JxStdDatabaseQuestionRecordMapper stdDatabaseQuestionRecordMapper;
    @Autowired
    private JxStdDatabaseDataDicCRecordMapper stdDatabaseDataDicCRecordMapper;
    @Autowired
    private JxStdDatabaseDataDicionaryMapper stdDatabaseDataDicionaryMapper;
    @Autowired
    SchedulerQuartz schedulerQuartz;


    public boolean test(String driver, String url, String username, String pwd) {
        try {
//            log.info("线程【{}】开始测试数据库连接性",Thread.currentThread().getName());
//            long start = System.currentTimeMillis();
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(driver);
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(pwd);

            HikariDataSource dataSource = new HikariDataSource(hikariConfig);
//            long end1 = System.currentTimeMillis();
//            log.info("线程【{}】,HikariDataSource耗时-->{}", Thread.currentThread().getName(), (end1 - start));
//
//
//            D.setDbConfig(url,username,pwd,driver);
//            D.getConnection();
//
//            long end2 = System.currentTimeMillis();
//            log.info("线程【{}】,DruidDataSource耗时-->{}", Thread.currentThread().getName(), (end2 - end1));
            return dataSource.isRunning();
        } catch (Exception e) {
            e.printStackTrace();
         //   throw new ScException("数据库连接异常");
            throw null;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public Result save(DatabaseSaveReqDto databaseSaveReqDto) {
        Result returnData = new Result();
        List<String> nameList = new ArrayList<>();
        if (databaseSaveReqDto.getId() == null) {
            nameList = this.jxStdDatabaseMapper.selectName();
        } else {
            nameList = this.jxStdDatabaseMapper.selectNameRemoveSelf(databaseSaveReqDto.getId());
        }

        if (nameList.contains(databaseSaveReqDto.getName().trim())) {
            returnData.setCode(500);
            returnData.setMsg("存在名称重复");
            return returnData;
        }
        databaseSaveReqDto.setName(databaseSaveReqDto.getName().trim());
        JxStdDatabase stdDatabase = new JxStdDatabase();
        BeanUtils.copyProperties(databaseSaveReqDto, stdDatabase);
        //2020.10.12 新增字段 modelName 默认和用户名一样，但是需要大写
        if (stdDatabase.getModelName() == null || "".equals(stdDatabase.getModelName())) {
            stdDatabase.setModelName(stdDatabase.getUsername().toUpperCase());
        } else {
            stdDatabase.setModelName(stdDatabase.getModelName().toUpperCase());
        }
        String version = getVersion(databaseSaveReqDto.getDriver(), databaseSaveReqDto.getUrl(), databaseSaveReqDto.getUsername(), databaseSaveReqDto.getPwd());
        stdDatabase.setVersion(version);//获取版本
        if (databaseSaveReqDto.getId() == null) {
            // insert
            stdDatabase.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            stdDatabase.setType(1);//默认使用生产数据库
//            stdDatabase.setCode(ScUtil.getUUID());
            this.stdDatabaseMapper.insert(stdDatabase);
            String companyCode = databaseSaveReqDto.getCompanyCode();
            StdDatabaseRelation stdDatabaseRelation = new StdDatabaseRelation();
            stdDatabaseRelation.setId(UUID.randomUUID().toString().replaceAll("\\-", ""));
            //stdDatabaseRelation.setBusinessId("");
            stdDatabaseRelation.setDatabaseId(stdDatabase.getId());
            stdDatabaseRelation.setCompanyCode(companyCode);
            //stdDatabaseRelation.setSystemCode(systemCode);
            this.stdDatabaseRelationMapper.insert(stdDatabaseRelation);
            //insertDataDicionary(stdDatabase.getDriver(), stdDatabase.getUrl(), stdDatabase.getUsername(), stdDatabase.getPwd(), stdDatabase.getId());
        } else {
            // update
            this.stdDatabaseMapper.updateByPrimaryKeySelective(stdDatabase);
            //this.stdDatabaseRelationMapper.deleteByDatabaseId(stdDatabase.getId());
        }

        returnData.setCode(200);
        returnData.setReason("保存成功");
        return returnData;
    }

    public DatabaseDetailRespDto detail(String id) {
        JxStdDatabase stdDatabase = this.stdDatabaseMapper.selectByPrimaryKey(id);
        if (stdDatabase == null) {
            //throw new ScException("没有该ID对应的详情信息，ID：%s", id);
        }

        DatabaseDetailRespDto result = new DatabaseDetailRespDto();
        BeanUtils.copyProperties(stdDatabase, result);
        return result;
    }

    private List<List<String>> getBusinessLevel(List<BaseTreeDto> businessTrees,
                                                Map<Long, List<StdBusiness>> businessMap, StdBusiness secendBusiness) {
        List<StdBusiness> businessList = businessMap.get(secendBusiness.getBusinessId());

        BaseTreeDto parentBaseTreeDto = new BaseTreeDto();
        parentBaseTreeDto.setId(secendBusiness.getBusinessId().toString());
        parentBaseTreeDto.setLabel(secendBusiness.getBusinessName());
        parentBaseTreeDto.setLast(CollectionUtils.isEmpty(businessList));
        parentBaseTreeDto.setParentId(secendBusiness.getParentId().toString());
        businessTrees.add(parentBaseTreeDto);

        if (CollectionUtils.isEmpty(businessList)) {
            List<List<String>> result = new ArrayList<>();
            result.add(new ArrayList(Arrays.asList(secendBusiness.getBusinessId().toString())));
            return result;
        }
        List<List<String>> all = new ArrayList<>();
        List<BaseTreeDto> childTrees = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(businessList)) {
            for (StdBusiness children : businessList) {
                List<List<String>> relations = getBusinessLevel(childTrees, businessMap, children);
                for (List<String> relation : relations) {
                    relation.add(0, secendBusiness.getBusinessId().toString());
                }
                all.addAll(relations);
            }
        }

        parentBaseTreeDto.setChildren(childTrees);
        return all;
    }

    public StdEvaluationDto tree(String search) {
        //管理员可以查看所有数据
        List<StdCompany> stdCompanyList = stdCompanyMapper.getAllComList();
        List<String> companyCodeList = stdCompanyList.stream().map(StdCompany::getCompanyCode).collect(Collectors.toList());
        List<JxStdDatabaseExtendRelationVo> stdDatabaseList = this.jxStdDatabaseMapper.search(search, companyCodeList);
        Map<String, List<JxStdDatabaseExtendRelationVo>> stdDatabaseMap = stdDatabaseList.stream().collect(Collectors.groupingBy(JxStdDatabaseExtendRelationVo::getCompanyCode));
        DatabaseTypeEnum[] databaseTypeEnums = DatabaseTypeEnum.values();
        List<BaseTreeDto> result = new ArrayList<>(databaseTypeEnums.length);
        for (int i = 0; i < stdCompanyList.size(); i++) {

            List<JxStdDatabaseExtendRelationVo> stdList = stdDatabaseMap.get(stdCompanyList.get(i).getCompanyCode());
            List<BaseTreeDto> childList = new ArrayList<BaseTreeDto>();
            if (!CollectionUtils.isEmpty(stdList)) {
                for (JxStdDatabaseExtendRelationVo stdDatabase : stdList) {
                    JxBaseTreeDto childTree = new JxBaseTreeDto();
                    childTree.setId(stdDatabase.getId());
                    childTree.setLabel(stdDatabase.getCode() + " " + stdDatabase.getName());
                    childTree.setText(stdDatabase.getCode() + " " + stdDatabase.getName());
                    childTree.setLast(true);
                    childTree.setParentCode(stdCompanyList.get(i).getCompanyCode());
                    childTree.setParentId(stdCompanyList.get(i).getCompanyCode());
                    DatabaseDetailRespDto dataDto = new DatabaseDetailRespDto();
                    BeanUtil.copyProperties(stdDatabase, dataDto);
                    dataDto.setTypeName(databaseTypeEnums[0].getName());
                    dataDto.splitUrl();
               */
/*     StringBuilder sb = new StringBuilder();
                    StdDatabaseRelation stdDatabaseRelation = stdDatabaseRelationMapper.selectByPrimaryKey(stdDatabase.getId());
                    sb.append(stdDatabaseRelation.getBusinessId()).append(",");
                    dataDto.setBussinessRelation(sb.substring(0, sb.length() - 1));*//*

                    childTree.setBasicData(dataDto);
                    childTree.setDataType(stdCompanyList.get(i).getCompanyCode());
                    childList.add(childTree);
                }

            }
            JxBaseTreeDto parentTree = new JxBaseTreeDto();
            parentTree.setId(stdCompanyList.get(i).getCompanyCode());
            parentTree.setLabel(stdCompanyList.get(i).getCompanyName());
            parentTree.setText(stdCompanyList.get(i).getCompanyName());
            parentTree.setLast(childList.isEmpty());
            parentTree.setChildren(childList);
            parentTree.setParentCode("0");
            parentTree.setParentId("0");
            result.add(parentTree);
        }

        StdEvaluationDto stdEvaluationDto = new StdEvaluationDto();
        stdEvaluationDto.setDataBaseSourceList(result);
        return stdEvaluationDto;
    }

    public List<BaseTreeDto> loadtree(String search) {
        List<JxStdDatabase> stdDatabaseList = this.stdDatabaseMapper.loadSearch(search);
        Map<String, List<JxStdDatabase>> stdDatabaseMap = stdDatabaseList.stream().collect(Collectors.groupingBy(JxStdDatabase::getCompanyCode));

        Map<String, Integer> stdDatabaseDicMap = getDataDicCountMap();
        DatabaseTypeEnum[] databaseTypeEnums = DatabaseTypeEnum.values();
        List<BaseTreeDto> result = new ArrayList<>(databaseTypeEnums.length);

        List<StdCompany> lstCompany = stdCompanyMapper.getAllComList();

        //List<StdDatabaseQuestionRecord> stdDatabaseQRrcord = this.stdDatabaseQuestionRecordMapper.list();
        Map<String, Integer> stdDatabaseQRrcordMap = getDataCRecordMap();

        //List<StdDatabaseDataDicChangeRecord> stdDatabaseDCRrcord = this.stdDatabaseDataDicCRecordMapper.list();
        Map<String, Integer> stdDatabaseDCRrcordMap = getDataQRecordMap();

        ProduceManageData produreManageData;
        Integer tempStdDatabaseDiCount;
        Integer tempStdDatabaseQRecordCount;
        Integer tempStdDatabaseDCRecordCount;
        for (int i = 0; i < lstCompany.size(); i++) {
            //DatabaseTypeEnum databaseTypeEnum = databaseTypeEnums[i];
            List<JxStdDatabase> stdDatabases = stdDatabaseMap.get(lstCompany.get(i).getCompanyCode());

            List<BaseTreeDto> childen = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(stdDatabases)) {
                for (JxStdDatabase stdDatabase : stdDatabases) {
                    BaseTreeDto baseTreeDto = new BaseTreeDto();
                    baseTreeDto.setId(stdDatabase.getId());
                    baseTreeDto.setLabel(stdDatabase.getName());
                    //baseTreeDto.setParentId(lstCompany.get(i).getCompanyCode().toString());
                    baseTreeDto.setParentCode(lstCompany.get(i).getCompanyCode());
                    produreManageData = new ProduceManageData();
                    produreManageData.setCode(stdDatabase.getCode());
                    produreManageData.setDriver(stdDatabase.getDriver());
                    produreManageData.setUrl(stdDatabase.getUrl());
                    produreManageData.setUsername(stdDatabase.getUsername());
                    produreManageData.setPwd(stdDatabase.getPwd());
                    produreManageData.setDes(stdDatabase.getDes());
                    produreManageData.setRate(stdDatabase.getRate());
                    tempStdDatabaseDiCount = stdDatabaseDicMap.get(stdDatabase.getId());
                    tempStdDatabaseQRecordCount = stdDatabaseQRrcordMap.get(stdDatabase.getId());
                    tempStdDatabaseDCRecordCount = stdDatabaseDCRrcordMap.get(stdDatabase.getId());

                    if (null == tempStdDatabaseDiCount) {
                        produreManageData.setTableNum(0);
                    } else {
                        produreManageData.setTableNum(tempStdDatabaseDiCount);
                    }
                    if (null == tempStdDatabaseQRecordCount) {
                        produreManageData.setCheckNum(0);
                    } else {
                        produreManageData.setCheckNum(tempStdDatabaseQRecordCount);
                    }
                    if (null == tempStdDatabaseDCRecordCount) {
                        produreManageData.setProcessNum(0);
                    } else {
                        produreManageData.setProcessNum(tempStdDatabaseDCRecordCount);
                    }
                    //produreManageData.setVersion("1.0.0"); //默认版本
                    if (null == stdDatabase.getVersion()) {
                        produreManageData.setVersion(""); //不默认版本
                    } else {
                        produreManageData.setVersion(stdDatabase.getVersion()); //不默认版本
                    }

                    baseTreeDto.setBasicData(produreManageData);
                    childen.add(baseTreeDto);
                }
            }

            BaseTreeDto parent = new BaseTreeDto();
            parent.setId(lstCompany.get(i).getCompanyCode());
            parent.setLabel(lstCompany.get(i).getCompanyName());
            parent.setChildren(childen);
            result.add(parent);
        }

        return result;
    }

    public String getVersion(String driver, String url, String username, String pwd) {
        try {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(driver);
            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(pwd);

            HikariDataSource dataSource = new HikariDataSource(hikariConfig);
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet show_databases = statement.executeQuery("select version from v$instance");
            while (show_databases.next()) {
                String version = show_databases.getString(1);
                if (StringUtils.isNotEmpty(version)) {
                    return show_databases.getString(1);
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            //throw new ScException("数据库连接异常");
        }
        return null;
    }

    private Map<String, Integer> getDataDicCountMap() {
        List<HashMap<String, Object>> list = this.stdDatabaseDataDicionaryMapper.listByGroupCount();
        return listToMap(list);
    }

    private Map<String, Integer> getDataCRecordMap() {
        List<HashMap<String, Object>> list = this.stdDatabaseDataDicCRecordMapper.listByGroupCount();
        return listToMap(list);
    }

    private Map<String, Integer> getDataQRecordMap() {
        List<HashMap<String, Object>> list = this.stdDatabaseQuestionRecordMapper.listByGroupCount();
        return listToMap(list);
    }

    private Map<String, Integer> listToMap(List<HashMap<String, Object>> list) { //list转map
        Map<String, Integer> map = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (HashMap<String, Object> map1 : list) {
                String key = null;
                Integer value = 0;
                for (Map.Entry<String, Object> entry : map1.entrySet()) {
                    if ("KEY".equalsIgnoreCase(entry.getKey())) {
                        key = (String) entry.getValue();
                    } else if ("VALUE".equalsIgnoreCase(entry.getKey())) {
                        value = Integer.parseInt((String.valueOf(entry.getValue())));
                    }
                }
                map.put(key, value);
            }
        }
        return map;
    }
}
*/
