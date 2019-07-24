/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.aa.entity.CalculateReplaceCost;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.jeesite.modules.aa.entity.CalculateCurrent;
import com.jeesite.modules.aa.service.CalculateCurrentService;

/**
 * 现行市价法Controller
 * @author lvchangwei
 * @version 2019-07-22
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/calculateCurrent")
public class CalculateCurrentController extends BaseController {

	@Autowired
	private CalculateCurrentService calculateCurrentService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CalculateCurrent get(String id, boolean isNewRecord) {
		return calculateCurrentService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CalculateCurrent calculateCurrent, Model model) {
		model.addAttribute("calculateCurrent", calculateCurrent);
		return "modules/aa/calculateCurrentList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CalculateCurrent> listData(CalculateCurrent calculateCurrent, HttpServletRequest request, HttpServletResponse response) {
		calculateCurrent.setPage(new Page<>(request, response));
		Page<CalculateCurrent> page = calculateCurrentService.findPage(calculateCurrent);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(CalculateCurrent calculateCurrent, Model model) {
		model.addAttribute("calculateCurrent", calculateCurrent);
		return "modules/aa/calculateCurrentForm";
	}

	/**
	 * 保存现行市价法
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CalculateCurrent calculateCurrent) {
		calculateCurrentService.save(calculateCurrent);
		return renderResult(Global.TRUE, text("保存现行市价法成功！"));
	}
	
	/**
	 * 删除现行市价法
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CalculateCurrent calculateCurrent) {
		calculateCurrentService.delete(calculateCurrent);
		return renderResult(Global.TRUE, text("删除现行市价法成功！"));
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "calculate")
	@ResponseBody
	public CommonResult calculate(CalculateCurrent calculateCurrent) {
		ExamUser examUser = UserUtils.getExamUser();
		calculateCurrent = calculateCurrentService.calculate(calculateCurrent, examUser);
		CommonResult result = new CommonResult();
		result.setData(calculateCurrent);
		return result;
	}
}