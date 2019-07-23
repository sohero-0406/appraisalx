/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.AppraisalReport;

/**
 * 二手车鉴定评估报告DAO接口
 * @author lvchangwei
 * @version 2019-07-19
 */
@MyBatisDao
public interface AppraisalReportDao extends CrudDao<AppraisalReport> {

    String findAppraisalNumMAX();

}