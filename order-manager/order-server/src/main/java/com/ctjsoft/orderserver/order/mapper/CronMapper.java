package com.ctjsoft.orderserver.order.mapper;

import com.ctjsoft.orderserver.order.domain.Cron;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
/**
 * 定时任务Mapper接口
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Mapper
public interface CronMapper 
{
    /**
     * 查询定时任务
     * 
     * @param id 定时任务ID
     * @return 定时任务
     */
    public Cron selectCronById(String id);

    /**
     * 查询定时任务列表
     * 
     * @param cron 定时任务
     * @return 定时任务集合
     */
    List<Cron> selectCronList(Cron cron);

    /**
     * 新增定时任务
     * 
     * @param cron 定时任务
     * @return 结果
     */
    int insertCron(Cron cron);

    /**
     * 修改定时任务
     * 
     * @param cron 定时任务
     * @return 结果
     */
    int updateCron(Cron cron);

    /**
     * 删除定时任务
     * 
     * @param id 定时任务ID
     * @return 结果
     */
    int deleteCronById(String id);

    /**
     * 批量删除定时任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCronByIds(String[] ids);

}
