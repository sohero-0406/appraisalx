/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import alvinJNI.UrlDecrypt;
import com.jeesite.common.config.Global;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.Reference;
import com.jeesite.modules.aa.entity.Tax;
import com.jeesite.modules.aa.service.ReferenceService;
import com.jeesite.modules.common.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	public CommonResult deleteReference(String id) {
		CommonResult comRes = new CommonResult();
		if(StringUtils.isBlank(id)){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("请先选择数据!");
			return comRes;
		}
		referenceService.deleteReference(id);
		return comRes;
	}

	/**
	 * 查询单个参照物
	 */
	@RequestMapping(value = "findReference")
	@ResponseBody
	public CommonResult findReference(@Validated Reference reference) {
		CommonResult comRes = new CommonResult();
		comRes.setData(referenceService.get(reference));
		return comRes;
	}

    @RequestMapping(value = "findReferenceList")
    @ResponseBody
    public CommonResult findReferenceList1(HttpServletRequest request, Reference reference) {
        Class<?>[] classes = {Reference.class};
        Object[] obs = {reference};
        CommonResult result = UrlDecrypt.test2("findReferenceList", this, ReferenceController.class, request, classes, obs);
        if (result == null) {
            return new CommonResult(CodeConstant.REGISTE_INFO_ERROR, "您未注册或者系统没有检测到硬件信息，或者您破坏了注册信息！");
        }
        return result;
    }

	/** 
	* @description: 查询参照物列表 搜索关键字为 车型名称、VIN码 模糊查询
	* @param: [keyword]
	* @return: com.jeesite.modules.common.entity.CommonResult
	* @author: Jiangyf
	* @date: 2019/8/12 
	* @time: 10:40
	*/
    public CommonResult findReferenceList(Reference reference) {
		CommonResult comRes = new CommonResult();
        comRes.setData(referenceService.findReferenceList(reference));
		return comRes;
	}


	/**
	 * 初始化参照物
	 */
	@RequestMapping(value = "initReference")
	@ResponseBody
	public CommonResult initReference(@Validated Reference reference) {
		CommonResult comRes = new CommonResult();
		comRes.setData(referenceService.initReference(reference));
		return comRes;
	}
}