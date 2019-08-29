/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import com.alibaba.fastjson.JSONArray;
import com.jeesite.common.config.Global;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.excel.ExcelExport;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.entity.OperationLog;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.ExamUserService;
import com.jeesite.modules.common.utils.UserUtils;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;

/**
 * common_exam_userController
 *
 * @author lvchangwei
 * @version 2019-06-27
 */
@Controller
@RequestMapping(value = "${adminPath}/common/examUser")
public class ExamUserController extends BaseController {

    @Autowired
    private ExamUserService examUserService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public ExamUser get(String id, boolean isNewRecord) {
        return examUserService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequestMapping(value = {"list", ""})
    public String list(ExamUser examUser, Model model) {
        model.addAttribute("examUser", examUser);
        return "modules/common/examUserList";
    }

    /**
     * 查询列表数据
     */
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<ExamUser> listData(ExamUser examUser, HttpServletRequest request, HttpServletResponse response) {
        examUser.setPage(new Page<>(request, response));
        Page<ExamUser> page = examUserService.findPage(examUser);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequestMapping(value = "form")
    public String form(ExamUser examUser, Model model) {
        model.addAttribute("examUser", examUser);
        return "modules/common/examUserForm";
    }

    /**
     * 保存common_exam_user
     */
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated ExamUser examUser) {
        examUserService.save(examUser);
        return renderResult(Global.TRUE, text("保存common_exam_user成功！"));
    }

    /**
     * 删除common_exam_user
     */
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(ExamUser examUser) {
        examUserService.delete(examUser);
        return renderResult(Global.TRUE, text("删除common_exam_user成功！"));
    }

//    //保存考生
//    @RequestMapping(value = "saveExamUser")
//    @ResponseBody
//    public CommonResult saveExamUser(@Validated String examUserJson, String examUserId) {
//        CommonResult comRes = examUserService.saveExamUser(examUserJson, examUserId);
//        return comRes;
//    }

    //批量删除考生
    @RequestMapping(value = "deleteExamUser")
    @ResponseBody
    public CommonResult deleteExamUser(@Validated String examUserIdListJson) {
        CommonResult comRes = examUserService.deletExamUser(examUserIdListJson);
        return comRes;
    }


    //查询考试成绩列表
    @RequestMapping(value = "getExamUserScoreList")
    @ResponseBody
    public CommonResult getExamUserScoreList(String examId) {
        CommonResult comRes = new CommonResult();
        //判断开始id 是否为空
        if (!StringUtils.isNotBlank(examId)) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
            comRes.setMsg("请求参数有误");
            return comRes;
        }
        comRes.setData(examUserService.getExamUserScoreList(examId));
        return comRes;
    }

    /**
     * 成绩批量导出
     */
    @RequestMapping(value = "exportResults")
    @ResponseBody
    public void exportOperationLog(HttpServletResponse response, String examId) {
        if (StringUtils.isBlank(examId)) {
            logger.warn("考试id未传!");
            return;
        }
        Exam exam = new Exam();
        exam.setId(examId);
        ExamUser examUser = new ExamUser();
        examUser.setExamId(examId);
        List<ExamUser> examUserList = examUserService.findList(examUser);
        StringBuilder stringBuilder = new StringBuilder();
        int len = examUserList.size();
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                stringBuilder.append(examUserList.get(i).getUserId());
            } else {
                stringBuilder.append(examUserList.get(i).getUserId() + ",");
            }
        }

        CommonResult loadStuListComRes = examUserService.getLoadStuListByIds(stringBuilder.toString());
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(loadStuListComRes.getCode())) {
            logger.error(loadStuListComRes.getMsg());
            return;
        }
        JSONArray array = JSONArray.parseArray(loadStuListComRes.getData().toString());
        if (array.size() > 0) {
            List list = examUserService.getExamUserListByPlatfrom(array, examId);
            String fileName = "学生成绩列表" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            try (ExcelExport ee = new ExcelExport(null, ExamUser.class)) {
                ee.setDataList(list).write(response, fileName);
            } catch (Exception e) {
                logger.warn("考试成绩导出异常!");
            }
        }

    }

    /**
     * 考试计时
     */
    @RequestMapping(value = "examTiming")
    @ResponseBody
    public CommonResult examTiming() {
        ExamUser examUser = UserUtils.getExamUser();
        return examUserService.examTiming(examUser);
    }



}












