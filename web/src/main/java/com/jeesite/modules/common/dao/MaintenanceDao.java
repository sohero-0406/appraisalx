/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.Maintenance;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆维保记录DAO接口
 * @author jiangyanfei
 * @version 2019-08-05
 */
@MyBatisDao
public interface MaintenanceDao extends CrudDao<Maintenance> {

    List<Maintenance> findMaintenance(@Param(value = "maintenance") Maintenance maintenance);

    Maintenance findMaintenanceDetail(@Param(value = "maintenance") Maintenance maintenance);
}