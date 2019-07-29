/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.modules.aa.entity.ExamScoreClassify;
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
import com.jeesite.modules.aa.entity.ExamScoreDetail;
import com.jeesite.modules.aa.service.ExamScoreDetailService;

import java.util.List;

/**
 * 考试评分详情表Controller
 * @author liangtao
 * @version 2019-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/examScoreDetail")
public class ExamScoreDetailController extends BaseController {

	@Autowired
	private ExamScoreDetailService examScoreDetailService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ExamScoreDetail get(String id, boolean isNewRecord) {
		return examScoreDetailService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ExamScoreDetail examScoreDetail, Model model) {
		model.addAttribute("examScoreDetail", examScoreDetail);
		return "modules/aa/examScoreDetailList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ExamScoreDetail> listData(ExamScoreDetail examScoreDetail, HttpServletRequest request, HttpServletResponse response) {
		examScoreDetail.setPage(new Page<>(request, response));
		Page<ExamScoreDetail> page = examScoreDetailService.findPage(examScoreDetail);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(ExamScoreDetail examScoreDetail, Model model) {
		model.addAttribute("examScoreDetail", examScoreDetail);
		return "modules/aa/examScoreDetailForm";
	}

	/**
	 * 保存考试评分详情表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ExamScoreDetail examScoreDetail) {
		examScoreDetailService.save(examScoreDetail);
		return renderResult(Global.TRUE, text("保存考试评分详情表成功！"));
	}
	
	/**
	 * 删除考试评分详情表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ExamScoreDetail examScoreDetail) {
		examScoreDetailService.delete(examScoreDetail);
		return renderResult(Global.TRUE, text("删除考试评分详情表成功！"));
	}

	/**
	 * 获取评分项分数
	 */
	@RequestMapping(value = "findData")
	@ResponseBody
	public CommonResult findData(String examId) {
		CommonResult comRes = new CommonResult();
		List<ExamScoreClassify> ExamScoreClassify =  examScoreDetailService.findData(examId);
		comRes.setData(ExamScoreClassify);
		return comRes;
	}

	/**
	 * 新建评分项分值
	 */
	@RequestMapping(value = "saveExamScoreInfo")
	@ResponseBody
	public CommonResult saveExamScoreInfo(String examScoreJson,String examId) {
		CommonResult comRes = new CommonResult();
		examScoreDetailService.saveExamScoreInfo(examScoreJson,examId);
		return comRes;
	}



}