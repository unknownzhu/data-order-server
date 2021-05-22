package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;
import java.util.Date;

@Data
public  class JxCompareVo {

    private static final long serialVersionUID = 1L;

    private String chineseName;
    private String databaseId;
    private String isNewest;
    private String tableName;
    private Date createTime;


    private String name;
    private String type;
    private String length;
    private String isPrimary;
    private String isEmpty;
    private String defaultValue;
    private String remak;

    private String mName;
    private String mType;
    private String mLength;
    private String mIsPrimary;
    private String mIsEmpty;
    private String mDefaultValue;

}