package com.ctjsoft.orderserver.order.domain;

import java.util.Date;

import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 工单管理对象 orders
 *
 * @author zzz
 * @date 2021-03-10
 */
@Data
public class Orders extends BaseDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 工单主键
     */
    private String id;

    /**
     * 工单名
     */
    private String name;

    /**
     * sql 语句
     */
    private String sqls;

    private String sqlType;

    private String fileId;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 影响行数
     */
    private String rowCount;

    /**
     * 数据库id
     */
    private String dbId;

    /**
     * 是否定时任务 0 否 1 是
     */
    private String isSchedule;

    /**
     * 定时表达式
     */
    private String cron;

    /**
     * 状态 0 未申请（草稿） 1 已送审 2 审核成功 3 审核失败
     */
    private String reviewStatus;

    private String checkStatus;

    private String checkStatusTitle;


    /**
     * 执行状态 0 失败 1 成功
     */
    private String executeStatus;

    /**
     * 流程id
     */
    private String flowId;

    /**
     * 送审时间
     */
    private Date pushTime;

    /**
     * 审核时间
     */
    private Date reviewTime;

    /**
     * 创建人id
     */
    private String createorId;

    /**
     * 送审人id
     */
    private String pusherId;

    /**
     * 审核人id
     */
    private String reviewerId;

    private String reviewerName;
    private String forbidWords;


    private String executorId;


    private Date executeTime;











    private String checkStatusTitle1;
    ;
    private String errorInfo;
    private String file;
    private String fileName;



}
