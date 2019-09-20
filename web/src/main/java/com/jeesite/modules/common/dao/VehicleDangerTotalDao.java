/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.VehicleDangerTotal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆出险总表DAO接口
 * @author liangtao
 * @version 2019-07-12
 */
@MyBatisDao
public interface VehicleDangerTotalDao extends CrudDao<VehicleDangerTotal> {

    List<VehicleDangerTotal> findVehicleDangerTotalList(VehicleDangerTotal vehicleDangerTotal);

    List<VehicleDangerTotal> findVehicleDangerTotalById(@Param(value = "split") String[] split);
}