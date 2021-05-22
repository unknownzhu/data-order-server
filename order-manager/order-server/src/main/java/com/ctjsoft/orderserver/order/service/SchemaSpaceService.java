/*
package com.ctjsoft.orderserver.order.service;


import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.datagear.management.domain.Schema;
import org.datagear.management.domain.User;
import org.datagear.persistence.Order;
import org.datagear.persistence.PagingQuery;
import org.datagear.web.controller.AbstractSchemaConnTableController;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.ctjsoft.orderserver.order.controller.OrdersController.pageTable;
import static org.datagear.web.controller.SchemaController.COOKIE_PAGINATION_SIZE;

@Service
public class SchemaSpaceService extends AbstractSchemaConnTableController {



    public ResultTable list(HttpServletRequest request,HttpServletResponse response,  PagingQuery pagingQuery,   Schema schema, PageDomain pageDomain) {


        User user = WebUtils.getUser(request, response);

        List<Schema> schemas = getSchemaService().query(user, pagingQuery);

        PageInfo<Schema> pageInfo = new PageInfo<>(schemas);
        ResultTable result = pageTable(pageInfo.getList(), pageInfo.getTotal());
        return result;
    }
}
*/
