package com.ctjsoft.orderserver.order.mapper;

import com.ctjsoft.orderserver.order.domain.Cron;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.datagear.management.domain.Schema;

import java.util.List;

/**
 * 定时任务Mapper接口
 *
 * @author zzz
 * @date 2021-03-22
 */
@Mapper
public interface SchemaMapper {

    int updateDbSpsce(@Param("id") String id, @Param("tablespace_name") String tablespace_name, @Param("total") String total, @Param("used") String used, @Param("free") String free);

    int updateDbStatus(@Param("id") String id,@Param("status") String status);

    List<Schema> selectTableSpace();



}
