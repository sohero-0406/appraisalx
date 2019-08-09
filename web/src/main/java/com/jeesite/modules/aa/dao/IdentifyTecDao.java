/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.IdentifyTec;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 鉴定技术状况DAO接口
 * @author lvchangwei
 * @version 2019-07-04
 */
@MyBatisDao
public interface IdentifyTecDao extends CrudDao<IdentifyTec> {

    List<IdentifyTec> findVehicleTecStatusResult(@Param(value = "identifyTec") IdentifyTec identifyTec);
}