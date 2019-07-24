/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.modules.aa.entity.SystemSetting;
import com.jeesite.modules.aa.service.SystemSettingService;

/**
 * 大平台域名设置Controller
 * @author lvchangwei
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/systemSetting")
public class SystemSettingController extends BaseController {

	@Autowired
	private SystemSettingService systemSettingService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public SystemSetting get(String id, boolean isNewRecord) {
		return systemSettingService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequiresPermissions("aa:systemSetting:view")
	@RequestMapping(value = {"list", ""})
	public String list(SystemSetting systemSetting, Model model) {
		model.addAttribute("systemSetting", systemSetting);
		return "modules/aa/systemSettingList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequiresPermissions("aa:systemSetting:view")
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<SystemSetting> listData(SystemSetting systemSetting, HttpServletRequest request, HttpServletResponse response) {
		systemSetting.setPage(new Page<>(request, response));
		Page<SystemSetting> page = systemSettingService.findPage(systemSetting);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequiresPermissions("aa:systemSetting:view")
	@RequestMapping(value = "form")
	public String form(SystemSetting systemSetting, Model model) {
		model.addAttribute("systemSetting", systemSetting);
		return "modules/aa/systemSettingForm";
	}

	/**
	 * 保存大平台域名设置
	 */
	@RequiresPermissions("aa:systemSetting:edit")
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated SystemSetting systemSetting) {
		systemSettingService.save(systemSetting);
		return renderResult(Global.TRUE, text("保存大平台域名设置成功！"));
	}
	
	/**
	 * 删除大平台域名设置
	 */
	@RequiresPermissions("aa:systemSetting:edit")
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(SystemSetting systemSetting) {
		systemSettingService.delete(systemSetting);
		return renderResult(Global.TRUE, text("删除大平台域名设置成功！"));
	}
	
}