/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.aa.entity.Reference;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 参照物表DAO接口
 * @author lvchangwei
 * @version 2019-07-16
 */
@MyBatisDao
public interface ReferenceDao extends CrudDao<Reference> {

    void deleteReference(@Param("referenceIdList") String[] referenceIdList);

    List<Reference> findReferenceListByKeyword(Reference reference);
}