/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.DelegateUser;
import com.jeesite.modules.aa.service.DelegateUserService;
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
 * 委托方信息Controller
 * @author chenlitao
 * @version 2019-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/delegateUser")
public class DelegateUserController extends BaseController {

	@Autowired
	private DelegateUserService delegateUserService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DelegateUser get(String id, boolean isNewRecord) {
		return delegateUserService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(DelegateUser delegateUser, Model model) {
		model.addAttribute("delegateUser", delegateUser);
		return "modules/aa/delegateUserList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DelegateUser> listData(DelegateUser delegateUser, HttpServletRequest request, HttpServletResponse response) {
		delegateUser.setPage(new Page<>(request, response));
		Page<DelegateUser> page = delegateUserService.findPage(delegateUser);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(DelegateUser delegateUser, Model model) {
		model.addAttribute("delegateUser", delegateUser);
		return "modules/aa/delegateUserForm";
	}

	/**
	 * 保存委托方信息
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DelegateUser delegateUser) {
		delegateUserService.save(delegateUser);
		return renderResult(Global.TRUE, text("保存委托方信息成功！"));
	}
	
	/**
	 * 删除委托方信息
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DelegateUser delegateUser) {
		delegateUserService.delete(delegateUser);
		return renderResult(Global.TRUE, text("删除委托方信息成功！"));
	}

	@RequestMapping(value = "getSession")
	@ResponseBody
	public String getSession(HttpServletRequest request,HttpServletResponse response) {
		ExamUser examUser = UserUtils.getExamUser();
//		examUser.re
		String a = (String)request.getSession().getAttribute("admin");
		System.out.println("得到session内的数据");
		return null;
	}

}