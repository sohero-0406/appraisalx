/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.VehicleInstallInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 车辆加装信息DAO接口
 * @author chenlitao
 * @version 2019-07-03
 */
@MyBatisDao
public interface VehicleInstallInfoDao extends CrudDao<VehicleInstallInfo> {

	List<Map<String,String>> getVehicleInstallName(@Param("examUserId") String examUserId, @Param("paperId") String paperId);
}