package com.ctjsoft.orderserver.order.domain;

import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

import java.util.Date;

/**
 * 送审日志对象 jx_order_review_log
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Data
public class OrderReviewLog extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 送审类型 0 保存 1 送审 2 审核通过 3 审核不通过 */
    private String type;

    /** 工单id */
    private String orderId;

    /** 创建人 */
    private String creatorId;
    /** 创建人 */
    private Date createTime;

    private String creatorName;

    private String orderName;

    /** 审核意见 */
    private String info;

    public OrderReviewLog(String id, String type, String orderId, String creatorId, Date createTime, String creatorName, String orderName, String info) {
        this.id = id;
        this.type = type;
        this.orderId = orderId;
        this.creatorId = creatorId;
        this.createTime = createTime;
        this.creatorName = creatorName;
        this.orderName = orderName;
        this.info = info;
    }
}
