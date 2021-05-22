package com.ctjsoft.orderserver.order.mapper;

import java.util.List;

import com.ctjsoft.orderserver.order.domain.OrderReviewLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 送审日志Mapper接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Mapper
public interface OrderReviewLogMapper 
{
    /**
     * 查询送审日志
     * 
     * @param id 送审日志ID
     * @return 送审日志
     */
    public OrderReviewLog selectOrderReviewLogById(String id);

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
     * 删除送审日志
     * 
     * @param id 送审日志ID
     * @return 结果
     */
    int deleteOrderReviewLogById(String id);

    /**
     * 批量删除送审日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOrderReviewLogByIds(String[] ids);

}
