/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.Calculate;
import com.jeesite.modules.aa.entity.CalculateCurrent;

/**
 * 现行市价法DAO接口
 * @author lvchangwei
 * @version 2019-07-22
 */
@MyBatisDao
public interface CalculateCurrentDao extends CrudDao<CalculateCurrent> {

    /**
     * 根据计算类型，查询被评估车辆相关参数
     * @param calculate
     * @return
     */
    CalculateCurrent getByCalculate(Calculate calculate);
}