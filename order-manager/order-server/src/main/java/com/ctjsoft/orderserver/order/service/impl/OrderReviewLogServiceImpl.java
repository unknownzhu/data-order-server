package com.ctjsoft.orderserver.order.service.impl;

import java.util.List;

import com.ctjsoft.orderserver.order.domain.OrderReviewLog;
import com.ctjsoft.orderserver.order.mapper.OrderReviewLogMapper;
import com.ctjsoft.orderserver.order.service.IOrderReviewLogService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 送审日志Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Service
public class OrderReviewLogServiceImpl implements IOrderReviewLogService
{
    @Autowired
    private OrderReviewLogMapper orderReviewLogMapper;

    /**
     * 查询送审日志
     * 
     * @param id 送审日志ID
     * @return 送审日志
     */
    @Override
    public OrderReviewLog selectOrderReviewLogById(String id)
    {
        return orderReviewLogMapper.selectOrderReviewLogById(id);
    }

    /**
     * 查询送审日志列表
     * 
     * @param orderReviewLog 送审日志
     * @return 送审日志
     */
    @Override
    public List<OrderReviewLog> selectOrderReviewLogList(OrderReviewLog orderReviewLog)
    {
        return orderReviewLogMapper.selectOrderReviewLogList(orderReviewLog);
    }

    /**
     * 查询送审日志
     * @param orderReviewLog 送审日志
     * @param pageDomain
     * @return 送审日志 分页集合
     * */
    @Override
    public PageInfo<OrderReviewLog> selectOrderReviewLogPage(OrderReviewLog orderReviewLog, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<OrderReviewLog> data = orderReviewLogMapper.selectOrderReviewLogList(orderReviewLog);
        return new PageInfo<>(data);
    }

    /**
     * 新增送审日志
     * 
     * @param orderReviewLog 送审日志
     * @return 结果
     */

    @Override
    public int insertOrderReviewLog(OrderReviewLog orderReviewLog)
    {
        return orderReviewLogMapper.insertOrderReviewLog(orderReviewLog);
    }

    /**
     * 修改送审日志
     * 
     * @param orderReviewLog 送审日志
     * @return 结果
     */
    @Override
    public int updateOrderReviewLog(OrderReviewLog orderReviewLog)
    {
        return orderReviewLogMapper.updateOrderReviewLog(orderReviewLog);
    }

    /**
     * 删除送审日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOrderReviewLogByIds(String[] ids)
    {
        return orderReviewLogMapper.deleteOrderReviewLogByIds(ids);
    }

    /**
     * 删除送审日志信息
     * 
     * @param id 送审日志ID
     * @return 结果
     */
    @Override
    public int deleteOrderReviewLogById(String id)
    {
        return orderReviewLogMapper.deleteOrderReviewLogById(id);
    }
}
