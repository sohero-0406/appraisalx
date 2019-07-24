/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.DelegateUser;

/**
 * 委托方信息DAO接口
 * @author chenlitao
 * @version 2019-06-29
 */
@MyBatisDao
public interface DelegateUserDao extends CrudDao<DelegateUser> {

    String findEntrustNumMAX(DelegateUser delegateUser);

}