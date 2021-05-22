package com.ctjsoft.orderserver.order.domain;

import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

import java.util.Date;

/**
 * 表结构修改日志 TableAlterLog
 *
 * @author zzz
 * @date 2021-03-10
 */
@Data
public class TableAlterLog extends BaseDomain {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private String creatorId;
    private String creatorName;
    private String sqls;



}
