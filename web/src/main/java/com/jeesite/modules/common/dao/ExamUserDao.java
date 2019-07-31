/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.ExamUser;

import java.util.List;

/**
 * common_exam_userDAO接口
 * @author lvchangwei
 * @version 2019-06-27
 */
@MyBatisDao
public interface ExamUserDao extends CrudDao<ExamUser> {

    void updateExamUserEndTime(String examId);

    List<ExamUser> getExamUserScoreList(String examId);

    void deleteExamUser(String examUserId);

}