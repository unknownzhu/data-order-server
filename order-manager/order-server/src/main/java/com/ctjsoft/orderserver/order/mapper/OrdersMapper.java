package com.ctjsoft.orderserver.order.mapper;


import com.ctjsoft.orderserver.order.domain.CronJobLog;
import com.ctjsoft.orderserver.order.domain.Orders;
import com.ctjsoft.orderserver.order.domain.OrdersJobVo;
import com.ctjsoft.orderserver.order.domain.OrdersVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 工单管理Mapper接口
 * 
 * @author zzz
 * @date 2021-03-10
 */
@Repository
public interface OrdersMapper 
{
    /**
     * 查询工单管理
     * 
     * @param id 工单管理ID
     * @return 工单管理
     */
    public Orders  selectOrdersVo(Long id);


    public Orders selectOrdersById(String id);

    /**
     * 查询工单管理列表
     * 
     * @param orders 工单管理
     * @return 工单管理集合
     */

    OrdersJobVo linkSelectCronJobLogList(OrdersJobVo ordersJobVo);



    List<Orders> selectOrdersList(Orders orders);
    List<OrdersVo> selectOrdersLinkList(OrdersVo orders);

    OrdersVo selectOneOrdersLinkDetail(OrdersVo orders);

    /**
     * 新增工单管理
     * 
     * @param orders 工单管理
     * @return 结果
     */
    int insertOrders(Orders orders);

    /**
     * 修改工单管理
     * 
     * @param orders 工单管理
     * @return 结果
     */
    int updateOrders(Orders orders);

    /**
     * 删除工单管理
     * 
     * @param id 工单管理ID
     * @return 结果
     */
    int deleteOrdersById(String id);

    /**
     * 批量删除工单管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOrdersByIds(String[] ids);

}
