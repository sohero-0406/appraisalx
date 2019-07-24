/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.service.PictureUserService;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 用户图片表Controller
 * @author chenlitao
 * @version 2019-06-29
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/pictureUser")
public class PictureUserController extends BaseController {

	@Autowired
	private PictureUserService pictureUserService;
	
	/**
	 * 获取数据
	 */
	@ModelAttribute
	public PictureUser get(String id, boolean isNewRecord) {
		return pictureUserService.get(id, isNewRecord);
	}

	/**
	 *
	 * @param picFile 图片文件
	 * @param id 原图片id
	 * @param pictureTypeId 图片类型id
	 * @param needDiscern 是:1,否:0
	 * 可识别的图片类型包括：身份证正面、车牌照、车辆识别代号（VIN）、
	 * 机动车行驶证正页、机动车登记证1、机动车销售统一发票
	 * @return
	 */
	@PostMapping(value="uploadPicture")
	@ResponseBody
	public CommonResult uploadPicture(MultipartFile picFile, String id, String pictureTypeId, String needDiscern) throws IOException {
		ExamUser examUser = UserUtils.getExamUser();
		return  pictureUserService.saveAndDiscernPicture(examUser, picFile, id, pictureTypeId, needDiscern);
	}

	@PostMapping(value="findPictureLibraryList")
	@ResponseBody
	public CommonResult findPictureByParentTypeId(){
		ExamUser examUser = UserUtils.getExamUser();
		String[] parentTypeIds = new String[]{
				"1143431479216775168","1143437059610071040","1143439093974253568",
				"1143441175747194880","1143446339264172032"};
		return pictureUserService.findPictureByParentTypeId(examUser, parentTypeIds);
	}

	/**
	 * 加载评估车辆拍照信息
	 * @return
	 */
	@PostMapping(value = "findVehiclePicture")
	@ResponseBody
	public CommonResult findVehiclePicture(){
		ExamUser examUser = UserUtils.getExamUser();
		CommonResult comRes = new CommonResult();
		String [] parentTypeIds = new String[]{"1143439093974253568"};//记录车辆基本信息
		List<PictureUser> pictureUserList = pictureUserService.findChildPicture(examUser,parentTypeIds);
		comRes.setData(pictureUserList);
		return comRes;
	}
	/**
	 * 查询列表
	 */
	@RequestMapping(value = {"list", ""})
	public String list(PictureUser pictureUser, Model model) {
		model.addAttribute("pictureUser", pictureUser);
		return "modules/aa/pictureUserList";
	}
	
	/**
	 * 查询列表数据
	 */
	@RequestMapping(value = "listData")
	@ResponseBody
	public Page<PictureUser> listData(PictureUser pictureUser, HttpServletRequest request, HttpServletResponse response) {
		pictureUser.setPage(new Page<>(request, response));
		Page<PictureUser> page = pictureUserService.findPage(pictureUser);
		return page;
	}

	/**
	 * 查看编辑表单
	 */
	@RequestMapping(value = "form")
	public String form(PictureUser pictureUser, Model model) {
		model.addAttribute("pictureUser", pictureUser);
		return "modules/aa/pictureUserForm";
	}

	/**
	 * 保存用户图片表
	 */
	@PostMapping(value = "save")
	@ResponseBody
	public String save(@Validated PictureUser pictureUser) {
		pictureUserService.save(pictureUser);
		return renderResult(Global.TRUE, text("保存用户图片表成功！"));
	}
	
	/**
	 * 删除用户图片表
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public String delete(PictureUser pictureUser) {
		pictureUserService.delete(pictureUser);
		return renderResult(Global.TRUE, text("删除用户图片表成功！"));
	}
	
}