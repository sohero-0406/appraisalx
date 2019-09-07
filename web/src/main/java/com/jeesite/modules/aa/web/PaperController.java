/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import alvinJNI.UrlDecrypt;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import com.jeesite.modules.common.web.MaintenanceTotalController;
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
import com.jeesite.modules.aa.entity.Paper;
import com.jeesite.modules.aa.service.PaperService;

import java.util.List;

/**
 * 试卷Controller
 *
 * @author lvchangwei
 * @version 2019-07-16
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/paper")
public class PaperController extends BaseController {

    @Autowired
    private PaperService paperService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public Paper get(String id, boolean isNewRecord) {
        return paperService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(Paper paper, Model model) {
        model.addAttribute("paper", paper);
        return "modules/aa/paperList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<Paper> listData(Paper paper, HttpServletRequest request, HttpServletResponse response) {
        paper.setPage(new Page<>(request, response));
        Page<Paper> page = paperService.findPage(paper);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(Paper paper, Model model) {
        model.addAttribute("paper", paper);
        return "modules/aa/paperForm";
    }

    /**
     * 保存试卷
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated Paper paper) {
        paperService.save(paper);
        return renderResult(Global.TRUE, text("保存试卷成功！"));
    }

    /**
     * 删除试卷
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(Paper paper) {
        paperService.delete(paper);
        return renderResult(Global.TRUE, text("删除试卷成功！"));
    }

    @RequestMapping(value = "getPaperList")
    @ResponseBody
    public CommonResult getPaperList1(HttpServletRequest request, String keyword) {
        Class<?>[] classes = {String.class};
        Object[] obs = {keyword};
        CommonResult result = UrlDecrypt.test2("getPaperList", this, PaperController.class, request, classes, obs);
        if (result == null) {
            return new CommonResult(CodeConstant.REGISTE_INFO_ERROR, "您未注册或者系统没有检测到硬件信息，或者您破坏了注册信息！");
        }
        return result;
    }

    /**
     * @description: 查询试卷列表
     * @param: [keyword]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/12
     * @time: 13:37
     */
    public CommonResult getPaperList(String keyword) {
        CommonResult comRes = new CommonResult();
        List<Paper> paperList = paperService.findPaper(keyword);
        comRes.setData(paperList);
        return comRes;
    }


    /**
     * 删除试卷
     */
    @RequestMapping(value = "deletePaper")
    @ResponseBody
    public CommonResult deletePaper(Paper paper) {
        if (paper.getId().isEmpty()) {
            CommonResult comRes = new CommonResult();
            comRes.setCode("1010");
            comRes.setMsg("未传入ID");
            return comRes;
        }
        paperService.delete(paper);
        return new CommonResult();
    }

    /**
     * @description: 试卷编辑校验--试卷修改
     * @param: [paperId]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/10
     * @time: 17:41
     */
    @RequestMapping(value = "paperEditCheck")
    @ResponseBody
    public CommonResult paperEditCheck(String paperId) {
        ExamUser examUser = UserUtils.getExamUser();
        if (StringUtils.isBlank(paperId)) {
            return new CommonResult(CodeConstant.REQUEST_FAILED, "试卷Id为空");
        }
        if (StringUtils.isNotBlank(examUser.getId())) {
            return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "试卷编辑校验通过");
        } else {
            // 教师
            Exam exam = new Exam();
            exam.setPaperId(paperId);
            exam = paperService.findExam(exam);
            if (null == exam || (StringUtils.isBlank(exam.toString())) || (StringUtils.isNotBlank(exam.toString()) && ("1").equals(exam.getState()))) {
                // paperId 存入缓存
                examUser.setPaperId(paperId);
                CacheUtils.put("examUser", examUser.getUserId(), examUser);
                // 该试卷未添加进考试、该试卷添加进考试 但其状态为 1   以上两种情况均可以通过编辑校验
                return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "试卷编辑校验通过");
            }
            return new CommonResult(CodeConstant.REQUEST_FAILED, "当前试卷被占用，不可进行编辑");
        }
    }

    /**
     * 查询添加考试 试卷列表
     */
    @RequestMapping(value = "selectExamPaperList")
    @ResponseBody
    public CommonResult selectExamPaperList() {
        Paper paper = new Paper();
        paper.setState("0");
        List<Paper> paperList = paperService.findList(paper);
        return new CommonResult(paperList);
    }

    /**
     * 修改试卷状态
     */
    @RequestMapping(value = "updatePaperState")
    @ResponseBody
    public CommonResult updatePaperState(String paperId) {
        ExamUser examUser = UserUtils.getExamUser();
        Paper paper = paperService.get(paperId);
        if (null == paper) {
            return new CommonResult(CodeConstant.DATA_NOT_FOUND, "试卷不存在!");
        }
        paper.setUserId(examUser.getUserId());
        String state = paper.getState();
        if ("1".equals(state)) {
            paper.setState("0");
        }
        if ("0".equals(state)) {
            paper.setState("1");
        }
        paperService.save(paper);
        return new CommonResult(paper.getState());
    }
}