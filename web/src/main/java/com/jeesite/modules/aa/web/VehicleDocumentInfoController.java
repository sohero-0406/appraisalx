/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.web.http.HttpClientUtils;
import com.jeesite.modules.aa.vo.VehicleDocumentInfoVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.VehicleDocumentInfo;
import com.jeesite.modules.aa.service.VehicleDocumentInfoService;

import java.util.List;

/**
 * 车辆单证信息Controller
 *
 * @author lvchangwei
 * @version 2019-07-01
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/vehicleDocumentInfo")
public class VehicleDocumentInfoController extends BaseController {

    @Autowired
    private VehicleDocumentInfoService vehicleDocumentInfoService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public VehicleDocumentInfo get(String id, boolean isNewRecord) {
        return vehicleDocumentInfoService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(VehicleDocumentInfo vehicleDocumentInfo, Model model) {
        model.addAttribute("vehicleDocumentInfo", vehicleDocumentInfo);
        return "modules/aa/vehicleDocumentInfoList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<VehicleDocumentInfo> listData(VehicleDocumentInfo vehicleDocumentInfo, HttpServletRequest request, HttpServletResponse response) {
        vehicleDocumentInfo.setPage(new Page<>(request, response));
        Page<VehicleDocumentInfo> page = vehicleDocumentInfoService.findPage(vehicleDocumentInfo);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(VehicleDocumentInfo vehicleDocumentInfo, Model model) {
        model.addAttribute("vehicleDocumentInfo", vehicleDocumentInfo);
        return "modules/aa/vehicleDocumentInfoForm";
    }

    /**
     * 录入车辆单证信息
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public CommonResult save(@Validated String velicleDocumentJson) {
        ExamUser examUser = UserUtils.getExamUser();
        vehicleDocumentInfoService.saveBatch(examUser, velicleDocumentJson);
        return new CommonResult();
    }

    /**
     * 删除车辆单证信息
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(VehicleDocumentInfo vehicleDocumentInfo) {
        vehicleDocumentInfoService.delete(vehicleDocumentInfo);
        return renderResult(Global.TRUE, text("删除车辆单证信息成功！"));
    }

    /**
     * 车辆单证信息加载
     */
    @RequestMapping(value = "findList")
    @ResponseBody
    public CommonResult findList() {
        ExamUser examUser = UserUtils.getExamUser();
        VehicleDocumentInfoVO vo = vehicleDocumentInfoService.findDocument(examUser);
        CommonResult result = new CommonResult();
        result.setData(vo);
        return result;
    }
}