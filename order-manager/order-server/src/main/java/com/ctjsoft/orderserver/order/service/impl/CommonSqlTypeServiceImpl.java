package com.ctjsoft.orderserver.order.service.impl;

import java.util.List;

import com.ctjsoft.orderserver.order.domain.CommonSqlType;
import com.ctjsoft.orderserver.order.mapper.CommonSqlTypeMapper;
import com.ctjsoft.orderserver.order.service.ICommonSqlTypeService;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 语句类型Service业务层处理
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Service
public class CommonSqlTypeServiceImpl implements ICommonSqlTypeService
{
    @Autowired
    private CommonSqlTypeMapper commonSqlTypeMapper;

    /**
     * 查询语句类型
     * 
     * @param id 语句类型ID
     * @return 语句类型
     */
    @Override
    public CommonSqlType selectCommonSqlTypeById(String id)
    {
        return commonSqlTypeMapper.selectCommonSqlTypeById(id);
    }

    /**
     * 查询语句类型列表
     * 
     * @param commonSqlType 语句类型
     * @return 语句类型
     */
    @Override
    public List<CommonSqlType> selectCommonSqlTypeList(CommonSqlType commonSqlType)
    {
        return commonSqlTypeMapper.selectCommonSqlTypeList(commonSqlType);
    }

    /**
     * 查询语句类型
     * @param commonSqlType 语句类型
     * @param pageDomain
     * @return 语句类型 分页集合
     * */
    @Override
    public PageInfo<CommonSqlType> selectCommonSqlTypePage(CommonSqlType commonSqlType, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<CommonSqlType> data = commonSqlTypeMapper.selectCommonSqlTypeList(commonSqlType);
        return new PageInfo<>(data);
    }

    /**
     * 新增语句类型
     * 
     * @param commonSqlType 语句类型
     * @return 结果
     */

    @Override
    public int insertCommonSqlType(CommonSqlType commonSqlType)
    {
        return commonSqlTypeMapper.insertCommonSqlType(commonSqlType);
    }

    /**
     * 修改语句类型
     * 
     * @param commonSqlType 语句类型
     * @return 结果
     */
    @Override
    public int updateCommonSqlType(CommonSqlType commonSqlType)
    {
        return commonSqlTypeMapper.updateCommonSqlType(commonSqlType);
    }

    /**
     * 删除语句类型对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCommonSqlTypeByIds(String[] ids)
    {
        return commonSqlTypeMapper.deleteCommonSqlTypeByIds(ids);
    }

    /**
     * 删除语句类型信息
     * 
     * @param id 语句类型ID
     * @return 结果
     */
    @Override
    public int deleteCommonSqlTypeById(String id)
    {
        return commonSqlTypeMapper.deleteCommonSqlTypeById(id);
    }
}
