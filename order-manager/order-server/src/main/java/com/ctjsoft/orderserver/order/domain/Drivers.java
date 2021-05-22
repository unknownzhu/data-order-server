package com.ctjsoft.orderserver.order.domain;

import com.ctjsoft.orderserver.utils.BaseDomain;
import lombok.Data;

/**
 * 流程管理对象 flow
 *
 * @author zzz
 * @date 2021-03-10
 */
@Data
public class Drivers extends BaseDomain {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;


    private String driverclassnames;

    private String template;

    private String port;

}
