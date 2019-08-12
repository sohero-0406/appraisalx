/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.common.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.entity.MaintenanceTotal;
import com.jeesite.modules.common.service.MaintenanceTotalService;

import java.util.List;

/**
 * 车辆维保总表Controller
 *
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Controller
@RequestMapping(value = "${adminPath}/common/maintenanceTotal")
public class MaintenanceTotalController extends BaseController {

    @Autowired
    private MaintenanceTotalService maintenanceTotalService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public MaintenanceTotal get(String id, boolean isNewRecord) {
        return maintenanceTotalService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(MaintenanceTotal maintenanceTotal, Model model) {
        model.addAttribute("maintenanceTotal", maintenanceTotal);
        return "modules/common/maintenanceTotalList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<MaintenanceTotal> listData(MaintenanceTotal maintenanceTotal, HttpServletRequest request, HttpServletResponse response) {
        maintenanceTotal.setPage(new Page<>(request, response));
        Page<MaintenanceTotal> page = maintenanceTotalService.findPage(maintenanceTotal);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(MaintenanceTotal maintenanceTotal, Model model) {
        model.addAttribute("maintenanceTotal", maintenanceTotal);
        return "modules/common/maintenanceTotalForm";
    }

    /**
     * 保存车辆维保总表
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated MaintenanceTotal maintenanceTotal) {
        maintenanceTotalService.save(maintenanceTotal);
        return renderResult(Global.TRUE, text("保存车辆维保总表成功！"));
    }

    /**
     * 删除车辆维保总表
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(MaintenanceTotal maintenanceTotal) {
        maintenanceTotalService.delete(maintenanceTotal);
        return renderResult(Global.TRUE, text("删除车辆维保总表成功！"));
    }

    /** 
    * @description: 加载维保记录全表
    * @param: [keyword]
    * @return: com.jeesite.modules.common.entity.CommonResult
    * @author: Jiangyf
    * @date: 2019/8/12 
    * @time: 11:27
    */
    @RequestMapping(value = "findMaintenanceTotalList")
    @ResponseBody
    public CommonResult findMaintenanceTotalList(String keyword) {
        CommonResult comRes = new CommonResult();
        List<MaintenanceTotal> maintenanceTotals = maintenanceTotalService.findMaintenanceTotalList(keyword);
        comRes.setData(maintenanceTotals);
        return comRes;
    }
    
    /** 
    * @description: 加载已保存维保记录(编辑时调用)
    * @param: [maintenanceTotal]
    * @return: com.jeesite.modules.common.entity.CommonResult
    * @author: Jiangyf
    * @date: 2019/8/7 
    * @time: 10:39
    */
    @RequestMapping(value = "findSavedMaintenance")
    @ResponseBody
    public CommonResult findSavedMaintenance(MaintenanceTotal maintenanceTotal) {
        if (StringUtils.isBlank(maintenanceTotal.getId())) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        return maintenanceTotalService.findSavedMaintenance(maintenanceTotal);
    }

    /**
    * @description: 删除维保记录
    * @param: [maintenanceTotal]
    * @return: com.jeesite.modules.common.entity.CommonResult
    * @author: Jiangyf
    * @date: 2019/8/6 
    * @time: 17:13
    */ 
    @RequestMapping(value = "deleteMaintenance")
    @ResponseBody
    public CommonResult deleteMaintenance(MaintenanceTotal maintenanceTotal) {
        if (StringUtils.isBlank(maintenanceTotal.getId())) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        return maintenanceTotalService.deleteMaintenance(maintenanceTotal, true);
    }
}