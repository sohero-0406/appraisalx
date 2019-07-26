/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

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
import com.jeesite.modules.common.entity.MaintenanceType;
import com.jeesite.modules.common.service.MaintenanceTypeService;

/**
 * common_maintenance_typeController
 * @author liangtao
 * @version 2019-07-26
 */
@Controller
@RequestMapping(value = "${adminPath}/common/maintenanceType")
public class MaintenanceTypeController extends BaseController {

	@Autowired
	private MaintenanceTypeService maintenanceTypeService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public MaintenanceType get(String id, boolean isNewRecord) {
		return maintenanceTypeService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(MaintenanceType maintenanceType, Model model) {
		model.addAttribute("maintenanceType", maintenanceType);
		return "modules/common/maintenanceTypeList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<MaintenanceType> listData(MaintenanceType maintenanceType, HttpServletRequest request, HttpServletResponse response) {
		maintenanceType.setPage(new Page<>(request, response));
		Page<MaintenanceType> page = maintenanceTypeService.findPage(maintenanceType);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(MaintenanceType maintenanceType, Model model) {
		model.addAttribute("maintenanceType", maintenanceType);
		return "modules/common/maintenanceTypeForm";
	}

	/**
	 * 保存common_maintenance_type
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated MaintenanceType maintenanceType) {
		maintenanceTypeService.save(maintenanceType);
		return renderResult(Global.TRUE, text("保存common_maintenance_type成功！"));
	}
	
	/**
	 * 删除common_maintenance_type
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(MaintenanceType maintenanceType) {
		maintenanceTypeService.delete(maintenanceType);
		return renderResult(Global.TRUE, text("删除common_maintenance_type成功！"));
	}
	
}