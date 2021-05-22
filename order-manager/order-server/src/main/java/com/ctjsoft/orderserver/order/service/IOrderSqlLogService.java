package com.ctjsoft.orderserver.order.service;


import java.util.List;

import com.ctjsoft.orderserver.order.domain.OrderSqlLog;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 语句执行日志Service接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
public interface IOrderSqlLogService 
{
    /**
     * 查询语句执行日志
     * 
     * @param id 语句执行日志ID
     * @return 语句执行日志
     */
    OrderSqlLog selectOrderSqlLogById(String id);


    /**
    * 查询语句执行日志
     * @param ${classsName} 语句执行日志
     * @param pageDomain
     * @return 语句执行日志 分页集合
     * */
    PageInfo<OrderSqlLog> selectOrderSqlLogPage(OrderSqlLog orderSqlLog, PageDomain pageDomain);

    /**
     * 查询语句执行日志列表
     * 
     * @param orderSqlLog 语句执行日志
     * @return 语句执行日志集合
     */
    List<OrderSqlLog> selectOrderSqlLogList(OrderSqlLog orderSqlLog);

    /**
     * 新增语句执行日志
     * 
     * @param orderSqlLog 语句执行日志
     * @return 结果
     */
    int insertOrderSqlLog(OrderSqlLog orderSqlLog);

    /**
     * 修改语句执行日志
     * 
     * @param orderSqlLog 语句执行日志
     * @return 结果
     */
    int updateOrderSqlLog(OrderSqlLog orderSqlLog);

    /**
     * 批量删除语句执行日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOrderSqlLogByIds(String[] ids);

    /**
     * 删除语句执行日志信息
     * 
     * @param id 语句执行日志ID
     * @return 结果
     */
    int deleteOrderSqlLogById(String id);

}
