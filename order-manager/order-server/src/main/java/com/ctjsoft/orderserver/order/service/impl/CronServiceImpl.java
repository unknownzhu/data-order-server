package com.ctjsoft.orderserver.order.service.impl;


import com.ctjsoft.orderserver.order.domain.Cron;
import com.ctjsoft.orderserver.order.mapper.CronMapper;
import com.ctjsoft.orderserver.order.service.ICronService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-22
 */
@Service
public class CronServiceImpl implements ICronService
{
    @Autowired
    private CronMapper cronMapper;

    /**
     * 查询定时任务
     * 
     * @param id 定时任务ID
     * @return 定时任务
     */
    @Override
    public Cron selectCronById(String id)
    {
        return cronMapper.selectCronById(id);
    }

    /**
     * 查询定时任务列表
     * 
     * @param cron 定时任务
     * @return 定时任务
     */
    @Override
    public List<Cron> selectCronList(Cron cron)
    {
        return cronMapper.selectCronList(cron);
    }

    /**
     * 查询定时任务
     * @param cron 定时任务
     * @param pageDomain
     * @return 定时任务 分页集合
     * */
    @Override
    public PageInfo<Cron> selectCronPage(Cron cron, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<Cron> data = cronMapper.selectCronList(cron);
        return new PageInfo<>(data);
    }

    /**
     * 新增定时任务
     * 
     * @param cron 定时任务
     * @return 结果
     */

    @Override
    public int insertCron(Cron cron)
    {
        return cronMapper.insertCron(cron);
    }

    /**
     * 修改定时任务
     * 
     * @param cron 定时任务
     * @return 结果
     */
    @Override
    public int updateCron(Cron cron)
    {
        return cronMapper.updateCron(cron);
    }

    /**
     * 删除定时任务对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCronByIds(String[] ids)
    {
        return cronMapper.deleteCronByIds(ids);
    }

    /**
     * 删除定时任务信息
     * 
     * @param id 定时任务ID
     * @return 结果
     */
    @Override
    public int deleteCronById(String id)
    {
        return cronMapper.deleteCronById(id);
    }
}
