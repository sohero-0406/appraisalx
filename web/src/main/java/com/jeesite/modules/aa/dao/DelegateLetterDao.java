/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.DelegateLetter;

/**
 * 委托书信息DAO接口
 * @author lvchangwei
 * @version 2019-07-18
 */
@MyBatisDao
public interface DelegateLetterDao extends CrudDao<DelegateLetter> {

    Integer findAppraisalNumMAX(String year);
}