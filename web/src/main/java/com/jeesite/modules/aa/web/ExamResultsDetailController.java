/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.common.entity.ExamUser;
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
import com.jeesite.modules.aa.entity.ExamResultsDetail;
import com.jeesite.modules.aa.service.ExamResultsDetailService;

/**
 * 学生成绩详情表Controller
 * @author liangtao
 * @version 2019-07-22
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/examResultsDetail")
public class ExamResultsDetailController extends BaseController {

	@Autowired
	private ExamResultsDetailService examResultsDetailService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ExamResultsDetail get(String id, boolean isNewRecord) {
		return examResultsDetailService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ExamResultsDetail examResultsDetail, Model model) {
		model.addAttribute("examResultsDetail", examResultsDetail);
		return "modules/aa/examResultsDetailList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ExamResultsDetail> listData(ExamResultsDetail examResultsDetail, HttpServletRequest request, HttpServletResponse response) {
		examResultsDetail.setPage(new Page<>(request, response));
		Page<ExamResultsDetail> page = examResultsDetailService.findPage(examResultsDetail);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(ExamResultsDetail examResultsDetail, Model model) {
		model.addAttribute("examResultsDetail", examResultsDetail);
		return "modules/aa/examResultsDetailForm";
	}

	/**
	 * 保存学生成绩详情表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ExamResultsDetail examResultsDetail) {
		examResultsDetailService.save(examResultsDetail);
		return renderResult(Global.TRUE, text("保存学生成绩详情表成功！"));
	}
	
	/**
	 * 删除学生成绩详情表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ExamResultsDetail examResultsDetail) {
		examResultsDetailService.delete(examResultsDetail);
		return renderResult(Global.TRUE, text("删除学生成绩详情表成功！"));
	}

	@RequestMapping(value = "saveExamResultsDetail")
	@ResponseBody
	public String saveExamResultsDetail(ExamUser examUser) {
		examResultsDetailService.saveExamResults(examUser);
		return null;
	}

	
}