package com.ctjsoft.orderserver.analyze.domain;

import lombok.Data;

/**
 * @Author zyx
 * Date on 2020/11/11
 */
@Data
//@AllArgsConstructor
public class JxStdScheduleDto {
    private String id;
    private String name;
    private String remark;
    private String cron;
    private String jobName;
    private String groupId;
}
