/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.vo.ExamVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.service.ExamService;
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
import java.util.List;

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

    /**
     * 获取考试信息
     */
    @RequestMapping(value = "getExamInfo")
    @ResponseBody
    public CommonResult getExamInfo(String keyword) {
        CommonResult comRes = new CommonResult();
        List<Exam> examList = examService.getExamInfo(keyword);
        comRes.setData(examList);
        return comRes;
    }

    /**
     * 保存考试功能
     */
    @RequestMapping(value = "saveExamInfo")
    @ResponseBody
    public CommonResult saveExamInfo(ExamVO examVO, String examScoreJson) {
        CommonResult comRes = examService.saveExamInfo(examVO,examScoreJson);
        return comRes;
    }

    /**
     * 新建修改考试功能
     * (包含考试评分项和内容模板)
     */
    @RequestMapping(value = "addOrUpdateExam")
    @ResponseBody
    public CommonResult addExam(String examId) {
        CommonResult comRes =  examService.addOrUpdateExam(examId);
        return comRes;
    }

    /**
     *  依据考试删除考试
     * @param examId
     * @return
     */
    @RequestMapping(value = "deleteExam")
    @ResponseBody
    public CommonResult deleteExam(Exam exam) {
        CommonResult comRes = examService.deleteExam(exam);
        return comRes;
    }


    /**
     * 查看考试详情接口
     */
    @RequestMapping(value = "getExam")
    @ResponseBody
    public CommonResult getExam(Exam exam) {
        CommonResult comRes = new CommonResult();
        examService.get(exam);

        comRes.setData(exam);
        return comRes;
    }

    /**
     * 操作考试（未开始，考试中，未统计，已出分）
     */
    @RequestMapping(value = "operationExam")
    @ResponseBody
    public CommonResult operationExam(Exam exam) {
        //修改考生状态
        CommonResult comRes = examService.updateExamSate(exam);
        return comRes;
    }
}