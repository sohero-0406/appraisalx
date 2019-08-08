/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.aa.vo.VehicleGradeAssessVO;
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
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.aa.service.VehicleGradeAssessService;

/**
 * 车辆等级评定Controller
 * @author lvchangwei
 * @version 2019-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/vehicleGradeAssess")
public class VehicleGradeAssessController extends BaseController {

	@Autowired
	private VehicleGradeAssessService vehicleGradeAssessService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public VehicleGradeAssess get(String id, boolean isNewRecord) {
		return vehicleGradeAssessService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(VehicleGradeAssess vehicleGradeAssess, Model model) {
		model.addAttribute("vehicleGradeAssess", vehicleGradeAssess);
		return "modules/aa/vehicleGradeAssessList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<VehicleGradeAssess> listData(VehicleGradeAssess vehicleGradeAssess, HttpServletRequest request, HttpServletResponse response) {
		vehicleGradeAssess.setPage(new Page<>(request, response));
		Page<VehicleGradeAssess> page = vehicleGradeAssessService.findPage(vehicleGradeAssess);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(VehicleGradeAssess vehicleGradeAssess, Model model) {
		model.addAttribute("vehicleGradeAssess", vehicleGradeAssess);
		return "modules/aa/vehicleGradeAssessForm";
	}

	/**
	 * 保存车辆等级评定
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public CommonResult save(@Validated VehicleGradeAssess vehicleGradeAssess) {
		ExamUser examUser = UserUtils.getExamUser();
		vehicleGradeAssessService.save(examUser,vehicleGradeAssess);
		return new CommonResult();
	}
	
	/**
	 * 删除车辆等级评定
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(VehicleGradeAssess vehicleGradeAssess) {
		vehicleGradeAssessService.delete(vehicleGradeAssess);
		return renderResult(Global.TRUE, text("删除车辆等级评定成功！"));
	}

	/**
	 * 查询车辆等级评定
	 */
	@RequestMapping(value = "getDetail")
	@ResponseBody
	public CommonResult getDetail() {
		ExamUser examUser = UserUtils.getExamUser();
		VehicleGradeAssessVO vo = vehicleGradeAssessService.getDetail(examUser);
		CommonResult result = new CommonResult();
		result.setData(vo);
		return result;
	}
}