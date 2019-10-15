/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.aa.entity.CalculateKm;
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
import com.jeesite.modules.aa.entity.CalculateReplaceCost;
import com.jeesite.modules.aa.service.CalculateReplaceCostService;

/**
 * 重置成本法Controller
 * @author chenlitao
 * @version 2019-07-05
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/calculateReplaceCost")
public class CalculateReplaceCostController extends BaseController {

	@Autowired
	private CalculateReplaceCostService calculateReplaceCostService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CalculateReplaceCost get(String id, boolean isNewRecord) {
		return calculateReplaceCostService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CalculateReplaceCost calculateReplaceCost, Model model) {
		model.addAttribute("calculateReplaceCost", calculateReplaceCost);
		return "modules/aa/calculateReplaceCostList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CalculateReplaceCost> listData(CalculateReplaceCost calculateReplaceCost, HttpServletRequest request, HttpServletResponse response) {
		calculateReplaceCost.setPage(new Page<>(request, response));
		Page<CalculateReplaceCost> page = calculateReplaceCostService.findPage(calculateReplaceCost);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(CalculateReplaceCost calculateReplaceCost, Model model) {
		model.addAttribute("calculateReplaceCost", calculateReplaceCost);
		return "modules/aa/calculateReplaceCostForm";
	}

	/**
	 * 保存重置成本法
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CalculateReplaceCost calculateReplaceCost) {
		calculateReplaceCostService.save(calculateReplaceCost);
		return renderResult(Global.TRUE, text("保存重置成本法成功！"));
	}
	
	/**
	 * 删除重置成本法
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CalculateReplaceCost calculateReplaceCost) {
		calculateReplaceCostService.delete(calculateReplaceCost);
		return renderResult(Global.TRUE, text("删除重置成本法成功！"));
	}

	/**
	 * 计算
	 */
	@RequestMapping(value = "calculate")
	@ResponseBody
	public CommonResult calculate(CalculateReplaceCost cost) {
		ExamUser examUser = UserUtils.getExamUser();
        CommonResult result = calculateReplaceCostService.calculate(cost, examUser);
		return result;
	}
}