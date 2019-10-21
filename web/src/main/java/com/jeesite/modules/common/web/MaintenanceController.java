/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.MaintenanceTotal;
import com.jeesite.modules.common.service.MaintenanceTotalService;
import com.jeesite.modules.common.vo.MaintenanceInfoVO;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.entity.Maintenance;
import com.jeesite.modules.common.service.MaintenanceService;

/**
 * 车辆维保记录Controller
 *
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Controller
@RequestMapping(value = "${adminPath}/common/maintenance")
public class MaintenanceController extends BaseController {

    @Autowired
    private MaintenanceService maintenanceService;


    @Autowired
    private MaintenanceTotalService maintenanceTotalService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Maintenance get(String id, boolean isNewRecord) {
        return maintenanceService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Maintenance maintenance, Model model) {
        model.addAttribute("maintenance", maintenance);
        return "modules/common/maintenanceList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Maintenance> listData(Maintenance maintenance, HttpServletRequest request, HttpServletResponse response) {
        maintenance.setPage(new Page<>(request, response));
        Page<Maintenance> page = maintenanceService.findPage(maintenance);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(Maintenance maintenance, Model model) {
        model.addAttribute("maintenance", maintenance);
        return "modules/common/maintenanceForm";
    }

    /**
     * 保存车辆维保记录
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated Maintenance maintenance) {
        maintenanceService.save(maintenance);
        return renderResult(Global.TRUE, text("保存车辆维保记录成功！"));
    }

    /**
     * 删除车辆维保记录
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Maintenance maintenance) {
        maintenanceService.delete(maintenance);
        return renderResult(Global.TRUE, text("删除车辆维保记录成功！"));
    }

    /**
     * @description: 加载维保记录
     * @param: [maintenanceTotal]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 17:03
     */
    @RequestMapping(value = "findMaintenance")
    @ResponseBody
    public CommonResult findMaintenance(MaintenanceTotal maintenanceTotal) {
        CommonResult commonResult = new CommonResult();
        if (StringUtils.isBlank(maintenanceTotal.getId())) {
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setMsg("参数为空");
            return commonResult;
        }
        return maintenanceService.findMaintenance(maintenanceTotal);
    }

    /**
     * @description: 加载维保详情
     * @param: [maintenance]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 17:14
     */
    @RequestMapping(value = "findMaintenanceDetail")
    @ResponseBody
    public CommonResult findMaintenanceDetail(Maintenance maintenance) {
        CommonResult commonResult = new CommonResult();
        if (StringUtils.isBlank(maintenance.getId())) {
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setMsg("参数为空");
            return commonResult;
        }
        return maintenanceService.findMaintenanceDetail(maintenance);
    }

    /**
     * @description: 保存维保记录
     * @param: [maintenanceInfoVO]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/6
     * @time: 8:28
     */
    @RequestMapping(value = "saveMaintenance")
    @ResponseBody
    public CommonResult saveMaintenance(@RequestBody(required = true) MaintenanceInfoVO maintenanceInfoVO) {
        if (StringUtils.isBlank(maintenanceInfoVO.getVin()) || StringUtils.isBlank(maintenanceInfoVO.getCarModel())) {
            return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER, "车型参数或VIN码不能为空!");
        }
        if(CollectionUtils.isEmpty(maintenanceInfoVO.getMaintenanceRecords())){
            return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER,"维保记录不能为空!");
        }

        MaintenanceTotal total = new MaintenanceTotal();
        total.setVinCode(maintenanceInfoVO.getVin());
        total = maintenanceTotalService.getByEntity(total);
        if(null!=total){
            if(StringUtils.isBlank(maintenanceInfoVO.getId()) || !maintenanceInfoVO.getId().equals(total.getId())){
                return new CommonResult(CodeConstant.REQUEST_FAILED, "该VIN记录已存在!");
            }
        }


        if (StringUtils.isNotBlank(maintenanceInfoVO.getId())) {
            // 编辑
            MaintenanceTotal maintenanceTotal = new MaintenanceTotal();
            maintenanceTotal.setId(maintenanceInfoVO.getId());
            maintenanceTotal = maintenanceTotalService.get(maintenanceTotal);
            if (null == maintenanceTotal) {
                return new CommonResult(CodeConstant.REQUEST_FAILED, "空对象");
            }
            maintenanceService.saveMaintenance(maintenanceTotal, maintenanceInfoVO);
        } else {
            // 保存
            maintenanceService.saveMaintenance(null, maintenanceInfoVO);
        }
        return new CommonResult();
    }

    /**
     * @description: 加载排放标准
     * @param: []
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/5
     * @time: 19:43
     */
    @RequestMapping("findEnvironmentalStandard")
    @ResponseBody
    public CommonResult findEnvironmentalStandard() {
        CommonResult commonResult = new CommonResult();
        commonResult.setData(DictUtils.getDictList("aa_environmental_standard"));
        return commonResult;
    }

}