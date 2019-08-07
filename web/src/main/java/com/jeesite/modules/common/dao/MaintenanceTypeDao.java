/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.Maintenance;
import com.jeesite.modules.common.entity.MaintenanceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * common_maintenance_typeDAO接口
 * @author jiangyanfei
 * @version 2019-08-05
 */
@MyBatisDao
public interface MaintenanceTypeDao extends CrudDao<MaintenanceType> {

    List<MaintenanceType> findRepairRecords(@Param(value = "maintenanceType") MaintenanceType maintenanceType, @Param(value = "dictValue") String dictValue);

    List<MaintenanceType> findListByMaintenances(@Param(value = "maintenances") List<Maintenance> maintenances);
}