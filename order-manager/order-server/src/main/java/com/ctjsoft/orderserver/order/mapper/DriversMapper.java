package com.ctjsoft.orderserver.order.mapper;


import com.ctjsoft.orderserver.order.domain.Drivers;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 流程管理Mapper接口
 * 
 * @author zzz
 * @date 2021-03-10
 */
@Repository
public interface DriversMapper 
{
    /**
     * 查询流程管理
     * 
     * @param id 流程管理ID
     * @return 流程管理
     */
    public Drivers selectDriversById(String id);

    /**
     * 查询流程管理列表
     * 
     * @param Drivers 流程管理
     * @return 流程管理集合
     */
    List<Drivers> selectDriversList(Drivers drivers);

    /**
     * 新增流程管理
     * 
     * @param Drivers 流程管理
     * @return 结果
     */
    int insertDrivers(Drivers drivers);

    /**
     * 修改流程管理
     * 
     * @param Drivers 流程管理
     * @return 结果
     */
    int updateDrivers(Drivers drivers);

    /**
     * 删除流程管理
     * 
     * @param id 流程管理ID
     * @return 结果
     */
    int deleteDriversById(String id);

    /**
     * 批量删除流程管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDriversByIds(String[] ids);

}
