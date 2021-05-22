package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.HistorySql;
import com.ctjsoft.orderserver.order.domain.HistorySqlVo;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 常用语句Service接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
public interface IHistorySqlService 
{
    /**
     * 查询常用语句
     * 
     * @param id 常用语句ID
     * @return 常用语句
     */
    HistorySql selectHistorySqlById(String id);


    /**
    * 查询常用语句
     * @param ${classsName} 常用语句
     * @param pageDomain
     * @return 常用语句 分页集合
     * */
    PageInfo<HistorySql> selectHistorySqlPage(HistorySql historySql, PageDomain pageDomain);
    PageInfo<HistorySqlVo> selectCommonVoSqlPage(HistorySql historySql, PageDomain pageDomain);

    /**
     * 查询常用语句列表
     * 
     * @param historySql 常用语句
     * @return 常用语句集合
     */
    List<HistorySql> selectHistorySqlList(HistorySql historySql);

    /**
     * 新增常用语句
     * 
     * @param historySql 常用语句
     * @return 结果
     */
    int insertHistorySql(HistorySql historySql);

    /**
     * 修改常用语句
     * 
     * @param historySql 常用语句
     * @return 结果
     */
    int updateHistorySql(HistorySql historySql);

    /**
     * 批量删除常用语句
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteHistorySqlByIds(String[] ids);

    /**
     * 删除常用语句信息
     * 
     * @param id 常用语句ID
     * @return 结果
     */
    int deleteHistorySqlById(String id);

}
