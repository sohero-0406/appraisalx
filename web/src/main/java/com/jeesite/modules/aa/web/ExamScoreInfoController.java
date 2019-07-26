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
import com.jeesite.modules.aa.entity.ExamScoreInfo;
import com.jeesite.modules.aa.service.ExamScoreInfoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 考试评分点Controller
 * @author liangtao
 * @version 2019-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/examScoreInfo")
public class ExamScoreInfoController extends BaseController {

	@Autowired
	private ExamScoreInfoService examScoreInfoService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ExamScoreInfo get(String id, boolean isNewRecord) {
		return examScoreInfoService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ExamScoreInfo examScoreInfo, Model model) {
		model.addAttribute("examScoreInfo", examScoreInfo);
		return "modules/aa/examScoreInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ExamScoreInfo> listData(ExamScoreInfo examScoreInfo, HttpServletRequest request, HttpServletResponse response) {
		examScoreInfo.setPage(new Page<>(request, response));
		Page<ExamScoreInfo> page = examScoreInfoService.findPage(examScoreInfo);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(ExamScoreInfo examScoreInfo, Model model) {
		model.addAttribute("examScoreInfo", examScoreInfo);
		return "modules/aa/examScoreInfoForm";
	}

	/**
	 * 保存考试评分点
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ExamScoreInfo examScoreInfo) {
		examScoreInfoService.save(examScoreInfo);
		return renderResult(Global.TRUE, text("保存考试评分点成功！"));
	}
	
	/**
	 * 删除考试评分点
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ExamScoreInfo examScoreInfo) {
		examScoreInfoService.delete(examScoreInfo);
		return renderResult(Global.TRUE, text("删除考试评分点成功！"));
	}

	/**
	 * 获取分值项默认值
	 */
	@RequestMapping(value = "getExamScoreInfo")
	@ResponseBody
	public CommonResult getExamScormeInfo() {
		CommonResult comRes = new CommonResult();
		List<ExamScoreInfo> examScoreInfo = examScoreInfoService.getExamScoreInfo();
		comRes.setData(examScoreInfo);
		return comRes;
	}



}


