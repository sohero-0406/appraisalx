/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.DelegateLetter;
import com.jeesite.modules.aa.service.DelegateLetterService;
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
 * 委托书信息Controller
 * @author lvchangwei
 * @version 2019-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/delegateLetter")
public class DelegateLetterController extends BaseController {

	@Autowired
	private DelegateLetterService delegateLetterService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DelegateLetter get(String id, boolean isNewRecord) {
		return delegateLetterService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(DelegateLetter delegateLetter, Model model) {
		model.addAttribute("delegateLetter", delegateLetter);
		return "modules/aa/delegateLetterList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DelegateLetter> listData(DelegateLetter delegateLetter, HttpServletRequest request, HttpServletResponse response) {
		delegateLetter.setPage(new Page<>(request, response));
		Page<DelegateLetter> page = delegateLetterService.findPage(delegateLetter);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(DelegateLetter delegateLetter, Model model) {
		model.addAttribute("delegateLetter", delegateLetter);
		return "modules/aa/delegateLetterForm";
	}

	/**
	 * 保存委托书信息
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DelegateLetter delegateLetter) {
		delegateLetterService.save(delegateLetter);
		return renderResult(Global.TRUE, text("保存委托书信息成功！"));
	}
	
	/**
	 * 删除委托书信息
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DelegateLetter delegateLetter) {
		delegateLetterService.delete(delegateLetter);
		return renderResult(Global.TRUE, text("删除委托书信息成功！"));
	}

	/**
	 * 查询单个委托书信息
	 */
	@RequestMapping(value = "findDelegateLetter")
	@ResponseBody
	public CommonResult findDelegateLetter() {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		comRes.setData(delegateLetterService.findDelegateLetter(examUser));
		return comRes;
	}

	/**
	 * 保存委托书信息
	 */
	@RequestMapping(value = "saveDelegateLetter")
	@ResponseBody
	public CommonResult saveDelegateLetter(DelegateLetter delegateLetter) {
		ExamUser examUser = UserUtils.getExamUser();
		delegateLetter.setPaperId(examUser.getPaperId());
		delegateLetterService.save(delegateLetter);
		return new CommonResult();
	}


	/**
	 * 查询鉴定报告
	 */
	@RequestMapping(value = "findAppraisalReport")
	@ResponseBody
	public CommonResult findAppraisalReport() {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		comRes.setData(delegateLetterService.findAppraisalReport(examUser));
		return comRes;
	}





	@RequestMapping(value = "test")
	@ResponseBody
	public CommonResult createAppraisalReportNum() {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		comRes.setData(delegateLetterService.appraisalReportInfo(examUser));
		return comRes;
	}

	@RequestMapping(value = "test1")
	@ResponseBody
	public CommonResult createAppraisalReportNum1() {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		delegateLetterService.generateLetter(examUser);
		return comRes;
	}




}