package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.domain.CronJobVo;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 定时任务Service接口
 * 
 * @author zzz
 * @date 2021-03-22
 */
public interface ICronJobService 
{
    /**
     * 查询定时任务
     * 
     * @param id 定时任务ID
     * @return 定时任务
     */
    CronJob selectCronJobById(String id);


    /**
    * 查询定时任务
     * @param ${classsName} 定时任务
     * @param pageDomain
     * @return 定时任务 分页集合
     * */
    PageInfo<CronJob> selectCronJobPage(CronJob cronJob, PageDomain pageDomain);


    PageInfo<CronJobVo> linkPageData(CronJobVo cronJobVo, PageDomain pageDomain);

    /**
     * 查询定时任务列表
     * 
     * @param cronJob 定时任务
     * @return 定时任务集合
     */
    List<CronJob> selectCronJobList(CronJob cronJob);

    Result control(String id, String type);
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
     * 批量删除定时任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCronJobByIds(String[] ids);

    /**
     * 删除定时任务信息
     * 
     * @param id 定时任务ID
     * @return 结果
     */
    int deleteCronJobById(String id);

}
