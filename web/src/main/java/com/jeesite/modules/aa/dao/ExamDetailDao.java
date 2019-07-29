/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.ExamDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 考试详情表DAO接口
 * @author lvchangwei
 * @version 2019-07-09
 */
@MyBatisDao
public interface ExamDetailDao extends CrudDao<ExamDetail> {

    void deleteExamDetail(@Param("examId") String examId);
}