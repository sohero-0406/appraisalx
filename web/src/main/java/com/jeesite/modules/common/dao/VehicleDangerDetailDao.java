/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.VehicleDanger;
import com.jeesite.modules.common.entity.VehicleDangerDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 出险记录详情表DAO接口
 * @author liangtao
 * @version 2019-07-12
 */
@MyBatisDao
public interface VehicleDangerDetailDao extends CrudDao<VehicleDangerDetail> {

    List<VehicleDangerDetail> findVehicleDangerDetail(@Param(value = "vehicleDangerDetail") VehicleDangerDetail vehicleDangerDetail);

    List<VehicleDangerDetail> findListByVehicleDangers(@Param(value = "vehicleDangers") List<VehicleDanger> vehicleDangers);
}