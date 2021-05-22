package com.ctjsoft.orderserver.order.controller;


import com.ctjsoft.orderserver.order.domain.*;
import com.ctjsoft.orderserver.order.service.*;
import com.ctjsoft.orderserver.schedule.IScheduleService;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.DataRightUtils;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.ResuTree;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.regexp.internal.RE;
import org.datagear.management.domain.Role;
import org.datagear.management.domain.Schema;
import org.datagear.management.domain.User;
import org.datagear.management.service.SchemaService;
import org.datagear.persistence.Query;
import org.datagear.web.config.CoreConfig;
import org.datagear.web.controller.AbstractSchemaConnTableController;
import org.datagear.web.util.OperationMessage;
import org.datagear.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工单管理Controller
 *
 * @author zzz
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/system/orders")
public class OrdersController extends AbstractSchemaConnTableController {
    private String prefix = "system/orders";

    @Autowired
    CoreConfig coreConfig;

    @Autowired
    private IOrdersService ordersService;
    @Autowired
    private IOrderReviewLogService orderReviewLogService;

    @Autowired
    private IScheduleService scheduleService;
    @Autowired
    private IOrderSqlLogService orderSqlLogService;

    @Autowired
    private ICronJobService cronJobService;
    @Autowired
    private ICronJobLogService cronJobLogService;
    @Autowired
    private SchemaService schemaService;

