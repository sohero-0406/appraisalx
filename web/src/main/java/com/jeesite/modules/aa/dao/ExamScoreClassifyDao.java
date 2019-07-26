/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.ExamScoreClassify;

import java.util.List;
import java.util.Map;

/**
 * 考试评分分类总分数DAO接口
 * @author liangtao
 * @version 2019-07-16
 */
@MyBatisDao
public interface ExamScoreClassifyDao extends CrudDao<ExamScoreClassify> {

    //根据考试id 获取试卷评分项
    List<Map<String,Object>> getExamScoreInfo(String examId);
}