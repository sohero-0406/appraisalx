/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 委托车辆信息DAO接口
 * @author chenlitao
 * @version 2019-06-29
 */
@MyBatisDao
public interface CarInfoDao extends CrudDao<CarInfo> {

    CarInfo findCarInfoBySortStu(Map<String,String> hs);

    CarInfo findLeftInfor(@Param("examUser")ExamUser examUser);

}