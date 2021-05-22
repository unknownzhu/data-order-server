package com.ctjsoft.orderserver.order.service.impl;


import com.ctjsoft.orderserver.order.domain.HistorySql;
import com.ctjsoft.orderserver.order.domain.HistorySqlVo;
import com.ctjsoft.orderserver.order.mapper.HistorySqlMapper;
import com.ctjsoft.orderserver.order.service.IHistorySqlService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 常用语句Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Service
public class HistorySqlServiceImpl implements IHistorySqlService
{
    @Autowired
    private HistorySqlMapper historySqlMapper;

    /**
     * 查询常用语句
     * 
     * @param id 常用语句ID
     * @return 常用语句
     */
    @Override
    public HistorySql selectHistorySqlById(String id)
    {
        return historySqlMapper.selectHistorySqlById(id);
    }

    /**
     * 查询常用语句列表
     * 
     * @param historySql 常用语句
     * @return 常用语句
     */
    @Override
    public List<HistorySql> selectHistorySqlList(HistorySql historySql)
    {
        return historySqlMapper.selectHistorySqlList(historySql);
    }

    /**
     * 查询常用语句
     * @param historySql 常用语句
     * @param pageDomain
     * @return 常用语句 分页集合
     * */
    @Override
    public PageInfo<HistorySql> selectHistorySqlPage(HistorySql historySql, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<HistorySql> data = historySqlMapper.selectHistorySqlList(historySql);
        return new PageInfo<>(data);
    }


    @Override
    public PageInfo<HistorySqlVo> selectCommonVoSqlPage(HistorySql historySql, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<HistorySqlVo> data = historySqlMapper.selectHistorySqlVoList(historySql);
        return new PageInfo<>(data);
    }

    /**
     * 新增常用语句
     * 
     * @param historySql 常用语句
     * @return 结果
     */

    @Override
    public int insertHistorySql(HistorySql historySql)
    {
        return historySqlMapper.insertHistorySql(historySql);
    }

    /**
     * 修改常用语句
     * 
     * @param historySql 常用语句
     * @return 结果
     */
    @Override
    public int updateHistorySql(HistorySql historySql)
    {
        return historySqlMapper.updateHistorySql(historySql);
    }

    /**
     * 删除常用语句对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteHistorySqlByIds(String[] ids)
    {
        return historySqlMapper.deleteHistorySqlByIds(ids);
    }

    /**
     * 删除常用语句信息
     * 
     * @param id 常用语句ID
     * @return 结果
     */
    @Override
    public int deleteHistorySqlById(String id)
    {
        return historySqlMapper.deleteHistorySqlById(id);
    }
}
