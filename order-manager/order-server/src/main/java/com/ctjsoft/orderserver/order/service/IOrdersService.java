package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.Orders;
import com.ctjsoft.orderserver.order.domain.OrdersJobVo;
import com.ctjsoft.orderserver.order.domain.OrdersVo;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 工单管理Service接口
 * 
 * @author zzz
 * @date 2021-03-10
 */
public interface IOrdersService 
{
    /**
     * 查询工单管理
     * 
     * @param id 工单管理ID
     * @return 工单管理
     */
    Orders selectOrdersById(String id);

    OrdersJobVo linkSelectCronJobLogList(OrdersJobVo ordersJobVo);

    /**
    * 查询工单管理
     * @param ${classsName} 工单管理
     * @param pageDomain
     * @return 工单管理 分页集合
     * */
    PageInfo<Orders> selectOrdersPage(Orders orders, PageDomain pageDomain);
    PageInfo<OrdersVo> selectOrdersLinkPage(OrdersVo orders, PageDomain pageDomain,String flowRightType);

    OrdersVo linkDetail(OrdersVo orders);

    OrdersVo selectOneOrdersLinkDetail(OrdersVo orders);
    /**
     * 查询工单管理列表
     * 
     * @param orders 工单管理
     * @return 工单管理集合
     */
    List<Orders> selectOrdersList(Orders orders);

    /**
     * 新增工单管理
     * 
     * @param orders 工单管理
     * @return 结果
     */
    int insertOrders(Orders orders);

    int checkSql(Orders orders);

    /**
     * 修改工单管理
     * 
     * @param orders 工单管理
     * @return 结果
     */
    int updateOrders(Orders orders);

    /**
     * 批量删除工单管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteOrdersByIds(String[] ids);

    /**
     * 删除工单管理信息
     * 
     * @param id 工单管理ID
     * @return 结果
     */
    int deleteOrdersById(String id);

}
