/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.jeesite.modules.aa.vo.VehicleInstallVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
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
import com.jeesite.modules.aa.entity.VehicleInstallInfo;
import com.jeesite.modules.aa.service.VehicleInstallInfoService;

import java.util.List;

/**
 * 车辆加装信息Controller
 * @author chenlitao
 * @version 2019-07-03
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/vehicleInstallInfo")
public class VehicleInstallInfoController extends BaseController {
	@Autowired
	private VehicleInstallInfoService vehicleInstallInfoService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public VehicleInstallInfo get(String id, boolean isNewRecord) {
		return vehicleInstallInfoService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(VehicleInstallInfo vehicleInstallInfo, Model model) {
		model.addAttribute("vehicleInstallInfo", vehicleInstallInfo);
		return "modules/aa/vehicleInstallInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<VehicleInstallInfo> listData(VehicleInstallInfo vehicleInstallInfo, HttpServletRequest request, HttpServletResponse response) {
		vehicleInstallInfo.setPage(new Page<>(request, response));
		Page<VehicleInstallInfo> page = vehicleInstallInfoService.findPage(vehicleInstallInfo);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(VehicleInstallInfo vehicleInstallInfo, Model model) {
		model.addAttribute("vehicleInstallInfo", vehicleInstallInfo);
		return "modules/aa/vehicleInstallInfoForm";
	}

	/**
	 * 保存车辆加装信息
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public CommonResult save(String itemJson) {
		ExamUser examUser = UserUtils.getExamUser();
		List<VehicleInstallVO> vehicleInstallVOList = null;
		if(itemJson != null && itemJson.trim().length() > 0){
			vehicleInstallVOList = JSONArray.parseArray(itemJson, VehicleInstallVO.class);
		}
		return vehicleInstallInfoService.saveAndDelete(examUser, vehicleInstallVOList);
	}

	/**
	 * 加载车辆加装信息，包含已选中的和未选中的
	 * @return
	 */
	@PostMapping(value="findList")
	@ResponseBody
	public CommonResult findList(){
		CommonResult commonResult = new CommonResult();
		ExamUser examUser = UserUtils.getExamUser();
		List<VehicleInstallVO> vehicleInstallInfoList = vehicleInstallInfoService.findList(examUser);
		commonResult.setData(vehicleInstallInfoList);
		return commonResult;
	}

	/**
	 * 删除车辆加装信息
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(VehicleInstallInfo vehicleInstallInfo) {
		vehicleInstallInfoService.delete(vehicleInstallInfo);
		return renderResult(Global.TRUE, text("删除车辆加装信息成功！"));
	}
	
}