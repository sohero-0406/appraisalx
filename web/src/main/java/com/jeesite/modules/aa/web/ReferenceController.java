/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.common.entity.CommonResult;
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
import com.jeesite.modules.aa.entity.Reference;
import com.jeesite.modules.aa.service.ReferenceService;

import java.util.List;

/**
 * 参照物表Controller
 * @author lvchangwei
 * @version 2019-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/reference")
public class ReferenceController extends BaseController {

	@Autowired
	private ReferenceService referenceService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Reference get(String id, boolean isNewRecord) {
		return referenceService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Reference reference, Model model) {
		model.addAttribute("reference", reference);
		return "modules/aa/referenceList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Reference> listData(Reference reference, HttpServletRequest request, HttpServletResponse response) {
		reference.setPage(new Page<>(request, response));
		Page<Reference> page = referenceService.findPage(reference);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(Reference reference, Model model) {
		model.addAttribute("reference", reference);
		return "modules/aa/referenceForm";
	}

	/**
	 * 保存参照物表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Reference reference) {
		referenceService.save(reference);
		return renderResult(Global.TRUE, text("保存参照物表成功！"));
	}
	
	/**
	 * 删除参照物表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Reference reference) {
		referenceService.delete(reference);
		return renderResult(Global.TRUE, text("删除参照物表成功！"));
	}


	/**
	 * 保存参照物
	 */
	@PostMapping(value = "saveReference")
	@ResponseBody
	public CommonResult saveReference(@Validated Reference reference) {
		referenceService.save(reference);
		return new CommonResult();
	}

	/**
	 * 删除参照物表
	 */
	@RequestMapping(value = "deleteReference")
	@ResponseBody
	public CommonResult deleteReference(@Validated Reference reference) {
		if(reference.getId().isEmpty()){
			CommonResult comRes = new CommonResult();
			comRes.setCode("1010");
			comRes.setMsg("未传入ID");
			return comRes;
		}
		referenceService.delete(reference);
		return new CommonResult();
	}

	/**
	 * 查询参照物列表
	 */
	@RequestMapping(value = "findReference")
	@ResponseBody
	public CommonResult findReference() {
		CommonResult comRes = new CommonResult();
		comRes.setData(referenceService.findList(new Reference()));
		return comRes;
	}
}