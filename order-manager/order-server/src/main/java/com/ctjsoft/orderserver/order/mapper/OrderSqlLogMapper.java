package com.ctjsoft.orderserver.order.mapper;

import java.util.List;

import com.ctjsoft.orderserver.order.domain.OrderSqlLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 语句执行日志Mapper接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Mapper
public interface OrderSqlLogMapper 
{
    /**
     * 查询语句执行日志
     * 
     * @param id 语句执行日志ID
     * @return 语句执行日志
     */
    public OrderSqlLog selectOrderSqlLogById(String id);

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
     * 删除语句执行日志
     * 
     * @param id 语句执行日志ID
     * @return 结果
     */
    int deleteOrderSqlLogById(String id);

    /**
     * 批量删除语句执行日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOrderSqlLogByIds(String[] ids);

}
