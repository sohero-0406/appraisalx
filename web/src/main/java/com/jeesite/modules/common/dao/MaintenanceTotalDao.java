/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.MaintenanceTotal;

/**
 * 车辆维保总表DAO接口
 * @author jiangyanfei
 * @version 2019-08-05
 */
@MyBatisDao
public interface MaintenanceTotalDao extends CrudDao<MaintenanceTotal> {
	
}