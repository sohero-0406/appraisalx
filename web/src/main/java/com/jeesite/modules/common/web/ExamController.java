/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.service.ExamService;

/**
 * common_examController
 *
 * @author lvchangwei
 * @version 2019-07-10
 */
@Controller
@RequestMapping(value = "${adminPath}/common/exam")
public class ExamController extends BaseController {

    @Autowired
    private ExamService examService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Exam get(String id, boolean isNewRecord) {
        return examService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Exam exam, Model model) {
        model.addAttribute("exam", exam);
        return "modules/common/examList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Exam> listData(Exam exam, HttpServletRequest request, HttpServletResponse response) {
        exam.setPage(new Page<>(request, response));
        Page<Exam> page = examService.findPage(exam);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(Exam exam, Model model) {
        model.addAttribute("exam", exam);
        return "modules/common/examForm";
    }

    /**
     * 保存common_exam
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated Exam exam) {
        examService.save(exam);
        return renderResult(Global.TRUE, text("保存common_exam成功！"));
    }

    /**
     * 删除common_exam
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Exam exam) {
        examService.delete(exam);
        return renderResult(Global.TRUE, text("删除common_exam成功！"));
    }

}