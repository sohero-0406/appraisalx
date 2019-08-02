/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
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
	@RequestMapping(value = {"list", ""})
	public String list(SystemSetting systemSetting, Model model) {
		model.addAttribute("systemSetting", systemSetting);
		return "modules/aa/systemSettingList";
	}
	
	/**
	 * 查询列表数据
	 */
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
	@RequestMapping(value = "form")
	public String form(SystemSetting systemSetting, Model model) {
		model.addAttribute("systemSetting", systemSetting);
		return "modules/aa/systemSettingForm";
	}

	/**
	 * 保存大平台域名设置
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated SystemSetting systemSetting) {
		systemSettingService.save(systemSetting);
		return renderResult(Global.TRUE, text("保存大平台域名设置成功！"));
	}
	
	/**
	 * 删除大平台域名设置
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(SystemSetting systemSetting) {
		systemSettingService.delete(systemSetting);
		return renderResult(Global.TRUE, text("删除大平台域名设置成功！"));
	}

	//修改端口号
	@RequestMapping(value = "saveSystemSetting")
	@ResponseBody
	public CommonResult saveSystemSetting(SystemSetting systemSetting) {
		CommonResult comRes = new CommonResult();
		if(null==systemSetting || StringUtils.isBlank(systemSetting.getId())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("参数不能为空");
			return comRes;
		}
		if(StringUtils.isBlank(systemSetting.getIp())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("ip地址不能为空");
			return comRes;
		}
		if(StringUtils.isBlank(systemSetting.getPort())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("端口号不能为空");
			return comRes;
		}
		if(StringUtils.isBlank(systemSetting.getContextPath())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("项目路径不能为空");
			return comRes;
		}
		SystemSetting systemSettingTest = new SystemSetting();
		int len = systemSettingService.findList(systemSettingTest).size();
		if(len>1){
			systemSettingService.phyDeleteByEntity(systemSettingTest);
			if(1==(int)systemSettingService.insertSystemSetting(systemSettingTest)){
				comRes.setMsg("修改成功!");
				return comRes;
			}else{
				comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
				comRes.setMsg("修改失败!");
				return comRes;
			}
		}
		comRes.setMsg("修改成功!");
		systemSettingService.save(systemSetting);
		return comRes;
	}



}