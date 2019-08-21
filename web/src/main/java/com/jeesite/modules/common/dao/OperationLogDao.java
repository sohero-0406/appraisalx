/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.dao;

import com.jeesite.common.dao.CrudDao;
import com.jeesite.common.mybatis.annotation.MyBatisDao;
import com.jeesite.modules.common.entity.OperationLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志DAO接口
 * @author liangtao
 * @version 2019-07-29
 */
@MyBatisDao
public interface OperationLogDao extends CrudDao<OperationLog> {
	List<OperationLog> getOperationLog(@Param("keyword") String keyword,@Param("idList")String[] idList);
}