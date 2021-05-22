package com.ctjsoft.orderserver.order.service.impl;

import com.ctjsoft.orderserver.order.domain.Flow;
import com.ctjsoft.orderserver.order.mapper.FlowMapper;
import com.ctjsoft.orderserver.order.service.IFlowService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程管理Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-10
 */
@Service
public class FlowServiceImpl implements IFlowService
{
    @Autowired
    private FlowMapper flowMapper;

    /**
     * 查询流程管理
     * 
     * @param id 流程管理ID
     * @return 流程管理
     */
    @Override
    public Flow selectFlowById(String id)
    {
        return flowMapper.selectFlowById(id);
    }

    /**
     * 查询流程管理列表
     * 
     * @param flow 流程管理
     * @return 流程管理
     */
    @Override
    public List<Flow> selectFlowList(Flow flow)
    {
        return flowMapper.selectFlowList(flow);
    }

    /**
     * 查询流程管理
     * @param flow 流程管理
     * @param pageDomain
     * @return 流程管理 分页集合
     * */
    @Override
    public PageInfo<Flow> selectFlowPage(Flow flow, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<Flow> data = flowMapper.selectFlowList(flow);
        return new PageInfo<>(data);
    }

    /**
     * 新增流程管理
     * 
     * @param flow 流程管理
     * @return 结果
     */

    @Override
    public int insertFlow(Flow flow)
    {
        return flowMapper.insertFlow(flow);
    }

    /**
     * 修改流程管理
     * 
     * @param flow 流程管理
     * @return 结果
     */
    @Override
    public int updateFlow(Flow flow)
    {
        return flowMapper.updateFlow(flow);
    }

    /**
     * 删除流程管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFlowByIds(String[] ids)
    {
        return flowMapper.deleteFlowByIds(ids);
    }

    /**
     * 删除流程管理信息
     * 
     * @param id 流程管理ID
     * @return 结果
     */
    @Override
    public int deleteFlowById(String id)
    {
        return flowMapper.deleteFlowById(id);
    }
}
