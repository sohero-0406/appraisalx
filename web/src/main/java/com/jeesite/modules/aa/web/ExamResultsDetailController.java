/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
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
import com.jeesite.modules.aa.entity.ExamResultsDetail;
import com.jeesite.modules.aa.service.ExamResultsDetailService;

/**
 * 学生成绩详情表Controller
 * @author liangtao
 * @version 2019-07-22
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/examResultsDetail")
public class ExamResultsDetailController extends BaseController {

	@Autowired
	private ExamResultsDetailService examResultsDetailService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public ExamResultsDetail get(String id, boolean isNewRecord) {
		return examResultsDetailService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(ExamResultsDetail examResultsDetail, Model model) {
		model.addAttribute("examResultsDetail", examResultsDetail);
		return "modules/aa/examResultsDetailList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<ExamResultsDetail> listData(ExamResultsDetail examResultsDetail, HttpServletRequest request, HttpServletResponse response) {
		examResultsDetail.setPage(new Page<>(request, response));
		Page<ExamResultsDetail> page = examResultsDetailService.findPage(examResultsDetail);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(ExamResultsDetail examResultsDetail, Model model) {
		model.addAttribute("examResultsDetail", examResultsDetail);
		return "modules/aa/examResultsDetailForm";
	}

	/**
	 * 保存学生成绩详情表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated ExamResultsDetail examResultsDetail) {
		examResultsDetailService.save(examResultsDetail);
		return renderResult(Global.TRUE, text("保存学生成绩详情表成功！"));
	}
	
	/**
	 * 删除学生成绩详情表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(ExamResultsDetail examResultsDetail) {
		examResultsDetailService.delete(examResultsDetail);
		return renderResult(Global.TRUE, text("删除学生成绩详情表成功！"));
	}

	/**
	 * 加载考生成绩详情
	 * @param examUser
	 * @return
	 */
	@RequestMapping(value = "getExamResultsDetail")
	@ResponseBody
	public CommonResult getExamResultsDetail(ExamUser examUser) {
		CommonResult comRes = new CommonResult();
		if(StringUtils.isBlank(examUser.getId())||StringUtils.isBlank(examUser.getExamId())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("请求参数异常");
			return comRes;
		}

		//首选验证 是否包含得分项 ture包含 false不包含
		if(examResultsDetailService.validationData(examUser.getId())){
			comRes.setData(examResultsDetailService.getExamResultsDetail(examUser));
		}else{
			//不包含 先保存不得分项 在进行查询
			examResultsDetailService.saveExamResults(examUser);
			comRes.setData(examResultsDetailService.getExamResultsDetail(examUser));
		}
		return comRes;
	}

	//加载成绩详情图片
	@RequestMapping(value = "getExamResultsVehiclePicture")
	@ResponseBody
	public CommonResult getExamResultsVehiclePicture(ExamUser examUser) {
		if(StringUtils.isBlank(examUser.getId())){
			return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER,"请求参数有误!");
		}
		return new CommonResult(examResultsDetailService.getExamResultsVehiclePicture(examUser));
	}


	//根据图片类型id 查询第四五模块学生与教师上传图片
	@RequestMapping(value = "getExamResultsPictureByType")
	@ResponseBody
	public CommonResult getExamResultsPictureByType(String examUserId,String pictureTypeId) {
		if(StringUtils.isBlank(examUserId) || StringUtils.isBlank(pictureTypeId)){
			return new CommonResult(CodeConstant.WRONG_REQUEST_PARAMETER,"请求参数有误!");
		}
		return new CommonResult(examResultsDetailService.getExamResultsPictureByType(examUserId,pictureTypeId));
	}


}