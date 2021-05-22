package com.ctjsoft.orderserver.order.mapper;

import com.ctjsoft.orderserver.order.domain.HistorySql;
import com.ctjsoft.orderserver.order.domain.HistorySqlVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 常用语句Mapper接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Mapper
public interface HistorySqlMapper
{
    /**
     * 查询常用语句
     * 
     * @param id 常用语句ID
     * @return 常用语句
     */
    public HistorySql selectHistorySqlById(String id);

    /**
     * 查询常用语句列表
     * 
     * @param historySql 常用语句
     * @return 常用语句集合
     */
    List<HistorySql> selectHistorySqlList(HistorySql historySql);
    List<HistorySqlVo> selectHistorySqlVoList(HistorySql historySql);

    /**
     * 新增常用语句
     * 
     * @param historySql 常用语句
     * @return 结果
     */
    int insertHistorySql(HistorySql historySql);

    /**
     * 修改常用语句
     * 
     * @param historySql 常用语句
     * @return 结果
     */
    int updateHistorySql(HistorySql historySql);

    /**
     * 删除常用语句
     * 
     * @param id 常用语句ID
     * @return 结果
     */
    int deleteHistorySqlById(String id);

    /**
     * 批量删除常用语句
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteHistorySqlByIds(String[] ids);

}
