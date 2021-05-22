package com.ctjsoft.orderserver.order.mapper;


import com.ctjsoft.orderserver.order.domain.Flow;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流程管理Mapper接口
 * 
 * @author zzz
 * @date 2021-03-10
 */
@Repository
public interface FlowMapper 
{
    /**
     * 查询流程管理
     * 
     * @param id 流程管理ID
     * @return 流程管理
     */
    public Flow selectFlowById(String id);

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
     * 删除流程管理
     * 
     * @param id 流程管理ID
     * @return 结果
     */
    int deleteFlowById(String id);

    /**
     * 批量删除流程管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteFlowByIds(String[] ids);

}
