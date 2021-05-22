package com.ctjsoft.orderserver.order.mapper;

import com.ctjsoft.orderserver.order.domain.CommonSql;
import com.ctjsoft.orderserver.order.domain.CommonSqlVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * 常用语句Mapper接口
 * 
 * @author zzz
 * @date 2021-03-24
 */
@Mapper
public interface CommonSqlMapper 
{
    /**
     * 查询常用语句
     * 
     * @param id 常用语句ID
     * @return 常用语句
     */
    public CommonSql selectCommonSqlById(String id);

    /**
     * 查询常用语句列表
     * 
     * @param commonSql 常用语句
     * @return 常用语句集合
     */
    List<CommonSql> selectCommonSqlList(CommonSql commonSql);
    List<CommonSqlVo> selectCommonSqlVoList(CommonSql commonSql);

    /**
     * 新增常用语句
     * 
     * @param commonSql 常用语句
     * @return 结果
     */
    int insertCommonSql(CommonSql commonSql);

    int insertFronHistorySql(@Param(value = "historySqlId") String historySqlId, @Param(value = "showType") String showType);

    /**
     * 修改常用语句
     * 
     * @param commonSql 常用语句
     * @return 结果
     */
    int updateCommonSql(CommonSql commonSql);

    /**
     * 删除常用语句
     * 
     * @param id 常用语句ID
     * @return 结果
     */
    int deleteCommonSqlById(String id);

    /**
     * 批量删除常用语句
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteCommonSqlByIds(String[] ids);

}
