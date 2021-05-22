package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.CronJobLog;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 定时任务日志Service接口
 * 
 * @author zzz
 * @date 2021-03-22
 */
public interface ICronJobLogService 
{
    /**
     * 查询定时任务日志
     * 
     * @param id 定时任务日志ID
     * @return 定时任务日志
     */
    CronJobLog selectCronJobLogById(String id);


    /**
    * 查询定时任务日志
     * @param ${classsName} 定时任务日志
     * @param pageDomain
     * @return 定时任务日志 分页集合
     * */
    PageInfo<CronJobLog> selectCronJobLogPage(CronJobLog cronJobLog, PageDomain pageDomain);

    /**
     * 查询定时任务日志列表
     * 
     * @param cronJobLog 定时任务日志
     * @return 定时任务日志集合
     */
    List<CronJobLog> selectCronJobLogList(CronJobLog cronJobLog);

    /**
     * 新增定时任务日志
     * 
     * @param cronJobLog 定时任务日志
     * @return 结果
     */
    int insertCronJobLog(CronJobLog cronJobLog);

    /**
     * 修改定时任务日志
     * 
     * @param cronJobLog 定时任务日志
     * @return 结果
     */
    int updateCronJobLog(CronJobLog cronJobLog);

    /**
     * 批量删除定时任务日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCronJobLogByIds(String[] ids);

    /**
     * 删除定时任务日志信息
     * 
     * @param id 定时任务日志ID
     * @return 结果
     */
    int deleteCronJobLogById(String id);

}
