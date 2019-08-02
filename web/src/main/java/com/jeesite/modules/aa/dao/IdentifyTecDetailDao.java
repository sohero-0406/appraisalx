/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.IdentifyTec;
import com.jeesite.modules.aa.entity.IdentifyTecDetail;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 鉴定技术状况详情DAO接口
 * @author lvchangwei
 * @version 2019-07-04
 */
@MyBatisDao
public interface IdentifyTecDetailDao extends CrudDao<IdentifyTecDetail> {

    IdentifyTec findData(IdentifyTec identifyTec);

    List<IdentifyTec> findIdentityTecCondition(IdentifyTec identifyTec);
}