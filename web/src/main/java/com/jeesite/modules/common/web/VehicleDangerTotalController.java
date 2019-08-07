/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.modules.common.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.service.VehicleDangerTotalService;

/**
 * 车辆出险总表Controller
 *
 * @author liangtao
 * @version 2019-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/common/vehicleDangerTotal")
public class VehicleDangerTotalController extends BaseController {

    @Autowired
    private VehicleDangerTotalService vehicleDangerTotalService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public VehicleDangerTotal get(String id, boolean isNewRecord) {
        return vehicleDangerTotalService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(VehicleDangerTotal vehicleDangerTotal, Model model) {
        model.addAttribute("vehicleDangerTotal", vehicleDangerTotal);
        return "modules/common/vehicleDangerTotalList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<VehicleDangerTotal> listData(VehicleDangerTotal vehicleDangerTotal, HttpServletRequest request, HttpServletResponse response) {
        vehicleDangerTotal.setPage(new Page<>(request, response));
        Page<VehicleDangerTotal> page = vehicleDangerTotalService.findPage(vehicleDangerTotal);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(VehicleDangerTotal vehicleDangerTotal, Model model) {
        model.addAttribute("vehicleDangerTotal", vehicleDangerTotal);
        return "modules/common/vehicleDangerTotalForm";
    }

    /**
     * 保存车辆出险总表
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated VehicleDangerTotal vehicleDangerTotal) {
        vehicleDangerTotalService.save(vehicleDangerTotal);
        return renderResult(Global.TRUE, text("保存车辆出险总表成功！"));
    }

    /**
     * 删除车辆出险总表
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(VehicleDangerTotal vehicleDangerTotal) {
        vehicleDangerTotalService.delete(vehicleDangerTotal);
        return renderResult(Global.TRUE, text("删除车辆出险总表成功！"));
    }

    /**
     * @description: 加载出险记录全表
     * @param: []
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/6
     * @time: 14:26
     */
    @RequestMapping(value = "findVehicleDangerTotalList")
    @ResponseBody
    public CommonResult findVehicleDangerTotalList() {
        CommonResult comRes = new CommonResult();
        comRes.setData(vehicleDangerTotalService.findList(new VehicleDangerTotal()));
        return comRes;
    }

    /**
    * @description: 加载已保存出险记录(编辑时调用)
    * @param: [vehicleDangerTotal]
    * @return: com.jeesite.modules.common.entity.CommonResult
    * @author: Jiangyf
    * @date: 2019/8/7
    * @time: 14:09
    */
    @RequestMapping(value = "findSavedVehicleDanger")
    @ResponseBody
    public CommonResult findSavedVehicleDanger(VehicleDangerTotal vehicleDangerTotal) {
        if (null == vehicleDangerTotal.getId()) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        return vehicleDangerTotalService.findSavedVehicleDanger(vehicleDangerTotal);
    }

    /**
     * @description: 删除出险记录
     * @param: [vehicleDangerTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/6
     * @time: 17:30
     */
    @RequestMapping(value = "deleteVehicleDanger")
    @ResponseBody
    public CommonResult deleteVehicleDanger(VehicleDangerTotal vehicleDangerTotal) {
        if (null == vehicleDangerTotal.getId()) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        return vehicleDangerTotalService.deleteVehicleDanger(vehicleDangerTotal, true);
    }
}