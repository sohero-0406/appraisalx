/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.CheckBodySkeleton;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 检查车体骨架DAO接口
 * @author lvchangwei
 * @version 2019-07-02
 */
@MyBatisDao
public interface CheckBodySkeletonDao extends CrudDao<CheckBodySkeleton> {

    /**
     * 判定事故车加载
     */
    List<CheckBodySkeleton> findAccidentVehicle(CheckBodySkeleton checkBodySkeleton);

    /**
     * 查询鉴定项
     */
    String getTechnologyInfo(CheckBodySkeleton checkBodySkeleton);

    List<CheckBodySkeleton> findCheckProjectResults(@Param(value = "checkBodySkeleton") CheckBodySkeleton checkBodySkeleton);
}