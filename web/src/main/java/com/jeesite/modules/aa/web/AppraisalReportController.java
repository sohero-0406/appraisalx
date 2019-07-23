/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.AppraisalReport;
import com.jeesite.modules.aa.service.AppraisalReportService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 二手车鉴定评估报告Controller
 * @author lvchangwei
 * @version 2019-07-19
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/appraisalReport")
public class AppraisalReportController extends BaseController {

	@Autowired
	private AppraisalReportService appraisalReportService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public AppraisalReport get(String id, boolean isNewRecord) {
		return appraisalReportService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(AppraisalReport appraisalReport, Model model) {
		model.addAttribute("appraisalReport", appraisalReport);
		return "modules/aa/appraisalReportList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<AppraisalReport> listData(AppraisalReport appraisalReport, HttpServletRequest request, HttpServletResponse response) {
		appraisalReport.setPage(new Page<>(request, response));
		Page<AppraisalReport> page = appraisalReportService.findPage(appraisalReport);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(AppraisalReport appraisalReport, Model model) {
		model.addAttribute("appraisalReport", appraisalReport);
		return "modules/aa/appraisalReportForm";
	}

	/**
	 * 保存二手车鉴定评估报告
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated AppraisalReport appraisalReport) {
		appraisalReportService.save(appraisalReport);
		return renderResult(Global.TRUE, text("保存二手车鉴定评估报告成功！"));
	}
	
	/**
	 * 删除二手车鉴定评估报告
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(AppraisalReport appraisalReport) {
		appraisalReportService.delete(appraisalReport);
		return renderResult(Global.TRUE, text("删除二手车鉴定评估报告成功！"));
	}

	/**
	 * 查询一份鉴定评估报告
	 */
	@RequestMapping(value = "findAppraisalReport")
	@ResponseBody
	public CommonResult findAppraisalReport() {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		comRes.setData(appraisalReportService.findAppraisalReport(examUser));
		return comRes;
	}

	/**
	 * 保存一份鉴定评估报告
	 */
	@RequestMapping(value = "saveAppraisalReport")
	@ResponseBody
	public CommonResult saveAppraisalReport(AppraisalReport appraisalReport) {
		appraisalReportService.save(appraisalReport);
		return new CommonResult();
	}

	/**
	 * 生成鉴定评估报告编号
	 */
	@RequestMapping(value = "createAppraisalReportNum")
	@ResponseBody
	public CommonResult createAppraisalReportNum() {
		appraisalReportService.createAppraisalReportNum();
		return new CommonResult();
	}
	
}