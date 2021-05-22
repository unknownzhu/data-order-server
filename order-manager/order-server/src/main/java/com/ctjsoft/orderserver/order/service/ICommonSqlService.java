package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.CommonSql;
import com.ctjsoft.orderserver.order.domain.CommonSqlVo;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 常用语句Service接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
public interface ICommonSqlService 
{
    /**
     * 查询常用语句
     * 
     * @param id 常用语句ID
     * @return 常用语句
     */
    CommonSql selectCommonSqlById(String id);


    /**
    * 查询常用语句
     * @param ${classsName} 常用语句
     * @param pageDomain
     * @return 常用语句 分页集合
     * */
    PageInfo<CommonSql> selectCommonSqlPage(CommonSql commonSql, PageDomain pageDomain);
    PageInfo<CommonSqlVo> selectCommonVoSqlPage(CommonSql commonSql, PageDomain pageDomain);

    /**
     * 查询常用语句列表
     * 
     * @param commonSql 常用语句
     * @return 常用语句集合
     */
    List<CommonSql> selectCommonSqlList(CommonSql commonSql);

    /**
     * 新增常用语句
     * 
     * @param commonSql 常用语句
     * @return 结果
     */
    int insertCommonSql(CommonSql commonSql);

    int insertFronHistorySql(String historySqlId ,String showType);

    /**
     * 修改常用语句
     * 
     * @param commonSql 常用语句
     * @return 结果
     */
    int updateCommonSql(CommonSql commonSql);

    /**
     * 批量删除常用语句
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCommonSqlByIds(String[] ids);

    /**
     * 删除常用语句信息
     * 
     * @param id 常用语句ID
     * @return 结果
     */
    int deleteCommonSqlById(String id);

}
