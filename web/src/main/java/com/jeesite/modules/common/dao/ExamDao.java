/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * common_examDAO接口
 * @author lvchangwei
 * @version 2019-07-10
 */
@MyBatisDao
public interface ExamDao extends CrudDao<Exam> {

    /**
     * 批量获取考试数据
     * @return
     */
    List<Exam> getExamInfo(@Param("keyword") String keyword,@Param("type")String type);

    List<String> getUserByExamId(String examId);

    List<Exam> findExamForCheck(Exam exam);

    List<Exam> selectExamIdList(@Param("examIds") String[] examIds);
}