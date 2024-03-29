/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.common.entity.ExamUser;
import org.apache.ibatis.annotations.Param;

/**
 * 车辆等级评定DAO接口
 * @author lvchangwei
 * @version 2019-07-09
 */
@MyBatisDao
public interface VehicleGradeAssessDao extends CrudDao<VehicleGradeAssess> {

    /**
     * 获取技术状况
     * @param vehicleGradeAssess
     * @return
     */
    String getTechnicalStatus(VehicleGradeAssess vehicleGradeAssess);

    /**
     *  统计扣分项
     */
    String sumTotalDeduct(@Param("examUser")ExamUser examUser);

}