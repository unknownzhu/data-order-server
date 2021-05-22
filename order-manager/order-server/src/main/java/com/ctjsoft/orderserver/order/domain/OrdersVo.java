package com.ctjsoft.orderserver.order.domain;

import lombok.Data;

import java.util.List;

/**
 * 工单管理对象 orders
 * 
 * @author zzz
 * @date 2021-03-10
 */
@Data
public class OrdersVo extends Orders
{
    private String dbName;
    private String flowName;
    private String creatorName;
    private String pusherName;
    private String reviewerName;
    private String cronName;
    private String cronText;
    //private String executeTime;
    private String[] reviewStatusS;


    private String flowRightType;
    private String currentUserId;
    private String orderId;


    private String isSelfOrder ; // 1 自己创建、自己审核的工单  0或其他 不管

    //private String cronText;



}