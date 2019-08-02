/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.Calculate;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 计算车辆价值DAO接口
 * @author chenlitao
 * @version 2019-07-04
 */
@MyBatisDao
public interface CalculateDao extends CrudDao<Calculate> {

    Map<String, String> getEstimateByType(@Param("examUserId") String examUserId, @Param("paperId") String paperId);

    /**
     * 查询算法类型
     * @param calculate
     */
    String getType(Calculate calculate);

}