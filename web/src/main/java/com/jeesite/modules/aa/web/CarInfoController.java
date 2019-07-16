/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.service.CarInfoService;
import com.jeesite.modules.aa.vo.BaseInfoVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import com.jeesite.modules.sys.entity.DictData;
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
 * 委托车辆信息Controller
 * @author chenlitao
 * @version 2019-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/carInfo")
public class CarInfoController extends BaseController {

	@Autowired
	private CarInfoService carInfoService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public CarInfo get(String id, boolean isNewRecord) {
		return carInfoService.get(id, isNewRecord);
	}
	
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(CarInfo carInfo, Model model) {
		model.addAttribute("carInfo", carInfo);
		return "modules/aa/carInfoList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<CarInfo> listData(CarInfo carInfo, HttpServletRequest request, HttpServletResponse response) {
		carInfo.setPage(new Page<>(request, response));
		Page<CarInfo> page = carInfoService.findPage(carInfo);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(CarInfo carInfo, Model model) {
		model.addAttribute("carInfo", carInfo);
		return "modules/aa/carInfoForm";
	}

	/**
	 * 保存委托车辆信息
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated CarInfo carInfo) {
		carInfoService.save(carInfo);
		return renderResult(Global.TRUE, text("保存委托车辆信息成功！"));
	}
	
	/**
	 * 删除委托车辆信息
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(CarInfo carInfo) {
		carInfoService.delete(carInfo);
		return renderResult(Global.TRUE, text("删除委托车辆信息成功！"));
	}
	/**
	 * 保存车辆基本信息和委托方基本信息
	 * @param baseInfoVO
	 * @return
	 */
	@PostMapping(value = "saveBaseInfo")
	@ResponseBody
	public CommonResult saveBaseInfo(@Validated BaseInfoVO baseInfoVO){
		ExamUser examUser = UserUtils.getExamUser();
		carInfoService.saveBaseInfo(baseInfoVO, examUser);
		return new CommonResult();
	}

	/**
	 * 获取车辆基本信息和委托方基本信息
	 * @return
	 */
	@PostMapping(value = "getBaseInfo")
	@ResponseBody
	public CommonResult getBaseInfo(String[] pictureTypeIds){
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		comRes.setData(carInfoService.getBaseInfo(examUser, pictureTypeIds));
		return comRes;
	}
}