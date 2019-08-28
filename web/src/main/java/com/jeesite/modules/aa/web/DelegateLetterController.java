/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.DelegateLetter;
import com.jeesite.modules.aa.service.DelegateLetterService;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.text.ParseException;

/**
 * 委托书信息Controller
 * @author lvchangwei
 * @version 2019-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/delegateLetter")
public class DelegateLetterController extends BaseController {

	@Autowired
	private DelegateLetterService delegateLetterService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public DelegateLetter get(String id, boolean isNewRecord) {
		return delegateLetterService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(DelegateLetter delegateLetter, Model model) {
		model.addAttribute("delegateLetter", delegateLetter);
		return "modules/aa/delegateLetterList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<DelegateLetter> listData(DelegateLetter delegateLetter, HttpServletRequest request, HttpServletResponse response) {
		delegateLetter.setPage(new Page<>(request, response));
		Page<DelegateLetter> page = delegateLetterService.findPage(delegateLetter);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(DelegateLetter delegateLetter, Model model) {
		model.addAttribute("delegateLetter", delegateLetter);
		return "modules/aa/delegateLetterForm";
	}

	/**
	 * 保存委托书信息
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated DelegateLetter delegateLetter) {
		delegateLetterService.save(delegateLetter);
		return renderResult(Global.TRUE, text("保存委托书信息成功！"));
	}
	
	/**
	 * 删除委托书信息
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(DelegateLetter delegateLetter) {
		delegateLetterService.delete(delegateLetter);
		return renderResult(Global.TRUE, text("删除委托书信息成功！"));
	}

	/**
	 * 查询单个委托书信息
	 */
	@RequestMapping(value = "findDelegateLetter")
	@ResponseBody
	public CommonResult findDelegateLetter() {
		ExamUser examUser = UserUtils.getExamUser();
		return new CommonResult(delegateLetterService.findDelegateLetter(examUser));
	}

	/**
	 * 保存委托书信息
	 */
	@RequestMapping(value = "saveDelegateLetter")
	@ResponseBody
	public CommonResult saveDelegateLetter(DelegateLetter delegateLetter) {
		ExamUser examUser = UserUtils.getExamUser();
		delegateLetter.setPaperId(examUser.getPaperId());
		delegateLetterService.save(delegateLetter);
		delegateLetterService.savePivture(examUser);
		return new CommonResult();
	}


	/**
	 * 保存一份鉴定评估报告
	 */
	@RequestMapping(value = "saveAppraisalReport")
	@ResponseBody
	public CommonResult saveAppraisalReport(DelegateLetter delegateLetter) {
		CommonResult comRes = new CommonResult();
		ExamUser examUser = UserUtils.getExamUser();
		//如果paperId 为空，则参数出现异常
		if(StringUtils.isBlank(examUser.getPaperId())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("请求参数存在异常!");
			return comRes;
		}
		comRes = delegateLetterService.saveAppraisalReport(delegateLetter,examUser);
		return comRes;
	}



	/**
	 * 鉴定评估报告预览
	 */
	@RequestMapping(value = "findAppraisalReport")
	@ResponseBody
	public CommonResult findAppraisalReport() throws ParseException {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		comRes.setData(delegateLetterService.appraisalReportInfo(examUser));
		return comRes;
	}


	/**
	 *  生成鉴定评估报告
	 * @return
	 */
	@RequestMapping(value = "generateAppraisalReport")
	@ResponseBody
	public CommonResult generateAppraisalReport() throws ParseException {
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		delegateLetterService.generateLetter(examUser);
		return comRes;
	}


	/**
	 *  下载鉴定报告
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "downloadAppraisalReport")
	@ResponseBody
	public void getWord(HttpServletRequest request, HttpServletResponse response) {
		ExamUser examUser = UserUtils.getExamUser();
		delegateLetterService.getWord(request,response,examUser);
	}
}