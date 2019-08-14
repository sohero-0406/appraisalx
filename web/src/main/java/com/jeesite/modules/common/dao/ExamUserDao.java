/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * common_exam_userDAO接口
 * @author lvchangwei
 * @version 2019-06-27
 */
@MyBatisDao
public interface ExamUserDao extends CrudDao<ExamUser> {

    void updateExamUserEndTime(String examId);

    List<ExamUser> getExamUserScoreList(@Param("examId")String examId);

    void deleteExamUser(String examUserId);

    ExamUser getAllowLogin(ExamUser examUser);

    /**
     * 查询学生成绩 排序
     */
    List<ExamUser> getExamUserListByOrder(@Param("examId") String examId);



    List<ExamUser> getExamStateByUserId(@Param("userIdList") List<String> userIdList,@Param("examId") String examId);



}