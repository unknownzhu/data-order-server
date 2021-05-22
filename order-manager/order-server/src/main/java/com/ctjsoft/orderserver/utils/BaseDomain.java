package com.ctjsoft.orderserver.utils;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * Describe: 基 础 实 体 类
 * Author: 就 眠 仪 式
 * CreateTime: 2019/10/23
 * */
@Data
public class BaseDomain implements Serializable {

    /**
     * 创建时间
     * */
    private Date createTime;

    /**
     * 创建人
     * */
    private String createBy;

    /**
     * 创建人名称
     * */
    private String createName;

    /**
     * 修改时间
     * */
    private Date updateTime;

    /**
     * 修改时间
     * */
    private String updateBy;

    /**
     * 修改名称
     * */
    private String updateName;

    /**
     * 备注
     * */
    private String remark;

    /**
     *  请求参数
     *  */
    private Map<String, Object> params;


    private Date startTime;


    private Date endTime;


    private String timeType;

    private String tmpId;

}
