/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.CarInfo;

import java.util.Map;

/**
 * 委托车辆信息DAO接口
 * @author chenlitao
 * @version 2019-06-29
 */
@MyBatisDao
public interface CarInfoDao extends CrudDao<CarInfo> {

    CarInfo findCarInfoBySortStu(Map<String,String> hs);

    /**
     * 查询车辆等级
     * @param carInfo
     * @return
     */
    String getLevel(CarInfo carInfo);

    /**
     * 查询环保标准
     * @param carInfo
     * @return
     */
    String getEnvironmentalStandard(CarInfo carInfo);

    /**
     * 查询车身颜色
     * @param carInfo
     */
    String getColor(CarInfo carInfo);
}