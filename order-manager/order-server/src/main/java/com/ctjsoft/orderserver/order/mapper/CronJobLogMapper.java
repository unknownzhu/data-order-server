package com.ctjsoft.orderserver.order.mapper;


import com.ctjsoft.orderserver.order.domain.CronJobLog;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
 * 定时任务日志Mapper接口
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Mapper
public interface CronJobLogMapper 
{
    /**
     * 查询定时任务日志
     * 
     * @param id 定时任务日志ID
     * @return 定时任务日志
     */
    public CronJobLog selectCronJobLogById(String id);

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
     * 删除定时任务日志
     * 
     * @param id 定时任务日志ID
     * @return 结果
     */
    int deleteCronJobLogById(String id);

    /**
     * 批量删除定时任务日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCronJobLogByIds(String[] ids);

}
