/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.CheckBodySkeleton;
import com.jeesite.modules.aa.entity.ExamResultsDetail;
import com.jeesite.modules.aa.entity.ExamScoreClassify;
import com.jeesite.modules.aa.entity.TechnologyInfo;
import org.apache.ibatis.annotations.Param;

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
    List<TechnologyInfo> getExamResults(@Param("examUserId") String examUserId, @Param("paperId") String paperId);

    //获取车架
    List<CheckBodySkeleton> getCheckBodySkeleton(@Param("examUserId") String examUserId, @Param("paperId") String paperId);

    //获取字典值
    List<Map<String,String>> getCarBodyDic();

    //验证数据是否包含不得分项
    List<ExamResultsDetail> validationData(String examUserId);

    //获取考生成绩详情
    List<ExamScoreClassify> getExamResultsDetail(String examUserId);

}