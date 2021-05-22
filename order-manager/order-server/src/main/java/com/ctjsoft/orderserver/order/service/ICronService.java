package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 定时任务Service接口
 * 
 * @author zzz
 * @date 2021-03-22
 */
public interface ICronService 
{
    /**
     * 查询定时任务
     * 
     * @param id 定时任务ID
     * @return 定时任务
     */
    Cron selectCronById(String id);


    /**
    * 查询定时任务
     * @param ${classsName} 定时任务
     * @param pageDomain
     * @return 定时任务 分页集合
     * */
    PageInfo<Cron> selectCronPage(Cron cron, PageDomain pageDomain);

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
     * 批量删除定时任务
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCronByIds(String[] ids);

    /**
     * 删除定时任务信息
     * 
     * @param id 定时任务ID
     * @return 结果
     */
    int deleteCronById(String id);

}
