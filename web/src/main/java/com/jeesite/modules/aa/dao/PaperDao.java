/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.Paper;

import java.util.List;
import java.util.Map;

/**
 * 试卷DAO接口
 * @author lvchangwei
 * @version 2019-07-16
 */
@MyBatisDao
public interface PaperDao extends CrudDao<Paper> {

    List<Paper> findPaper();

    List<CarInfo> findPaperBySortTea(Map<String,String> hs);
}