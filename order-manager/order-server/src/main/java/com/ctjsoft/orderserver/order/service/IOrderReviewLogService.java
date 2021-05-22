package com.ctjsoft.orderserver.order.service;


import java.util.List;

import com.ctjsoft.orderserver.order.domain.OrderReviewLog;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 送审日志Service接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
public interface IOrderReviewLogService 
{
    /**
     * 查询送审日志
     * 
     * @param id 送审日志ID
     * @return 送审日志
     */
    OrderReviewLog selectOrderReviewLogById(String id);


    /**
    * 查询送审日志
     * @param ${classsName} 送审日志
     * @param pageDomain
     * @return 送审日志 分页集合
     * */
    PageInfo<OrderReviewLog> selectOrderReviewLogPage(OrderReviewLog orderReviewLog, PageDomain pageDomain);

    /**
     * 查询送审日志列表
     * 
     * @param orderReviewLog 送审日志
     * @return 送审日志集合
     */
    List<OrderReviewLog> selectOrderReviewLogList(OrderReviewLog orderReviewLog);

    /**
     * 新增送审日志
     * 
     * @param orderReviewLog 送审日志
     * @return 结果
     */
    int insertOrderReviewLog(OrderReviewLog orderReviewLog);

    /**
     * 修改送审日志
     * 
     * @param orderReviewLog 送审日志
     * @return 结果
     */
    int updateOrderReviewLog(OrderReviewLog orderReviewLog);

    /**
     * 批量删除送审日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOrderReviewLogByIds(String[] ids);

    /**
     * 删除送审日志信息
     * 
     * @param id 送审日志ID
     * @return 结果
     */
    int deleteOrderReviewLogById(String id);

}
