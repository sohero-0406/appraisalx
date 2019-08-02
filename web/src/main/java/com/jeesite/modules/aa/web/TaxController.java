/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.Tax;
import com.jeesite.modules.aa.service.TaxService;
import com.jeesite.modules.common.entity.CommonResult;
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

/**
 * 税率表Controller
 * @author lvchangwei
 * @version 2019-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/tax")
public class TaxController extends BaseController {

	@Autowired
	private TaxService taxService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public Tax get(String id, boolean isNewRecord) {
		return taxService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Tax tax, Model model) {
		model.addAttribute("tax", tax);
		return "modules/aa/taxList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<Tax> listData(Tax tax, HttpServletRequest request, HttpServletResponse response) {
		tax.setPage(new Page<>(request, response));
		Page<Tax> page = taxService.findPage(tax);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(Tax tax, Model model) {
		model.addAttribute("tax", tax);
		return "modules/aa/taxForm";
	}

	/**
	 * 保存税率表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated Tax tax) {
		taxService.save(tax);
		return renderResult(Global.TRUE, text("保存税率表成功！"));
	}
	
	/**
	 * 删除税率表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(Tax tax) {
		taxService.delete(tax);
		return renderResult(Global.TRUE, text("删除税率表成功！"));
	}

	@PostMapping(value = "saveTax")
	@ResponseBody
	public CommonResult saveTax(Tax tax) {
		CommonResult comRes = new CommonResult();
		if(null==tax || StringUtils.isBlank(tax.getId())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("参数不能为空");
			return comRes;
		}
		if (StringUtils.isBlank(tax.getPurchase())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("购置税税率不能为空");
			return comRes;
		}
		if (StringUtils.isBlank(tax.getVat())){
			comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
			comRes.setMsg("增值税税率不能为空");
			return comRes;
		}
		//验证是否出现多条数据
		Tax taxTest = new Tax();
		int len = taxService.findList(taxTest).size();
		//因为此数据必为一条 所以一旦多条数据 全部删除，保存用户最后一次输入的有效数据
		if(len>1){
			taxService.phyDeleteByEntity(taxTest);
			if(1==(int)taxService.insertTax(tax)){
				comRes.setMsg("修改成功!");
				return comRes;
			}else{
				comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
				comRes.setMsg("修改失败!");
				return comRes;
			}
		}
		comRes.setMsg("修改成功!");
		taxService.save(tax);
		return comRes;
	}
	
}