/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.service.CarInfoService;
import com.jeesite.modules.common.entity.*;
import com.jeesite.modules.common.service.MaintenanceService;
import com.jeesite.modules.common.service.VehicleDangerDetailService;
import com.jeesite.modules.common.service.VehicleDangerService;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.CheckTradableVehicles;
import com.jeesite.modules.aa.service.CheckTradableVehiclesService;


/**
 * 检查可交易车辆Controller
 *
 * @author lvchangwei
 * @version 2019-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/checkTradableVehicles")
public class CheckTradableVehiclesController extends BaseController {

    @Autowired
    private CheckTradableVehiclesService checkTradableVehiclesService;

    @Autowired
    private CarInfoService carInfoService;

    @Autowired
    private MaintenanceService maintenanceService;

    @Autowired
    private VehicleDangerService vehicleDangerService;

    @Autowired
    private VehicleDangerDetailService vehicleDangerDetailService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public CheckTradableVehicles get(String id, boolean isNewRecord) {
        return checkTradableVehiclesService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(CheckTradableVehicles checkTradableVehicles, Model model) {
        model.addAttribute("checkTradableVehicles", checkTradableVehicles);
        return "modules/aa/checkTradableVehiclesList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<CheckTradableVehicles> listData(CheckTradableVehicles checkTradableVehicles, HttpServletRequest request, HttpServletResponse response) {
        checkTradableVehicles.setPage(new Page<>(request, response));
        Page<CheckTradableVehicles> page = checkTradableVehiclesService.findPage(checkTradableVehicles);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(CheckTradableVehicles checkTradableVehicles, Model model) {
        model.addAttribute("checkTradableVehicles", checkTradableVehicles);
        return "modules/aa/checkTradableVehiclesForm";
    }

	/**
	 * 保存检查可交易车辆
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public CommonResult save(@Validated CheckTradableVehicles checkTradableVehicles) {
		ExamUser examUser = UserUtils.getExamUser();
		checkTradableVehiclesService.saveObj(examUser,checkTradableVehicles);
		return new CommonResult();
	}
	
	/**
	 * 删除检查可交易车辆
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CheckTradableVehicles checkTradableVehicles) {
		checkTradableVehiclesService.delete(checkTradableVehicles);
		return renderResult(Global.TRUE, text("删除检查可交易车辆成功！"));
	}

    /**
     * 加载可交易车辆判别信息
     */
    @RequestMapping(value = "getDetail")
    @ResponseBody
    public CommonResult getDetail() {
        ExamUser examUser = UserUtils.getExamUser();
        CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
        checkTradableVehicles.setExamUserId(examUser.getId());
        checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);
        CommonResult result = new CommonResult();
        result.setData(checkTradableVehicles);
        return result;
    }

	/**
	 * 保存事故车判定信息
	 */
	@PostMapping(value = "saveIsAccident")
	@ResponseBody
	public CommonResult saveIsAccident(@Validated CheckTradableVehicles checkTradableVehicles) {
		ExamUser examUser = UserUtils.getExamUser();
		checkTradableVehiclesService.saveIsAccident(examUser,checkTradableVehicles);
		return new CommonResult();
	}

    /**
     * @description: 加载维保记录信息
     * @param: []
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/7
     * @time: 16:43
     */
    @RequestMapping(value = "findMaintenanceRecord")
    @ResponseBody
    public CommonResult findMaintenanceRecord() {
        ExamUser examUser = UserUtils.getExamUser();
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        // 根据考生用户获取车辆信息的VIN码
        String vinCode = carInfoService.getByEntity(carInfo).getVinCode();
        if (StringUtils.isBlank(vinCode)) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "VIN码为空");
        }
        MaintenanceTotal maintenanceTotal = new MaintenanceTotal();
        maintenanceTotal.setVinCode(vinCode);
        return maintenanceService.findMaintenance(maintenanceTotal);
    }

    /**
     * @description: 加载维保记录信息详情
     * @param: [maintenanceTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/7
     * @time: 17:46
     */
    @RequestMapping(value = "findMaintenanceRecordDetail")
    @ResponseBody
    public CommonResult findMaintenanceRecordDetail(Maintenance maintenance) {
        if (StringUtils.isBlank(maintenance.getId())) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        return maintenanceService.findMaintenanceDetail(maintenance);
    }

    /**
     * @description: 加载出险记录信息
     * @param: []
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/7
     * @time: 16:46
     */
    @RequestMapping(value = "findVehicleDangerRecord")
    @ResponseBody
    public CommonResult findVehicleDangerRecord() {
        ExamUser examUser = UserUtils.getExamUser();
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        // 根据考生用户获取车辆信息的VIN码
        String vinCode = carInfoService.getByEntity(carInfo).getVinCode();
        if (StringUtils.isBlank(vinCode)) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "VIN码为空");
        }
        VehicleDangerTotal vehicleDangerTotal = new VehicleDangerTotal();
        vehicleDangerTotal.setVinCode(vinCode);
        return vehicleDangerService.findVehicleDanger(vehicleDangerTotal);
    }

    /**
     * @description: 加载出险记录详情 - 理赔详情
     * @param: [vehicleDanger]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/7
     * @time: 18:01
     */
    @RequestMapping(value = "findVehicleDangerRecordDetail")
    @ResponseBody
    public CommonResult findVehicleDangerRecordDetail(VehicleDanger vehicleDanger) {
        CommonResult commonResult = new CommonResult();
        if (StringUtils.isBlank(vehicleDanger.getId())) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "参数为空");
        }
        commonResult.setData(vehicleDangerDetailService.findVehicleDangerDetail(vehicleDanger));
        return commonResult;
    }
}