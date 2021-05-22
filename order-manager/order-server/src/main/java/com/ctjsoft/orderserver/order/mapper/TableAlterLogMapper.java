package com.ctjsoft.orderserver.order.mapper;

import com.ctjsoft.orderserver.order.domain.TableAlterLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 语句执行日志Mapper接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Mapper
public interface TableAlterLogMapper
{
    /**
     * 查询语句执行日志
     * 
     * @param id 语句执行日志ID
     * @return 语句执行日志
     */
    public TableAlterLog selectTableAlterLogById(String id);

    /**
     * 查询语句执行日志列表
     * 
     * @param TableAlterLog 语句执行日志
     * @return 语句执行日志集合
     */
    List<TableAlterLog> selectTableAlterLogList(TableAlterLog TableAlterLog);

    /**
     * 新增语句执行日志
     * 
     * @param TableAlterLog 语句执行日志
     * @return 结果
     */
    int insertTableAlterLog(TableAlterLog TableAlterLog);

    /**
     * 修改语句执行日志
     * 
     * @param TableAlterLog 语句执行日志
     * @return 结果
     */
    int updateTableAlterLog(TableAlterLog TableAlterLog);

    /**
     * 删除语句执行日志
     * 
     * @param id 语句执行日志ID
     * @return 结果
     */
    int deleteTableAlterLogById(String id);

    /**
     * 批量删除语句执行日志
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteTableAlterLogByIds(String[] ids);

}
