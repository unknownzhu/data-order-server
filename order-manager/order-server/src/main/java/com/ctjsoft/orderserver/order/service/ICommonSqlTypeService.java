package com.ctjsoft.orderserver.order.service;

import java.util.List;

import com.ctjsoft.orderserver.order.domain.CommonSqlType;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 语句类型Service接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
public interface ICommonSqlTypeService 
{
    /**
     * 查询语句类型
     * 
     * @param id 语句类型ID
     * @return 语句类型
     */
    CommonSqlType selectCommonSqlTypeById(String id);


    /**
    * 查询语句类型
     * @param ${classsName} 语句类型
     * @param pageDomain
     * @return 语句类型 分页集合
     * */
    PageInfo<CommonSqlType> selectCommonSqlTypePage(CommonSqlType commonSqlType, PageDomain pageDomain);

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
     * 批量删除语句类型
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCommonSqlTypeByIds(String[] ids);

    /**
     * 删除语句类型信息
     * 
     * @param id 语句类型ID
     * @return 结果
     */
    int deleteCommonSqlTypeById(String id);

}
