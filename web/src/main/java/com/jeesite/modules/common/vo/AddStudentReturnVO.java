package com.jeesite.modules.common.vo;

import com.alibaba.fastjson.JSONArray;
import com.jeesite.modules.common.entity.ExamUser;

import java.util.List;

/**
 * 添加学生返回VO
 */
public class AddStudentReturnVO {

    private List<ExamUser> stuList;
    private JSONArray assessmentNameList;

    public List<ExamUser> getStuList() {
        return stuList;
    }

    public void setStuList(List<ExamUser> stuList) {
        this.stuList = stuList;
    }

    public JSONArray getAssessmentNameList() {
        return assessmentNameList;
    }

    public void setAssessmentNameList(JSONArray assessmentNameList) {
        this.assessmentNameList = assessmentNameList;
    }
}
