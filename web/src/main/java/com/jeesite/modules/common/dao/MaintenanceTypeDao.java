/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.MaintenanceType;

/**
 * common_maintenance_typeDAO接口
 * @author liangtao
 * @version 2019-07-26
 */
@MyBatisDao
public interface MaintenanceTypeDao extends CrudDao<MaintenanceType> {
	
}