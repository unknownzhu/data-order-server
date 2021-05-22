package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

import java.util.List;

@Data
public class JxScoreRelationDetailVo  {

    private String id;
    private String code;
    private String name;
    private Integer score;

    private String contentDes;
    private Integer defaultValue;

    private Integer proportion;

    private List<JxScoreRelationDetailVo> children;

    private String scoreType;
    private String stdType;
    private String huizongNum;
    private String shengchanNum;

}
