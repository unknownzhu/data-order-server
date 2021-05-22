package com.ctjsoft.orderserver.order.controller;


import com.ctjsoft.orderserver.order.domain.CronJob;
import com.ctjsoft.orderserver.order.domain.CronJobVo;
import com.ctjsoft.orderserver.order.mapper.SchemaMapper;
import com.ctjsoft.orderserver.order.service.ICronJobService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.datagear.management.domain.Schema;
import org.datagear.management.domain.User;
import org.datagear.management.service.SchemaService;
import org.datagear.persistence.Order;
import org.datagear.persistence.PagingData;
import org.datagear.persistence.PagingQuery;
import org.datagear.web.controller.AbstractSchemaConnTableController;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

import static com.ctjsoft.orderserver.order.controller.OrdersController.pageTable;
import static org.datagear.web.controller.SchemaController.COOKIE_PAGINATION_SIZE;

/**
 * 定时任务Controller
 *
 * @author zzz
 * @date 2021-03-22
 */
@RestController
@RequestMapping("/system/schema")
public class SchemaSpaceController extends AbstractSchemaConnTableController {
    private String prefix = "system/job";

    @Autowired
    private ICronJobService cronJobService;
    @Autowired
    private SchemaMapper schemaMapper;

    @Autowired
    private SchemaService schemaService;


    @GetMapping(value = "/list")
    @ResponseBody
    public ResultTable list(HttpServletRequest request, HttpServletResponse response, @ModelAttribute Schema schema,  PageDomain pageDomain) {

        PagingQuery pagingQueryParam = new PagingQuery();
        pagingQueryParam.setPage(pageDomain.getPage());
        pagingQueryParam.setPageSize(pageDomain.getLimit());
        pagingQueryParam.setKeyword("");
        pagingQueryParam.setOrders(new Order("createTime","desc"));

        PagingQuery pagingQuery = inflatePagingQuery(request, pagingQueryParam, COOKIE_PAGINATION_SIZE);

        User user = WebUtils.getUser(request, response);

        PagingData<Schema> schemaPagingData =  this.schemaService.pagingQuery(user,pagingQuery);

        PageInfo<Schema> pageInfo = new PageInfo<>(schemaPagingData.getItems());


      //  List<Schema> schemas = getSchemaService().query(user, pagingQuery);

       // PageInfo<Schema> pageInfo = new PageInfo<>(schemas);
        ResultTable result = pageTable(pageInfo.getList(), schemaPagingData.getTotal());
        return result;
    }


    @GetMapping(value = "/listTableSpace")
    @ResponseBody
    public List<Schema> listTableSpace() {


        List<Schema> schemas = schemaMapper.selectTableSpace();

        return schemas;
    }







}
