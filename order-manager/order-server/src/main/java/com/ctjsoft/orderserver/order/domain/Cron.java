package com.ctjsoft.orderserver.order.domain;


import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 定时任务对象 Cron
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Data
public class Cron extends BaseDomain
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 名称 */
    private String name;

    /** 表达式 */
    private String cron;

    /** 创建人 */
    private String creatorId;

    /** 排序 */
    private String sort;

}