    public OrdersController() {
        super();
    }

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/orders/main','system:orders:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询工单管理列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/orders/data','system:orders:data')")
    public ResultTable list(@ModelAttribute Orders orders, PageDomain pageDomain) throws IOException {
        PageInfo<Orders> pageInfo = ordersService.selectOrdersPage(orders, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }


    @ResponseBody
    @GetMapping("/getUserRight")
    //@PreAuthorize("hasPermission('/system/orders/data','system:orders:data')")
    public Result getUserRight() throws IOException {
        User user = WebUtils.getUser();
        Boolean flag = false;
        if (!DataRightUtils.getUserDataType(user)) { //false则只有自己工单的权限；设置creator/updateor 为自己
            flag = true;
        }

        return Result.success(flag);
    }

    /**
     * 查询工单管理列表
     */
    @ResponseBody
    @GetMapping("/linkData")
    //@PreAuthorize("hasPermission('/system/orders/data','system:orders:data')")
    public ResultTable linkData(@ModelAttribute OrdersVo ordersVo, @RequestParam(value = "flowRightType", required = false) String flowRightType, PageDomain pageDomain) throws IOException {
       /* User user = WebUtils.getUser();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取reuqest
        HttpServletRequest request = attributes.getRequest();
        //获取session
        HttpSession sess = request.getSession();
        //  sess.setAttribute("a", "路");
        for (String name : sess.getValueNames()) {
            System.out.println(name);

        }
        System.out.println("123:"+sess.getAttribute("a"));*/


        PageInfo<OrdersVo> pageInfo = ordersService.selectOrdersLinkPage(ordersVo, pageDomain, flowRightType);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 查询工单管理列表
     */
    @ResponseBody
    @GetMapping("/linkDetail")
    //@PreAuthorize("hasPermission('/system/orders/data','system:orders:data')")
    public Result linkDetail(@ModelAttribute OrdersVo ordersVo) throws IOException {
        OrdersVo pageInfo = ordersService.linkDetail(ordersVo);
        return Result.success(pageInfo);
    }


    /**
     * 查询工单管理列表
     */
    @ResponseBody
    @GetMapping("/selectOneOrdersLinkDetail")
    //@PreAuthorize("hasPermission('/system/orders/data','system:orders:data')")
    public Result selectOneOrdersLinkDetail(@ModelAttribute OrdersVo ordersVo) throws IOException {
        OrdersVo pageInfo = ordersService.selectOneOrdersLinkDetail(ordersVo);
        return Result.success(pageInfo);
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/flow/data','system:flow:data')")
    public Result list(@ModelAttribute Orders orders) {
        Result result = new Result();
        List<Orders> list = ordersService.selectOrdersList(orders);
        result.setSuccess(true);
        result.setCode(200);
        result.setData(list);
        return result;
    }

    /**
     * 新增工单管理
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/orders/add','system:orders:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存工单管理
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/orders/add','system:orders:add')")
    public Result save(@RequestBody Orders orders, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "status", required = false) String status, @RequestParam(value = "info", required = false) String info) throws InterruptedException {
        orders.setId(StringUtils.getUUid());
        orders.setCreateorId(WebUtils.getUser().getId());
        orders.setCreateTime(new Date());
        orders.setUpdateTime(new Date());

        User user = WebUtils.getUser();
        Date date = new Date();
        if (orders.getReviewStatus().equals("1")) {
            //保存及送审
            orders.setPusherId(user.getId());
            orders.setReviewStatus("1");
            Thread.currentThread().sleep(1000);
            orders.setPushTime(new Date());
            //
            orderReviewLogService.insertOrderReviewLog(new OrderReviewLog(StringUtils.getUUid(), "0", orders.getId(), user.getId(), date, user.getName(), orders.getName(), info));
        } else {
            //仅保存
        }

        orderReviewLogService.insertOrderReviewLog(new OrderReviewLog(StringUtils.getUUid(), orders.getReviewStatus(), orders.getId(), user.getId(), date, user.getName(), orders.getName(), info));

        return decide(ordersService.insertOrders(orders));
    }

    /**
     * 新增保存工单管理
     */
    @ResponseBody
    @PostMapping("/analyzeSqlFile")
    //@PreAuthorize("hasPermission('/system/orders/add','system:orders:add')")
    public Result analyzeSqlFile(@RequestParam(value = "dbId", required = false) String dbId,
                                 @RequestParam("file") MultipartFile file,
                                 HttpServletRequest request) throws IOException, SQLException {
        String contentType = file.getContentType();
        System.out.println(contentType);
        InputStream inputStream = file.getInputStream();
        BufferedReader reader;

        Map<String, String> map = new HashMap<>();
        map.put("msg", "检验通过");
        map.put("status", "1");

        Schema schema = getSchemaNotNull(dbId);
        Connection ct = getSchemaConnection(schema);
        ct.setAutoCommit(false);
        Statement sm = ct.createStatement();

        reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String line;
        int sqlCount = 0;

        try {

            line = reader.readLine().replace("；", "");
            Integer emptyCount = 0;
            while (line != null) {
                if (!line.contains(";")) {
                    String tmp = reader.readLine();
                    if (tmp != null) {
                        line += tmp;
                    }
                    ++emptyCount;
                    if (emptyCount < 10) {
                        continue;
                    }
                }
                line = line.replace(";", "");
                sm.addBatch(line);
                System.out.println("-----------------------");
                System.out.println(line);
                System.out.println("-----------------------");

                sqlCount++;
                sm.executeBatch();
                line = reader.readLine();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            map.put("msg", "检验不通过");
            map.put("errofInfo", e.getMessage());
            map.put("status", "2");
        } finally {
            ct.rollback();
            sm.close();
            ct.close();
        }

        map.put("sqlCount", sqlCount + "");
        map.put("fileId", StringUtils.getUUid());
        map.put("fileName", file.getOriginalFilename());
        reader.close();
        return Result.success(map);
    }


    /**
     * 新增保存工单管理
     */
    @ResponseBody
    @PostMapping("/checkSql")
    //@PreAuthorize("hasPermission('/system/orders/add','system:orders:add')")
    public Result checkSql(@RequestBody Orders orders) throws SQLException {
        Result result = new Result();
        result.setSuccess(true);
        result.setData(checkQuerySql(orders));
        return result;
    }

    private Map<String, String> checkQuerySql(Orders orders) throws SQLException {
        Map<String, String> map = new HashMap<>();
        map.put("msg", "检验通过");
        map.put("status", "1");

        if (orders.getForbidWords() != null && orders.getForbidWords() != "") {
            List<String> forbidWordList = Arrays.asList(orders.getForbidWords().split(","));
            String reg = "";
            for (String word : forbidWordList) {
                if (reg.length() > 0) {
                    reg += "|(\\b" + word + "\\b)";
                } else {
                    reg += "(\\b" + word + "\\b)";
                }
            }
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(orders.getSqls().toLowerCase());
            if (matcher.find()) {
                map.put("msg", "检验不通过");
                map.put("errofInfo", "存在不被允许的标识符，请检查申请类型与语句");
                map.put("status", "2");
                return map;
            }
        }

     /*   Connection ct = null;
        Statement sm = null;
        try {
            ct = getSchemaConnection(getSchemaNotNull(orders.getDbId()));
            ct.setAutoCommit(false);
            sm = ct.createStatement();
            String sqls = orders.getSqls().replace("；", ";").replace("，", ";");
            String tmp = sqls.replace(" ", "").replace("\n", "");

            List<String> sqlList = Arrays.asList(sqls.split(";"));
            Integer length = sqlList.size();
            if (length != 1 && tmp.substring(tmp.length() - 1, tmp.length()).equals(";")) {
                length--;
            }

            for (int i = 0; i < length; i++) {
                sm.executeUpdate(sqlList.get(i));
                // sm.execute(sqlList.get(i));
            }
        } catch (SQLException throwables) {
            map.put("msg", "检验不通过");
            map.put("errofInfo", throwables.getMessage());
            map.put("status", "2");
            throwables.printStackTrace();
        } finally {
            ct.rollback();
            sm.close();
            ct.close();
        }*/

        return map;
    }


  /*  @ResponseBody
    @PostMapping("/execSql")
    public Result execSql(@RequestParam(value = "id", required = false) String id, @RequestParam(value = "cronJobId", required = false) String cronJobId) throws SQLException  */

    public Result execSql(String id, String cronJobId) throws SQLException {
        Result result = new Result();
        result.setSuccess(true);
        Orders orders = ordersService.selectOrdersById(id);
        Map<String, String> execMap = connectAndExec(orders, cronJobId);
        String errofInfo = (execMap.get("errofInfo") == null || execMap.get("errofInfo").equals("")) ? "" : execMap.get("errofInfo");

        if (cronJobId != null && !cronJobId.equals("")) {
            CronJobLog cronJobLog = new CronJobLog();
            cronJobLog.setId(StringUtils.getUUid());
            cronJobLog.setCronJobId(cronJobId);
            cronJobLog.setCreateTime(new Date());
            cronJobLog.setStatus(execMap.get("status"));
            cronJobLog.setResulrInfo(execMap.get("msg") + execMap.get("errofInfo"));
            cronJobLogService.insertCronJobLog(cronJobLog);
        }
        result.setData(execMap);
        return result;
    }

    private Map<String, String> connectAndExec(Orders orders, String cronJobId) throws SQLException {
        OrderSqlLog orderSqlLog = new OrderSqlLog();
        orderSqlLog.setOrderId(orders.getId());
        orderSqlLog.setSqls(orders.getSqls());
        orderSqlLog.setSqls(orders.getSqls());
        orderSqlLog.setFlowId(orders.getFlowId());
        orderSqlLog.setCronJobId(cronJobId);
        orderSqlLog.setSchemaId(orders.getDbId());
        Map<String, String> map = new HashMap<>();
        map.put("msg", "执行通过");
        map.put("status", "1");
        Integer resultCount = 0;

        if (orders.getForbidWords() != null && orders.getForbidWords() != "") {
            List<String> forbidWordList = Arrays.asList(orders.getForbidWords().split(","));
            String reg = "";
            for (String word : forbidWordList) {
                if (reg.length() > 0) {
                    reg += "|(\\b" + word + "\\b)";
                } else {
                    reg += "(\\b" + word + "\\b)";
                }
            }
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(orders.getSqls().toLowerCase());
            if (matcher.find()) {
                map.put("msg", "执行不通过");
                map.put("errofInfo", "存在不被允许的标识符，请检查申请类型与语句");
                map.put("status", "2");
                return map;
            }
        }
        //  String reg = "(\\bdelete\\b)|(\\binsert\\b)|(\\bupdate\\b)|(\\balter\\b)|(\\bcreate\\b)|(\\bdrop\\b)|(\\btruncate\\b)";

        Connection ct = null;
        Statement sm = null;
        try {
            ct = getSchemaConnection(getSchemaNotNull(orders.getDbId()));
            ct.setAutoCommit(false);
            sm = ct.createStatement();
            String sqls = orders.getSqls().replace("；", ";").replace("，", ";");
            String tmp = sqls.replace(" ", "").replace("\n", "");

            List<String> sqlList = Arrays.asList(sqls.split(";"));
            Integer length = sqlList.size();
            if (length != 1 && tmp.substring(tmp.length() - 1, tmp.length()).equals(";") && length > 1) {
                length--;
            }
            for (int i = 0; i < length; i++) {
                //System.out.println("qqq");
                //    sm.executeUpdate(sqlList.get(i));
                //  sm.execute(sqlList.get(i));

                //  resultCount+= sm.getUpdateCount();

                resultCount += sm.executeUpdate(sqlList.get(i));
            }
            map.put("resultCount", resultCount + "");
            orderSqlLog.setInfo(resultCount + "");
            orders.setExecuteStatus("1");
        } catch (SQLException throwables) {
            map.put("msg", "执行不通过");
            map.put("errofInfo", throwables.getMessage());
            map.put("status", "2");
            orders.setExecuteStatus("0");
            orderSqlLog.setInfo(throwables.getMessage());
            throwables.printStackTrace();
        } finally {
            //   ct.rollback();
            sm.close();
            ct.close();

            ordersService.updateOrders(orders);
            orderSqlLogService.insertOrderSqlLog(orderSqlLog);
        }

        return map;
    }

    /**
     * 修改工单管理
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result edit(String id) {
        Orders orders = ordersService.selectOrdersById(id);
        Result result = new Result();
        result.setCode(200);
        result.setData(orders);
        result.setSuccess(true);
        return result;
    }

    /*    *//**
     * 修改保存工单管理
     *//*
    @ResponseBody
    @PutMapping("/exec")
    public Result exec(@RequestBody Orders orders, @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status, @RequestParam(value = "info", required = false) String info) {

        orders = ordersService.selectOrdersById(orders.getId());
        if (type.equals("exec") && orders.getReviewStatus().equals("2") && orders.getIsSchedule() != null && orders.getIsSchedule().equals("1")) {

            CronJob cronJob = new CronJob();
            cronJob.setId(StringUtils.getUUid());
            cronJob.setCronId(orders.getCron());
            cronJob.setOrderId(orders.getId());
            cronJob.setCreatorId(WebUtils.getUser().getId());
            cronJob.setCreateTime(new Date());
            cronJob.setStatus("1");
            cronJobService.insertCronJob(cronJob);
            scheduleService.startJobsByCronJobId(cronJob);
        } else if (type.equals("exec") && orders.getReviewStatus().equals("2")) {
            try {
                connectAndExec(ordersService.selectOrdersById(orders.getId()),null);
                //  ordersService.updateOrders(orders);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failure(e.getMessage());
            }
        }


        return Result.success();
    }*/

    /**
     * 修改保存工单管理
     */
    @ResponseBody
    @PutMapping("/update")
    public Result update(@RequestBody Orders orders, @RequestParam(value = "type", required = false) String type,
                         @RequestParam(value = "status", required = false) String status, @RequestParam(value = "info", required = false) String info) {
        User user = WebUtils.getUser();
        Orders tmp = ordersService.selectOrdersById(orders.getId());
        if (type == null) {

        } else if (type.equals("push")) {
            orders.setPusherId(user.getId());
            orders.setReviewStatus("1");
            orders.setPushTime(new Date());

            orderReviewLogService.insertOrderReviewLog(new OrderReviewLog(StringUtils.getUUid(), "1", orders.getId(), user.getId(), new Date(), user.getRealName(), tmp.getName(), info));
        } else if (type.equals("review")) {
            orders.setReviewerId(user.getId());
            orders.setReviewTime(new Date());
            orderReviewLogService.insertOrderReviewLog(new OrderReviewLog(StringUtils.getUUid(), "2", orders.getId(), user.getId(), new Date(), user.getRealName(), tmp.getName(), info));
        } else if (type.equals("back")) {
            orders.setReviewerId(user.getId());
            orders.setReviewTime(new Date());
            orderReviewLogService.insertOrderReviewLog(new OrderReviewLog(StringUtils.getUUid(), "3", orders.getId(), user.getId(), new Date(), user.getRealName(), tmp.getName(), info));
        } else if (type.equals("exec")) {
            orders.setExecutorId(user.getId());
            orders.setExecuteTime(new Date());
            orderReviewLogService.insertOrderReviewLog(new OrderReviewLog(StringUtils.getUUid(), "4", orders.getId(), user.getId(), new Date(), user.getRealName(), tmp.getName(), info));

            String reviewStatus = tmp.getReviewStatus();
            String isSchedule = tmp.getIsSchedule();
            if (type.equals("exec") && reviewStatus != null && ( reviewStatus.equals("2") || reviewStatus.equals("4")  ) && isSchedule != null && isSchedule.equals("1")) { // 审核通过且类型为执行且是定时任务

                CronJob cronJob = new CronJob();
                cronJob.setId(StringUtils.getUUid());
                cronJob.setCronId(orders.getCron());
                cronJob.setOrderId(orders.getId());
                cronJob.setCreatorId(WebUtils.getUser().getId());
                cronJob.setCreateTime(new Date());
                cronJob.setStatus("1");
                cronJobService.insertCronJob(cronJob);
                scheduleService.startJobsByCronJobId(cronJob);
            } else if (type.equals("exec") && reviewStatus != null &&   ( reviewStatus.equals("2") || reviewStatus.equals("4")  )   ) {   // 审核通过且类型为执行且但不是定时任务，则此时执行
                try {
                    connectAndExec(ordersService.selectOrdersById(tmp.getId()), null);
                    //  ordersService.updateOrders(orders);
                } catch (Exception e) {
                    e.printStackTrace();
                    return Result.failure(e.getMessage());
                }
            }

        }

        if (status != null && !status.equals("")) {
            orders.setReviewStatus(status);
        }
        orders.setUpdateTime(new Date());
        ordersService.updateOrders(orders);

        orders = ordersService.selectOrdersById(orders.getId());
        /*if (type.equals("review") && orders.getReviewStatus().equals("2") && orders.getIsSchedule() != null && orders.getIsSchedule().equals("1")) {

            CronJob cronJob = new CronJob();
            cronJob.setId(StringUtils.getUUid());
            cronJob.setCronId(orders.getCron());
            cronJob.setOrderId(orders.getId());
            cronJob.setCreatorId(WebUtils.getUser().getId());
            cronJob.setCreateTime(new Date());
            cronJob.setStatus("1");
            cronJobService.insertCronJob(cronJob);
            scheduleService.startJobsByCronJobId(cronJob);
        } else if (type.equals("review") && orders.getReviewStatus().equals("2")) {
            try {
                connectAndExec(ordersService.selectOrdersById(orders.getId()),null);
                //  ordersService.updateOrders(orders);
            } catch (Exception e) {
                e.printStackTrace();
                return Result.failure(e.getMessage());
            }
        }
*/
        return Result.success();
    }

    /* *//**
     * 修改保存工单管理
     *//*
    @ResponseBody
    @PutMapping("/push")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result push(@RequestBody Orders orders) {
        User user = WebUtils.getUser();
        orders.setPusherId(user.getId());
        orders.setPushTime(new Date());
        return decide(ordersService.updateOrders(orders));
    }

    *//**
     * 修改保存工单管理
     *//*
    @ResponseBody
    @PutMapping("/review")
    //@PreAuthorize("hasPermission('/system/orders/edit','system:orders:edit')")
    public Result review(@RequestBody Orders orders) {
        User user = WebUtils.getUser();
        orders.setReviewerId(user.getId());
        orders.setReviewTime(new Date());
        return decide(ordersService.updateOrders(orders));
    }*/


    /**
     * 删除工单管理
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/orders/remove','system:orders:remove')")
    public Result batchRemove(String ids) {
        return decide(ordersService.deleteOrdersByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/orders/remove','system:orders:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(ordersService.deleteOrdersById(id));
    }


    /**
     * 成功操作
     */
    public Result success() {

        return Result.success();
    }

    /**
     * 成功操作
     */
    public Result success(String msg) {

        return Result.success(msg);
    }

    /**
     * 成功操作
     */
    public Result success(Object data) {

        return Result.success(data);
    }

    /**
     * 成功操作
     */
    public Result success(String msg, Object data) {

        return Result.success(msg, data);
    }

    /**
     * 成功操作
     */
    public Result success(int code, String message, Object data) {

        return Result.success(code, message, data);
    }

    /**
     * 失败操作
     */
    public Result failure() {

        return Result.failure();
    }

    /**
     * 失败操作
     */
    public Result failure(String msg) {

        return Result.failure(msg);
    }

    /**
     * 失败操作
     */
    public Result failure(String msg, Object data) {

        return Result.failure(msg, data);
    }

    /**
     * 失败操作
     */
    public Result failure(int code, String msg, Object data) {

        return Result.failure(code, msg, data);
    }

    /**
     * 选择返回
     */
    public Result decide(Boolean b) {

        return Result.decide(b);
    }

    /**
     * 选择返回
     */
    public Result decide(Boolean b, String success, String failure) {

        return Result.decide(b, success, failure);
    }

    /**
     * 选择返回
     */
    public Result decide(int result) {
        if (result > 0) {
            return Result.decide(true);
        } else {
            return Result.decide(false);
        }
    }

    /**
     * 选择返回
     */
    public Result decide(int result, String success, String failure) {
        if (result > 0) {
            return Result.decide(true, success, failure);
        } else {
            return Result.decide(false, success, failure);
        }
    }

    /**
     * 页面跳转
     */
    public ModelAndView JumpPage(String path) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(path);
        return modelAndView;
    }

    /**
     * 带参数的页面跳转
     */
    public ModelAndView JumpPage(String path, Map<String, ?> params) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(path);
        modelAndView.addAllObjects(params);

        return modelAndView;
    }

    /**
     * Describe: 返回 Tree 数据
     * Param data
     * Return Tree数据
     */
    protected static ResuTree dataTree(Object data) {
        ResuTree resuTree = new ResuTree();
        resuTree.setData(data);
        return resuTree;
    }

    /**
     * Describe: 返回数据表格数据 分页
     * Param data
     * Return 表格分页数据
     */
    public static ResultTable pageTable(Object data, long count) {

        return ResultTable.pageTable(count, data);
    }

    /**
     * Describe: 返回数据表格数据
     * Param data
     * Return 表格分页数据
     */
    protected static ResultTable dataTable(Object data) {

        return ResultTable.dataTable(data);
    }

    /**
     * Describe: 返回树状表格数据 分页
     * Param data
     * Return 表格分页数据
     */
    protected static ResultTable treeTable(Object data) {

        return ResultTable.dataTable(data);
    }

}
