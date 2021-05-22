package com.ctjsoft.orderserver.order.service.impl;


import com.ctjsoft.orderserver.order.domain.Orders;
import com.ctjsoft.orderserver.order.domain.OrdersJobVo;
import com.ctjsoft.orderserver.order.domain.OrdersVo;
import com.ctjsoft.orderserver.order.mapper.OrdersMapper;
import com.ctjsoft.orderserver.order.service.IOrdersService;
import com.ctjsoft.orderserver.utils.DataRightUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.datagear.management.domain.User;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 工单管理Service业务层处理
 * @author zzz
 * @date 2021-03-10
 */
@Service
public class OrdersServiceImpl implements IOrdersService {
    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 查询工单管理
     * @param id 工单管理ID
     * @return 工单管理
     */
    @Override
    public Orders selectOrdersById(String id) {
        return ordersMapper.selectOrdersById(id);
    }

    @Override
    public OrdersJobVo linkSelectCronJobLogList(OrdersJobVo ordersJobVo) {
        return ordersMapper.linkSelectCronJobLogList(ordersJobVo);
    }


    /**
     * 查询工单管理列表
     * @param orders 工单管理
     * @return 工单管理
     */
    @Override
    public List<Orders> selectOrdersList(Orders orders) {
        return ordersMapper.selectOrdersList(orders);
    }

    /**
     * 查询工单管理
     * @param orders     工单管理
     * @param pageDomain
     * @return 工单管理 分页集合
     */
    @Override
    public PageInfo<Orders> selectOrdersPage(Orders orders, PageDomain pageDomain) {
        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());
        List<Orders> data = ordersMapper.selectOrdersList(orders);
        return new PageInfo<>(data);
    }

    @Override
    public PageInfo<OrdersVo> selectOrdersLinkPage(OrdersVo ordersVo, PageDomain pageDomain, String flowRightType) {
        List<OrdersVo> data = new ArrayList<>();
        User user = WebUtils.getUser();
        ordersVo.setOrderId(ordersVo.getId());
        ordersVo.setCurrentUserId(user.getId());
        if (!ordersVo.getDbId().equals("")) {
            ordersVo.setDbId(ordersVo.getDbId().substring(1, ordersVo.getDbId().length()));
        }

        if ( !(flowRightType.equals("1"))) {   //  不为1 时
            ordersVo.setFlowRightType(null);   //
        }
        if (!DataRightUtils.getUserDataType(user)) { //false则只有自己工单的权限；查询creator/updateor为含自己的工单
            ordersVo.setCreateorId(user.getId());
            ordersVo.setPusherId(user.getId());
            ordersVo.setReviewerId(user.getId());
        }
        String flowType = "";
        if (flowRightType != null && flowRightType.equals("1")) {
            flowType =  DataRightUtils.getUserFlowType(user) + "";
        }

        if (flowType.equals("0")) {
            ordersVo.setReviewStatusS("0,2,3".split(","));
            data = ordersMapper.selectOrdersLinkList(ordersVo);
        }else if(flowType.equals("1")){
            // flowType 1 时  查询未送审  或  审核成功 且创建人为自己的
           // ordersVo.setReviewStatusS("1,2".split(","));
            ordersVo.setIsSelfOrder("1");
            ordersVo.setCreateorId(user.getId());
            data = ordersMapper.selectOrdersLinkList(ordersVo);
        }else{
            data = ordersMapper.selectOrdersLinkList(ordersVo);
        }


        PageHelper.startPage(pageDomain.getPage(), pageDomain.getLimit());

        return new PageInfo<>(data);
    }

    @Override
    public OrdersVo linkDetail(OrdersVo ordersVo) {
        OrdersVo data = ordersMapper.selectOrdersLinkList(ordersVo).get(0);
        return data;
    }


    @Override
    public OrdersVo selectOneOrdersLinkDetail(OrdersVo ordersVo) {
        OrdersVo data = ordersMapper.selectOneOrdersLinkDetail(ordersVo);
        return data;
    }

    /**
     * 新增工单管理
     * @param orders 工单管理
     * @return 结果
     */

    @Override
    public int insertOrders(Orders orders) {
        return ordersMapper.insertOrders(orders);
    }

    @Override
    public int checkSql(Orders orders) {
        //     Connection cn = getConnection();

        return 0;
        //  return ordersMapper.insertOrders(orders);
    }

    /**
     * 修改工单管理
     * @param orders 工单管理
     * @return 结果
     */
    @Override
    public int updateOrders(Orders orders) {
        return ordersMapper.updateOrders(orders);
    }

    /**
     * 删除工单管理对象
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteOrdersByIds(String[] ids) {
        return ordersMapper.deleteOrdersByIds(ids);
    }

    /**
     * 删除工单管理信息
     * @param id 工单管理ID
     * @return 结果
     */
    @Override
    public int deleteOrdersById(String id) {
       // return ordersMapper.deleteOrdersById(id);
        Orders orders = new Orders();
        orders.setId(id);
        orders.setReviewStatus("9");

        return ordersMapper.updateOrders(orders);

    }
}
