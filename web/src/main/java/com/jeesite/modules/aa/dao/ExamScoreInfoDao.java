/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import com.jeesite.modules.aa.entity.ExamScoreInfo;

import java.util.List;

/**
 * 考试评分点DAO接口
 * @author liangtao
 * @version 2019-07-16
 */
@MyBatisDao
public interface ExamScoreInfoDao extends CrudDao<ExamScoreInfo> {

    List<ExamScoreClassify> getExamScoreInfo();
	
}