/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.modules.aa.entity.IdentifyTec;
import com.jeesite.modules.aa.service.IdentifyTecService;

/**
 * 鉴定技术状况Controller
 * @author lvchangwei
 * @version 2019-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/identifyTec")
public class IdentifyTecController extends BaseController {

	@Autowired
	private IdentifyTecService identifyTecService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public IdentifyTec get(String id, boolean isNewRecord) {
		return identifyTecService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(IdentifyTec identifyTec, Model model) {
		model.addAttribute("identifyTec", identifyTec);
		return "modules/aa/identifyTecList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<IdentifyTec> listData(IdentifyTec identifyTec, HttpServletRequest request, HttpServletResponse response) {
		identifyTec.setPage(new Page<>(request, response));
		Page<IdentifyTec> page = identifyTecService.findPage(identifyTec);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(IdentifyTec identifyTec, Model model) {
		model.addAttribute("identifyTec", identifyTec);
		return "modules/aa/identifyTecForm";
	}

	/**
	 * 保存鉴定技术状况
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated IdentifyTec identifyTec) {
		identifyTecService.save(identifyTec);
		return renderResult(Global.TRUE, text("保存鉴定技术状况成功！"));
	}
	
	/**
	 * 删除鉴定技术状况
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(IdentifyTec identifyTec) {
		identifyTecService.delete(identifyTec);
		return renderResult(Global.TRUE, text("删除鉴定技术状况成功！"));
	}

	/**
	 * 保存鉴定技术状况数据
	 */
	@RequestMapping(value = "saveData")
	@ResponseBody
	public CommonResult saveData(@Validated String itemJson) {
		ExamUser examUser = UserUtils.getExamUser();
		identifyTecService.saveData(examUser, itemJson);
		return new CommonResult();
	}

	/** 
	* @description: 加载二手车技术状况表
	* @param: []
	* @return: com.jeesite.modules.common.entity.CommonResult
	* @author: Jiangyf
	* @date: 2019/8/8 
	* @time: 18:27
	*/ 
	@RequestMapping(value = "findTechnicalStatusTable")
	@ResponseBody
	public CommonResult findTechnicalStatusTable() {
		ExamUser examUser = UserUtils.getExamUser();
		return identifyTecService.findTechnicalStatusTable(examUser);
	}

}