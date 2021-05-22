package com.ctjsoft.orderserver.order.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctjsoft.orderserver.order.domain.Drivers;
import com.ctjsoft.orderserver.order.domain.Flow;
import com.ctjsoft.orderserver.order.service.IDriversService;
import com.ctjsoft.orderserver.order.service.IDriversService;
import com.ctjsoft.orderserver.utils.BaseController;
import com.ctjsoft.orderserver.utils.Convert;
import com.ctjsoft.orderserver.utils.StringUtils;
import com.ctjsoft.orderserver.utils.request.PageDomain;
import com.ctjsoft.orderserver.utils.response.Result;
import com.ctjsoft.orderserver.utils.response.ResultTable;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


/**
 * 流程管理Controller
 *
 * @author zzz
 * @date 2021-03-10
 */
@RestController
@RequestMapping("/system/drivers")
public class DriversController extends BaseController {
    private String prefix = "system/drivers";

    @Autowired
    private IDriversService driversService;

    @GetMapping("/main")
    //@PreAuthorize("hasPermission('/system/drivers/main','system:drivers:main')")
    public ModelAndView main() {
        return JumpPage(prefix + "/main");
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/data")
    //@PreAuthorize("hasPermission('/system/drivers/data','system:drivers:data')")
    public ResultTable data(@ModelAttribute Drivers drivers, PageDomain pageDomain) {
        PageInfo<Drivers> pageInfo = driversService.selectDriversPage(drivers, pageDomain);
        return pageTable(pageInfo.getList(), pageInfo.getTotal());
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/list")
    //@PreAuthorize("hasPermission('/system/drivers/data','system:drivers:data')")
    public Result list(@ModelAttribute Drivers drivers) {
        Result result = new Result();
        List<Drivers> list = driversService.selectDriversList(drivers);
        result.setSuccess(true);
        result.setCode(200);
        result.setData(list);
        return result;
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/getDetail")
    public Result getDetail(@ModelAttribute Drivers drivers, @RequestParam(value = "url", required = false) String url) {
        Result result = new Result();
        String className = url.substring(url.indexOf(":") + 1, url.indexOf(":", url.indexOf(":") + 1));
        List<Drivers> list = driversService.selectDriversList(drivers,className);
        result.setSuccess(true);
        result.setCode(200);
        result.setData(list);
        return result;
    }

    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/import")
    public Result importFromFile(@ModelAttribute Drivers drivers, @RequestParam(value = "url", required = false) String url) {
        Result result = new Result();
        String className = url.substring(url.indexOf(":") + 1, url.indexOf(":", url.indexOf(":") + 1));
        List<Drivers> list = driversService.selectDriversList(drivers);
        List<Drivers> tmpList = new ArrayList<>();
        for (Drivers tmp : list) {
            if (tmp.getName().toUpperCase().contains(className.toUpperCase())
                    || tmp.getDriverclassnames().toUpperCase().contains(className.toUpperCase())) {
                tmpList.add(tmp)
                ;
            }
        }
        result.setSuccess(true);
        result.setCode(200);
        result.setData(tmpList);
        return result;
    }


    /**
     * 查询流程管理列表
     */
    @ResponseBody
    @GetMapping("/constuctList")
    //@PreAuthorize("hasPermission('/system/drivers/data','system:drivers:data')")
    public Result constuctList(@ModelAttribute Drivers drivers) {
        Result result = new Result();
        List<Drivers> list = driversService.selectDriversList(drivers);
        List<JSONObject> tmp = new ArrayList<>();
        for (Drivers drivers1 : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", drivers1.getId());
            jsonObject.put("name", drivers1.getName());
            List<String> list1 = Arrays.asList(drivers1.getDriverclassnames().replace("，", ",").split(","));
            jsonObject.put("driverClassNames", list1);


            Map<String, Object> urlTemplate = new HashMap<>();
            Map<String, Object> defaultValue = new HashMap<>();
            defaultValue.put("port", drivers1.getPort());


            urlTemplate.put("template", drivers1.getTemplate());
            urlTemplate.put("defaultValue", defaultValue);


            jsonObject.put("urlTemplate", urlTemplate);
            tmp.add(jsonObject);
        }
        result.setSuccess(true);
        result.setCode(200);
        result.setData(tmp);
        return result;
    }


    /**
     * 新增流程管理
     */
    @GetMapping("/add")
    //@PreAuthorize("hasPermission('/system/drivers/add','system:drivers:add')")
    public ModelAndView add() {
        return JumpPage(prefix + "/add");
    }

    /**
     * 新增保存流程管理
     */
    @ResponseBody
    @PostMapping("/save")
    //@PreAuthorize("hasPermission('/system/drivers/add','system:drivers:add')")
    public Result save(@RequestBody Drivers drivers) {
        drivers.setId(StringUtils.getUUid());
        return decide(driversService.insertDrivers(drivers));
    }
    /**
     * 新增保存流程管理
     */
    @ResponseBody
    @PostMapping("/batchSave")
    //@PreAuthorize("hasPermission('/system/drivers/add','system:drivers:add')")
    public Result batchSave(@RequestBody List<Drivers> drivers) {
        for (Drivers tmp : drivers) {
            tmp.setId(StringUtils.getUUid());
            driversService.insertDrivers(tmp);
        }
        return Result.success();
    }

    /**
     * 修改流程管理
     */
/*    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/drivers/edit','system:drivers:edit')")
    public ModelAndView edit(String id, ModelMap mmap) {
        Drivers drivers = driversService.selectDriversById(id);
        mmap.put("drivers", drivers);
        return JumpPage(prefix + "/edit");
    }*/


    /**
     * 修改流程管理
     */
    @GetMapping("/edit")
    //@PreAuthorize("hasPermission('/system/drivers/edit','system:drivers:edit')")
    public ModelAndView edit(String id, ModelMap mmap) {
        Drivers drivers = driversService.selectDriversById(id);
        mmap.put("drivers", drivers);
        return JumpPage(prefix + "/edit");
    }

    /**
     * 修改保存流程管理
     */
    @ResponseBody
    @PutMapping("/update")
    //@PreAuthorize("hasPermission('/system/drivers/edit','system:drivers:edit')")
    public Result update(@RequestBody Drivers drivers) {
        return decide(driversService.updateDrivers(drivers));
    }

    /**
     * 删除流程管理
     */
    @ResponseBody
    @DeleteMapping("/batchRemove")
    //@PreAuthorize("hasPermission('/system/drivers/remove','system:drivers:remove')")
    public Result batchRemove(String ids) {
        return decide(driversService.deleteDriversByIds(Convert.toStrArray(ids)));
    }

    /**
     * 删除
     */
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    //@PreAuthorize("hasPermission('/system/drivers/remove','system:drivers:remove')")
    public Result remove(@PathVariable("id") String id) {
        return decide(driversService.deleteDriversById(id));
    }
}
