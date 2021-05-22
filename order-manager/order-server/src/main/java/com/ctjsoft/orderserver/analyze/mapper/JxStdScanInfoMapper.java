package com.ctjsoft.orderserver.analyze.mapper;

import com.ctjsoft.orderserver.analyze.domain.JxStdScanInfo;
import com.ctjsoft.orderserver.analyze.domain.JxStdScanInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zyx
 * @date 2020/11/11
 * @desc
 */
@Repository
public interface JxStdScanInfoMapper {
    int insert(JxStdScanInfo record);

    int selcountByDbId(String   dbid);

    JxStdScanInfo selectByPrimaryKey(String id);

    List<JxStdScanInfo> list(JxStdScanInfo record );
}
