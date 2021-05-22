package com.ctjsoft.orderserver.order.mapper;

import java.util.List;

import com.ctjsoft.orderserver.order.domain.CommonSqlType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 语句类型Mapper接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Mapper
public interface CommonSqlTypeMapper 
{
    /**
     * 查询语句类型
     * 
     * @param id 语句类型ID
     * @return 语句类型
     */
    public CommonSqlType selectCommonSqlTypeById(String id);

    /**
     * 查询语句类型列表
     * 
     * @param commonSqlType 语句类型
     * @return 语句类型集合
     */
    List<CommonSqlType> selectCommonSqlTypeList(CommonSqlType commonSqlType);

    /**
     * 新增语句类型
     * 
     * @param commonSqlType 语句类型
     * @return 结果
     */
    int insertCommonSqlType(CommonSqlType commonSqlType);

    /**
     * 修改语句类型
     * 
     * @param commonSqlType 语句类型
     * @return 结果
     */
    int updateCommonSqlType(CommonSqlType commonSqlType);

    /**
     * 删除语句类型
     * 
     * @param id 语句类型ID
     * @return 结果
     */
    int deleteCommonSqlTypeById(String id);

    /**
     * 批量删除语句类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCommonSqlTypeByIds(String[] ids);

}
