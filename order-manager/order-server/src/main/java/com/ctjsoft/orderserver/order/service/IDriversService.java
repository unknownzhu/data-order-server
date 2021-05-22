package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.order.domain.Drivers;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 流程管理Service接口
 * 
 * @author zzz
 * @date 2021-03-10
 */
public interface IDriversService
{
    /**
     * 查询流程管理
     * 
     * @param id 流程管理ID
     * @return 流程管理
     */
    Drivers selectDriversById(String id);


    /**
    * 查询流程管理
     * @param ${classsName} 流程管理
     * @param pageDomain
     * @return 流程管理 分页集合
     * */
    PageInfo<Drivers> selectDriversPage(Drivers drivers, PageDomain pageDomain);

    /**
     * 查询流程管理列表
     * 
     * @param drivers 流程管理
     * @return 流程管理集合
     */
    List<Drivers> selectDriversList(Drivers drivers);

    List<Drivers> selectDriversList(Drivers drivers,String className);
    /**
     * 新增流程管理
     * 
     * @param drivers 流程管理
     * @return 结果
     */
    int insertDrivers(Drivers drivers);

    /**
     * 修改流程管理
     * 
     * @param drivers 流程管理
     * @return 结果
     */
    int updateDrivers(Drivers drivers);

    /**
     * 批量删除流程管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteDriversByIds(String[] ids);

    /**
     * 删除流程管理信息
     * 
     * @param id 流程管理ID
     * @return 结果
     */
    int deleteDriversById(String id);

}
