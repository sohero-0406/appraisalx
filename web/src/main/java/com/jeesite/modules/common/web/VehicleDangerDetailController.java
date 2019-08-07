/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.VehicleDanger;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
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
import com.jeesite.modules.common.entity.VehicleDangerDetail;
import com.jeesite.modules.common.service.VehicleDangerDetailService;

import java.util.ArrayList;
import java.util.List;

/**
 * 出险记录详情表Controller
 *
 * @author liangtao
 * @version 2019-07-12
 */
@Controller
@RequestMapping(value = "${adminPath}/common/vehicleDangerDetail")
public class VehicleDangerDetailController extends BaseController {

    @Autowired
    private VehicleDangerDetailService vehicleDangerDetailService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public VehicleDangerDetail get(String id, boolean isNewRecord) {
        return vehicleDangerDetailService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(VehicleDangerDetail vehicleDangerDetail, Model model) {
        model.addAttribute("vehicleDangerDetail", vehicleDangerDetail);
        return "modules/common/vehicleDangerDetailList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<VehicleDangerDetail> listData(VehicleDangerDetail vehicleDangerDetail, HttpServletRequest request, HttpServletResponse response) {
        vehicleDangerDetail.setPage(new Page<>(request, response));
        Page<VehicleDangerDetail> page = vehicleDangerDetailService.findPage(vehicleDangerDetail);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(VehicleDangerDetail vehicleDangerDetail, Model model) {
        model.addAttribute("vehicleDangerDetail", vehicleDangerDetail);
        return "modules/common/vehicleDangerDetailForm";
    }

    /**
     * 保存出险记录详情表
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated VehicleDangerDetail vehicleDangerDetail) {
        vehicleDangerDetailService.save(vehicleDangerDetail);
        return renderResult(Global.TRUE, text("保存出险记录详情表成功！"));
    }

    /**
     * 删除出险记录详情表
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(VehicleDangerDetail vehicleDangerDetail) {
        vehicleDangerDetailService.delete(vehicleDangerDetail);
        return renderResult(Global.TRUE, text("删除出险记录详情表成功！"));
    }

    /**
     * @description: 加载理赔记录
     * @param: []
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/4
     * @time: 15:04
     */
    @RequestMapping(value = "findVehicleDangerDetail")
    @ResponseBody
    public CommonResult findVehicleDangerDetail(VehicleDanger vehicleDanger) {
        CommonResult commonResult = new CommonResult();
        if (null == vehicleDanger.getId()) {
            commonResult.setCode(CodeConstant.REQUEST_FAILED);
            commonResult.setMsg("参数为空");
            return commonResult;
        }
        commonResult.setData(vehicleDangerDetailService.findVehicleDangerDetail(vehicleDanger));
        return commonResult;
    }

    /** 
    * @description: 加载理赔类型
    * @param: []
    * @return: com.jeesite.modules.common.entity.CommonResult
    * @author: Jiangyf
    * @date: 2019/8/5 
    * @time: 14:55
    */ 
    @RequestMapping(value = "findDangerSingleType")
    @ResponseBody
    public CommonResult findDangerSingleType() {
        CommonResult commonResult = new CommonResult();
        commonResult.setData(DictUtils.getDictList("aa_claim_type"));
        return commonResult;
    }
}