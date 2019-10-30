/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.entity.CheckBodySkeleton;
import com.jeesite.modules.aa.service.CheckBodySkeletonService;
import com.jeesite.modules.aa.vo.CheckBodySkeletonVO;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 检查车体骨架Controller
 *
 * @author lvchangwei
 * @version 2019-07-02
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/checkBodySkeleton")
public class CheckBodySkeletonController extends BaseController {

    @Autowired
    private CheckBodySkeletonService checkBodySkeletonService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public CheckBodySkeleton get(String id, boolean isNewRecord) {
        return checkBodySkeletonService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(CheckBodySkeleton checkBodySkeleton, Model model) {
        model.addAttribute("checkBodySkeleton", checkBodySkeleton);
        return "modules/aa/checkBodySkeletonList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<CheckBodySkeleton> listData(CheckBodySkeleton checkBodySkeleton, HttpServletRequest request, HttpServletResponse response) {
        checkBodySkeleton.setPage(new Page<>(request, response));
        Page<CheckBodySkeleton> page = checkBodySkeletonService.findPage(checkBodySkeleton);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(CheckBodySkeleton checkBodySkeleton, Model model) {
        model.addAttribute("checkBodySkeleton", checkBodySkeleton);
        return "modules/aa/checkBodySkeletonForm";
    }

    /**
     * 保存检查车体骨架
     */
    @PostMapping(value = "save")
    @ResponseBody
    public CommonResult save(@Validated String veliclePicJson) {
        ExamUser examUser = UserUtils.getExamUser();
        checkBodySkeletonService.saveBatch(examUser, veliclePicJson);
        return new CommonResult();
    }

    /**
     * 删除检查车体骨架
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(CheckBodySkeleton checkBodySkeleton) {
        checkBodySkeletonService.delete(checkBodySkeleton);
        return renderResult(Global.TRUE, text("删除检查车体骨架成功！"));
    }

    /**
     * 车体骨架信息加载
     */
    @RequestMapping(value = "findList")
    @ResponseBody
    public CommonResult findList() {
        ExamUser examUser = UserUtils.getExamUser();
        CheckBodySkeletonVO vo = checkBodySkeletonService.findBodySkeleton(examUser);
        CommonResult result = new CommonResult();
        result.setData(vo);
        return result;
    }

    /**
     * 判定事故车加载
     */
    @RequestMapping(value = "findAccidentVehicle")
    @ResponseBody
    public CommonResult findAccidentVehicle() {
        ExamUser examUser = UserUtils.getExamUser();
        CheckBodySkeletonVO vo = checkBodySkeletonService.findAccidentVehicle(examUser);
        CommonResult result = new CommonResult();
        result.setData(vo);
        return result;
    }

    /**
     * 车体骨架照片保存（学生查看教师答案时专用）
     */
    @RequestMapping(value = "saveBodySkeletonImg")
    @ResponseBody
    public CommonResult saveBodySkeletonImg() {
        return checkBodySkeletonService.saveBodySkeletonImg();
    }
}