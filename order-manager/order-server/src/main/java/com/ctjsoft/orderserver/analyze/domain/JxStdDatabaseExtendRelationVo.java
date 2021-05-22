package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

@Data
public class JxStdDatabaseExtendRelationVo extends StdDatabase {

    private String companyCode;

    private String companyName;

    private String systemCode;

    private String systemName;

    private String scoreType;

}
