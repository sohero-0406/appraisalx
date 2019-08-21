/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.excel.ExcelExport;
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
import com.jeesite.modules.common.entity.OperationLog;
import com.jeesite.modules.common.service.OperationLogService;

import java.io.File;
import java.util.List;

/**
 * 操作日志Controller
 * @author liangtao
 * @version 2019-07-29
 */
@Controller
@RequestMapping(value = "${adminPath}/common/operationLog")
public class OperationLogController extends BaseController {

	@Autowired
	private OperationLogService operationLogService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public OperationLog get(String id, boolean isNewRecord) {
		return operationLogService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(OperationLog operationLog, Model model) {
		model.addAttribute("operationLog", operationLog);
		return "modules/common/operationLogList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<OperationLog> listData(OperationLog operationLog, HttpServletRequest request, HttpServletResponse response) {
		operationLog.setPage(new Page<>(request, response));
		Page<OperationLog> page = operationLogService.findPage(operationLog);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(OperationLog operationLog, Model model) {
		model.addAttribute("operationLog", operationLog);
		return "modules/common/operationLogForm";
	}

	/**
	 * 保存操作日志
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated OperationLog operationLog) {
		operationLogService.save(operationLog);
		return renderResult(Global.TRUE, text("保存操作日志成功！"));
	}
	
	/**
	 * 删除操作日志
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(OperationLog operationLog) {
		operationLogService.delete(operationLog);
		return renderResult(Global.TRUE, text("删除操作日志成功！"));
	}

	/**
	 * 日志搜索查询  (导出)
	 */
	@RequestMapping(value = "getOperationLog")
	@ResponseBody
	public CommonResult getOperationLog(String keyword) {
		CommonResult comRes = new CommonResult();
		//判断数据是否为空
		if(StringUtils.isNotBlank(keyword)){
			keyword = keyword.replace(".","-");
		}
		comRes.setData(operationLogService.getOperationLog(keyword,null));
		return comRes;
	}

	/**
	 * 日志搜索查询导出
	 */
	@RequestMapping(value = "exportOperationLog")
	@ResponseBody
	public void exportOperationLog(String keyword,String operationLogIds,HttpServletResponse response) {
		CommonResult comRes = new CommonResult();
		if(StringUtils.isBlank(operationLogIds)){
			comRes.setMsg("请先在左侧选择需要导出的日志!");
			return;
		}
		String[] idList = operationLogIds.split(",");
		//判断数据是否为空
		if(StringUtils.isNotBlank(keyword)){
			keyword = keyword.replace(".","-");
		}
		List<OperationLog> list = operationLogService.getOperationLog(keyword,idList);
		String fileName = "操作日志" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
		try(ExcelExport ee = new ExcelExport("操作日志", OperationLog.class)) {
			ee.setDataList(list).write(response, fileName);
		}catch (Exception e){
			logger.warn("教师端日志导出异常!");
		}
	}




}