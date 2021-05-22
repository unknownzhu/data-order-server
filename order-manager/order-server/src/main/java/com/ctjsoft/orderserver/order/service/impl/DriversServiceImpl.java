package com.ctjsoft.orderserver.order.service.impl;

import com.ctjsoft.orderserver.order.domain.Drivers;
import com.ctjsoft.orderserver.order.mapper.DriversMapper;
import com.ctjsoft.orderserver.order.mapper.DriversMapper;
import com.ctjsoft.orderserver.order.service.IDriversService;
import com.ctjsoft.orderserver.order.service.IDriversService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程管理Service业务层处理
 *
 * @author zzz
 * @date 2021-03-10
 */
@Service
public class DriversServiceImpl implements IDriversService
{
    @Autowired
    private DriversMapper driversMapper;

    /**
     * 查询流程管理
     *
     * @param id 流程管理ID
     * @return 流程管理
     */
    @Override
    public Drivers selectDriversById(String id)
    {
        return driversMapper.selectDriversById(id);
    }

    /**
     * 查询流程管理列表
     *
     * @param drivers 流程管理
     * @return 流程管理
     */
    @Override
    public List<Drivers> selectDriversList(Drivers drivers)
    {
        return driversMapper.selectDriversList(drivers);
    }

    @Override
    public List<Drivers> selectDriversList(Drivers drivers,String className)
    {

        List<Drivers> list = driversMapper.selectDriversList(drivers);
        List<Drivers> tmpList = new ArrayList<>();
        for (Drivers tmp : list) {
            if (tmp.getName().toUpperCase().contains(className.toUpperCase())
                    || tmp.getDriverclassnames().toUpperCase().contains(className.toUpperCase())) {
                tmpList.add(tmp)
                ;
            }
        }

        return  tmpList ;
    }

    /**
     * 查询流程管理
     * @param drivers 流程管理
     * @param pageDomain
     * @return 流程管理 分页集合
     * */
    @Override
    public PageInfo<Drivers> selectDriversPage(Drivers drivers, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<Drivers> data = driversMapper.selectDriversList(drivers);
        return new PageInfo<>(data);
    }

    /**
     * 新增流程管理
     *
     * @param drivers 流程管理
     * @return 结果
     */

    @Override
    public int insertDrivers(Drivers drivers)
    {
        return driversMapper.insertDrivers(drivers);
    }

    /**
     * 修改流程管理
     *
     * @param drivers 流程管理
     * @return 结果
     */
    @Override
    public int updateDrivers(Drivers drivers)
    {
        return driversMapper.updateDrivers(drivers);
    }

    /**
     * 删除流程管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteDriversByIds(String[] ids)
    {
        return driversMapper.deleteDriversByIds(ids);
    }

    /**
     * 删除流程管理信息
     *
     * @param id 流程管理ID
     * @return 结果
     */
    @Override
    public int deleteDriversById(String id)
    {
        return driversMapper.deleteDriversById(id);
    }
}
