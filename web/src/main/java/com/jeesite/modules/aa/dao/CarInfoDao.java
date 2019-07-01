/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.CarInfo;

/**
 * 委托车辆信息DAO接口
 * @author chenlitao
 * @version 2019-06-29
 */
@MyBatisDao
public interface CarInfoDao extends CrudDao<CarInfo> {
	
}