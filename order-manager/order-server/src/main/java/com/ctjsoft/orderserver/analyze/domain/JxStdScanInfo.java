package com.ctjsoft.orderserver.analyze.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class JxStdScanInfo {
    private String id;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createtime;
    private String creatorid;
    private String creatorname;
    private Integer totalnum;
    private Integer ordernum;
    private Integer updatedicrecords;
    private Integer insertdicrecords;
    private Integer deletedicrecords;
    private Integer updatecolumnsrecord;
    private Integer insertcolumnsrecord;
    private Integer deletecolumnsrecord;
    private String dbid;
    private String info;
    private String status;



    public JxStdScanInfo() {
        super();
    }

    public JxStdScanInfo(String id, Date createtime, String creatorid, String creatorname, Integer totalnum, Integer ordernum, Integer updatedicrecords, Integer insertdicrecords,
                         Integer deletedicrecords, Integer updatecolumnsrecord, Integer insertcolumnsrecord, Integer deletecolumnsrecord, String dbid,String info ,String status ) {
        this.id = id;
        this.createtime = createtime;
        this.creatorid = creatorid;
        this.creatorname = creatorname;
        this.totalnum = totalnum;
        this.ordernum = ordernum;
        this.updatedicrecords = updatedicrecords;
        this.insertdicrecords = insertdicrecords;
        this.deletedicrecords = deletedicrecords;
        this.updatecolumnsrecord = updatecolumnsrecord;
        this.insertcolumnsrecord = insertcolumnsrecord;
        this.deletecolumnsrecord = deletecolumnsrecord;
        this.dbid = dbid;
        this.info = info;
        this.status = status;
    }


    public JxStdScanInfo(String id, String dbid,String info ,String status ) {
        this.id = id;
        this.createtime = new Date();
        this.creatorid = "";
        this.creatorname = "";
        this.totalnum = 0;
        this.ordernum = 0;
        this.updatedicrecords = 0;
        this.insertdicrecords = 0;
        this.deletedicrecords = 0;
        this.updatecolumnsrecord = 0;
        this.insertcolumnsrecord = 0;
        this.deletecolumnsrecord = 0;
        this.dbid = dbid;
        this.info = info;
        this.status = status;
    }
}
