/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.ExamResultsDetail;
import com.jeesite.modules.aa.entity.TechnologyInfo;

import java.util.List;
import java.util.Map;

/**
 * 学生成绩详情表DAO接口
 * @author liangtao
 * @version 2019-07-22
 */
@MyBatisDao
public interface ExamResultsDetailDao extends CrudDao<ExamResultsDetail> {

    //获取不等分项--鉴定技术部分
    List<TechnologyInfo> getExamResults(String examUserId, String paperId);
}