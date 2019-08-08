/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.VehicleDangerTotal;
import com.jeesite.modules.common.service.VehicleDangerTotalService;
import com.jeesite.modules.common.vo.VehicleDangerInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.entity.VehicleDanger;
import com.jeesite.modules.common.service.VehicleDangerService;

/**
 * 车辆出险记录表Controller
 *
 * @author liangtao
 * @version 2019-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/common/vehicleDanger")
public class VehicleDangerController extends BaseController {

    @Autowired
    private VehicleDangerService vehicleDangerService;

    @Autowired
    private VehicleDangerTotalService vehicleDangerTotalService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public VehicleDanger get(String id, boolean isNewRecord) {
        return vehicleDangerService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(VehicleDanger vehicleDanger, Model model) {
        model.addAttribute("vehicleDanger", vehicleDanger);
        return "modules/common/vehicleDangerList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<VehicleDanger> listData(VehicleDanger vehicleDanger, HttpServletRequest request, HttpServletResponse response) {
        vehicleDanger.setPage(new Page<>(request, response));
        Page<VehicleDanger> page = vehicleDangerService.findPage(vehicleDanger);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(VehicleDanger vehicleDanger, Model model) {
        model.addAttribute("vehicleDanger", vehicleDanger);
        return "modules/common/vehicleDangerForm";
    }

    /**
     * 保存车辆出险记录表
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated VehicleDanger vehicleDanger) {
        vehicleDangerService.save(vehicleDanger);
        return renderResult(Global.TRUE, text("保存车辆出险记录表成功！"));
    }

    /**
     * 删除车辆出险记录表
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(VehicleDanger vehicleDanger) {
        vehicleDangerService.delete(vehicleDanger);
        return renderResult(Global.TRUE, text("删除车辆出险记录表成功！"));
    }

    /***
     * @description: 加载出险记录
     * @param: [vehicleDangerTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/4
     * @time: 15:02
     */
    @RequestMapping(value = "findVehicleDanger")
    @ResponseBody
    public CommonResult findVehicleDanger(VehicleDangerTotal vehicleDangerTotal) {
        if (StringUtils.isBlank(vehicleDangerTotal.getId())) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        return vehicleDangerService.findVehicleDanger(vehicleDangerTotal);
    }

    /**
     * @description: 保存出险记录
     * @param: [vehicleDangerInfoVO]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 9:38
     */
    @RequestMapping(value = "saveVehicleDanger")
    @ResponseBody
    public CommonResult saveVehicleDanger(@RequestBody(required = true) VehicleDangerInfoVO vehicleDangerInfoVO) {
        if (StringUtils.isBlank(vehicleDangerInfoVO.getVin()) || StringUtils.isBlank(vehicleDangerInfoVO.getCarModel())) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "车型参数或VIN码为空");
        }
        if (StringUtils.isNotBlank(vehicleDangerInfoVO.getId())) {
            // 编辑
            VehicleDangerTotal vehicleDangerTotal = new VehicleDangerTotal();
            vehicleDangerTotal.setId(vehicleDangerInfoVO.getId());
            vehicleDangerTotal = vehicleDangerTotalService.get(vehicleDangerTotal);
            if (null == vehicleDangerTotal) {
                return new CommonResult(CodeConstant.REQUEST_FAILED, "空对象");
            }
            vehicleDangerService.saveVehicleDanger(vehicleDangerTotal, vehicleDangerInfoVO);
        } else {
            // 保存
            vehicleDangerService.saveVehicleDanger(null, vehicleDangerInfoVO);
        }
        return new CommonResult();
    }

}