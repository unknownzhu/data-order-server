package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.Flow;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 流程管理Service接口
 * 
 * @author zzz
 * @date 2021-03-10
 */
public interface IFlowService 
{
    /**
     * 查询流程管理
     * 
     * @param id 流程管理ID
     * @return 流程管理
     */
    Flow selectFlowById(String id);


    /**
    * 查询流程管理
     * @param ${classsName} 流程管理
     * @param pageDomain
     * @return 流程管理 分页集合
     * */
    PageInfo<Flow> selectFlowPage(Flow flow, PageDomain pageDomain);

    /**
     * 查询流程管理列表
     * 
     * @param flow 流程管理
     * @return 流程管理集合
     */
    List<Flow> selectFlowList(Flow flow);

    /**
     * 新增流程管理
     * 
     * @param flow 流程管理
     * @return 结果
     */
    int insertFlow(Flow flow);

    /**
     * 修改流程管理
     * 
     * @param flow 流程管理
     * @return 结果
     */
    int updateFlow(Flow flow);

    /**
     * 批量删除流程管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteFlowByIds(String[] ids);

    /**
     * 删除流程管理信息
     * 
     * @param id 流程管理ID
     * @return 结果
     */
    int deleteFlowById(String id);

}
