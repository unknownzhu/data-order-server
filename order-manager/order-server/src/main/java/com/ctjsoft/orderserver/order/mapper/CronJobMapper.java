package com.ctjsoft.orderserver.order.mapper;


import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.domain.CronJobVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 定时任务Mapper接口
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Mapper
public interface CronJobMapper 
{
    /**
     * 查询定时任务
     * 
     * @param id 定时任务ID
     * @return 定时任务
     */
    public CronJob selectCronJobById(String id);

    /**
     * 查询定时任务列表
     * 
     * @param cronJob 定时任务
     * @return 定时任务集合
     */
    List<CronJob> selectCronJobList(CronJob cronJob);

    List<CronJobVo> linkPageData(CronJob cronJob);

    /**
     * 新增定时任务
     * 
     * @param cronJob 定时任务
     * @return 结果
     */
    int insertCronJob(CronJob cronJob);

    /**
     * 修改定时任务
     * 
     * @param cronJob 定时任务
     * @return 结果
     */
    int updateCronJob(CronJob cronJob);

    /**
     * 删除定时任务
     * 
     * @param id 定时任务ID
     * @return 结果
     */
    int deleteCronJobById(String id);

    /**
     * 批量删除定时任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCronJobByIds(String[] ids);

}
