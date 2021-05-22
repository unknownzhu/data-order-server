package com.ctjsoft.orderserver.order.service.impl;


import java.util.Date;
import java.util.List;

import com.ctjsoft.orderserver.order.domain.OrderSqlLog;
import com.ctjsoft.orderserver.order.mapper.OrderSqlLogMapper;
import com.ctjsoft.orderserver.order.service.IOrderSqlLogService;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 语句执行日志Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Service
public class OrderSqlLogServiceImpl implements IOrderSqlLogService
{
    @Autowired
    private OrderSqlLogMapper orderSqlLogMapper;

    /**
     * 查询语句执行日志
     * 
     * @param id 语句执行日志ID
     * @return 语句执行日志
     */
    @Override
    public OrderSqlLog selectOrderSqlLogById(String id)
    {
        return orderSqlLogMapper.selectOrderSqlLogById(id);
    }

    /**
     * 查询语句执行日志列表
     * 
     * @param orderSqlLog 语句执行日志
     * @return 语句执行日志
     */
    @Override
    public List<OrderSqlLog> selectOrderSqlLogList(OrderSqlLog orderSqlLog)
    {
        return orderSqlLogMapper.selectOrderSqlLogList(orderSqlLog);
    }

    /**
     * 查询语句执行日志
     * @param orderSqlLog 语句执行日志
     * @param pageDomain
     * @return 语句执行日志 分页集合
     * */
    @Override
    public PageInfo<OrderSqlLog> selectOrderSqlLogPage(OrderSqlLog orderSqlLog, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<OrderSqlLog> data = orderSqlLogMapper.selectOrderSqlLogList(orderSqlLog);
        return new PageInfo<>(data);
    }

    /**
     * 新增语句执行日志
     * 
     * @param orderSqlLog 语句执行日志
     * @return 结果
     */

    @Override
    public int insertOrderSqlLog(OrderSqlLog orderSqlLog)
    {
        orderSqlLog.setId(StringUtils.getUUid());
        orderSqlLog.setCreateTime(new Date());
        return orderSqlLogMapper.insertOrderSqlLog(orderSqlLog);
    }

    /**
     * 修改语句执行日志
     * 
     * @param orderSqlLog 语句执行日志
     * @return 结果
     */
    @Override
    public int updateOrderSqlLog(OrderSqlLog orderSqlLog)
    {
        return orderSqlLogMapper.updateOrderSqlLog(orderSqlLog);
    }

    /**
     * 删除语句执行日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOrderSqlLogByIds(String[] ids)
    {
        return orderSqlLogMapper.deleteOrderSqlLogByIds(ids);
    }

    /**
     * 删除语句执行日志信息
     * 
     * @param id 语句执行日志ID
     * @return 结果
     */
    @Override
    public int deleteOrderSqlLogById(String id)
    {
        return orderSqlLogMapper.deleteOrderSqlLogById(id);
    }
}
