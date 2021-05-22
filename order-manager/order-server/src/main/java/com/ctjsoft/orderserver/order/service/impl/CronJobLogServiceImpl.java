package com.ctjsoft.orderserver.order.service.impl;


import com.ctjsoft.orderserver.order.domain.CronJobLog;
import com.ctjsoft.orderserver.order.mapper.CronJobLogMapper;
import com.ctjsoft.orderserver.order.service.ICronJobLogService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务日志Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Service
public class CronJobLogServiceImpl implements ICronJobLogService
{
    @Autowired
    private CronJobLogMapper cronJobLogMapper;

    /**
     * 查询定时任务日志
     * 
     * @param id 定时任务日志ID
     * @return 定时任务日志
     */
    @Override
    public CronJobLog selectCronJobLogById(String id)
    {
        return cronJobLogMapper.selectCronJobLogById(id);
    }

    /**
     * 查询定时任务日志列表
     * 
     * @param cronJobLog 定时任务日志
     * @return 定时任务日志
     */
    @Override
    public List<CronJobLog> selectCronJobLogList(CronJobLog cronJobLog)
    {
        return cronJobLogMapper.selectCronJobLogList(cronJobLog);
    }

    /**
     * 查询定时任务日志
     * @param cronJobLog 定时任务日志
     * @param pageDomain
     * @return 定时任务日志 分页集合
     * */
    @Override
    public PageInfo<CronJobLog> selectCronJobLogPage(CronJobLog cronJobLog, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<CronJobLog> data = cronJobLogMapper.selectCronJobLogList(cronJobLog);
        return new PageInfo<>(data);
    }

    /**
     * 新增定时任务日志
     * 
     * @param cronJobLog 定时任务日志
     * @return 结果
     */

    @Override
    public int insertCronJobLog(CronJobLog cronJobLog)
    {
        return cronJobLogMapper.insertCronJobLog(cronJobLog);
    }

    /**
     * 修改定时任务日志
     * 
     * @param cronJobLog 定时任务日志
     * @return 结果
     */
    @Override
    public int updateCronJobLog(CronJobLog cronJobLog)
    {
        return cronJobLogMapper.updateCronJobLog(cronJobLog);
    }

    /**
     * 删除定时任务日志对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCronJobLogByIds(String[] ids)
    {
        return cronJobLogMapper.deleteCronJobLogByIds(ids);
    }

    /**
     * 删除定时任务日志信息
     * 
     * @param id 定时任务日志ID
     * @return 结果
     */
    @Override
    public int deleteCronJobLogById(String id)
    {
        return cronJobLogMapper.deleteCronJobLogById(id);
    }
}
