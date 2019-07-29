/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.common.entity.CommonResult;
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
import com.jeesite.modules.aa.entity.ExamDetail;
import com.jeesite.modules.aa.service.ExamDetailService;

/**
 * 考试详情表Controller
 * @author lvchangwei
 * @version 2019-07-09
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/examDetail")
public class ExamDetailController extends BaseController {

	@Autowired
	private ExamDetailService examDetailService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ExamDetail get(String id, boolean isNewRecord) {
		return examDetailService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ExamDetail examDetail, Model model) {
		model.addAttribute("examDetail", examDetail);
		return "modules/aa/examDetailList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ExamDetail> listData(ExamDetail examDetail, HttpServletRequest request, HttpServletResponse response) {
		examDetail.setPage(new Page<>(request, response));
		Page<ExamDetail> page = examDetailService.findPage(examDetail);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(ExamDetail examDetail, Model model) {
		model.addAttribute("examDetail", examDetail);
		return "modules/aa/examDetailForm";
	}

	/**
	 * 保存考试详情表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ExamDetail examDetail) {
		examDetailService.save(examDetail);
		return renderResult(Global.TRUE, text("保存考试详情表成功！"));
	}
	
	/**
	 * 删除考试详情表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ExamDetail examDetail) {
		examDetailService.delete(examDetail);
		return renderResult(Global.TRUE, text("删除考试详情表成功！"));
	}


}