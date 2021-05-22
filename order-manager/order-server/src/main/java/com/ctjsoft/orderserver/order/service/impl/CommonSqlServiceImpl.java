package com.ctjsoft.orderserver.order.service.impl;


import com.ctjsoft.orderserver.order.domain.CommonSql;
import com.ctjsoft.orderserver.order.domain.CommonSqlVo;
import com.ctjsoft.orderserver.order.mapper.CommonSqlMapper;
import com.ctjsoft.orderserver.order.service.ICommonSqlService;
import com.ctjsoft.orderserver.utils.DataRightUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.datagear.management.domain.User;
import org.datagear.web.util.WebUtils;
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
public class CommonSqlServiceImpl implements ICommonSqlService
{
    @Autowired
    private CommonSqlMapper commonSqlMapper;

    /**
     * 查询常用语句
     * 
     * @param id 常用语句ID
     * @return 常用语句
     */
    @Override
    public CommonSql selectCommonSqlById(String id)
    {
        return commonSqlMapper.selectCommonSqlById(id);
    }

    /**
     * 查询常用语句列表
     * 
     * @param commonSql 常用语句
     * @return 常用语句
     */
    @Override
    public List<CommonSql> selectCommonSqlList(CommonSql commonSql)
    {
        return commonSqlMapper.selectCommonSqlList(commonSql);
    }

    /**
     * 查询常用语句
     * @param commonSql 常用语句
     * @param pageDomain
     * @return 常用语句 分页集合
     * */
    @Override
    public PageInfo<CommonSql> selectCommonSqlPage(CommonSql commonSql, PageDomain pageDomain)
    {
        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<CommonSql> data = commonSqlMapper.selectCommonSqlList(commonSql);
        return new PageInfo<>(data);
    }


    @Override
    public PageInfo<CommonSqlVo> selectCommonVoSqlPage(CommonSql commonSql, PageDomain pageDomain)
    {

        User user = WebUtils.getUser();
        if (!DataRightUtils.getUserDataType(user)) { //false则只有自己工单的权限；设置creator/updateor 为自己
            commonSql.setCreatorId(user.getId());
        }

        PageHelper.startPage(pageDomain.getPage(),pageDomain.getLimit());
        List<CommonSqlVo> data = commonSqlMapper.selectCommonSqlVoList(commonSql);
        return new PageInfo<>(data);
    }

    /**
     * 新增常用语句
     * 
     * @param commonSql 常用语句
     * @return 结果
     */

    @Override
    public int insertCommonSql(CommonSql commonSql)
    {
        return commonSqlMapper.insertCommonSql(commonSql);
    }

    @Override
    public int insertFronHistorySql(String historySqlId ,String showType)
    {
        return commonSqlMapper.insertFronHistorySql(historySqlId , showType);
    }
    /**
     * 修改常用语句
     * 
     * @param commonSql 常用语句
     * @return 结果
     */
    @Override
    public int updateCommonSql(CommonSql commonSql)
    {
        return commonSqlMapper.updateCommonSql(commonSql);
    }

    /**
     * 删除常用语句对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCommonSqlByIds(String[] ids)
    {
        return commonSqlMapper.deleteCommonSqlByIds(ids);
    }

    /**
     * 删除常用语句信息
     * 
     * @param id 常用语句ID
     * @return 结果
     */
    @Override
    public int deleteCommonSqlById(String id)
    {
        return commonSqlMapper.deleteCommonSqlById(id);
    }
}
