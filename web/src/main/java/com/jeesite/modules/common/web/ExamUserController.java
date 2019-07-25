/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamUserService;
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
 * common_exam_userController
 * @author lvchangwei
 * @version 2019-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/common/examUser")
public class ExamUserController extends BaseController {

	@Autowired
	private ExamUserService examUserService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ExamUser get(String id, boolean isNewRecord) {
		return examUserService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ExamUser examUser, Model model) {
		model.addAttribute("examUser", examUser);
		return "modules/common/examUserList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ExamUser> listData(ExamUser examUser, HttpServletRequest request, HttpServletResponse response) {
		examUser.setPage(new Page<>(request, response));
		Page<ExamUser> page = examUserService.findPage(examUser);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(ExamUser examUser, Model model) {
		model.addAttribute("examUser", examUser);
		return "modules/common/examUserForm";
	}

	/**
	 * 保存common_exam_user
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ExamUser examUser) {
		examUserService.save(examUser);
		return renderResult(Global.TRUE, text("保存common_exam_user成功！"));
	}

	/**
	 * 删除common_exam_user
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ExamUser examUser) {
		examUserService.delete(examUser);
		return renderResult(Global.TRUE, text("删除common_exam_user成功！"));
	}


	/**
	 *  判考试成绩
	 */
	@RequestMapping(value = "gradePapers")
	@ResponseBody
	public CommonResult gradePapers(Exam exam) {
		CommonResult comRes = new CommonResult();
		comRes.setData(examUserService.gradePapers(exam));
		return comRes;
	}

	/**
	 * 考试计时
	 */
	@RequestMapping(value = "examTiming")
	@ResponseBody
	public CommonResult examTiming() {
		ExamUser examUser = UserUtils.getExamUser();
		return examUserService.examTiming(examUser);
	}

}












