/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.VehicleDocumentInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 车辆单证信息DAO接口
 * @author lvchangwei
 * @version 2019-07-01
 */
@MyBatisDao
public interface VehicleDocumentInfoDao extends CrudDao<VehicleDocumentInfo> {

    List<String> findOtherDocuments(@Param(value = "vehicleDocumentInfo") VehicleDocumentInfo vehicleDocumentInfo);

    List<VehicleDocumentInfo> findExistedDocuments(@Param(value = "vehicleDocumentInfo")VehicleDocumentInfo vehicleDocumentInfo);
}