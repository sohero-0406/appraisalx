/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import java.util.List;

import com.jeesite.common.network.IpUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.common.entity.ExamUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.entity.OperationLog;
import com.jeesite.modules.common.dao.OperationLogDao;

/**
 * 操作日志Service
 * @author liangtao
 * @version 2019-07-29
 */
@Service
@Transactional(readOnly=true)
public class OperationLogService extends CrudService<OperationLogDao, OperationLog> {
	
	/**
	 * 获取单条数据
	 * @param operationLog
	 * @return
	 */
	@Override
	public OperationLog get(OperationLog operationLog) {
		return super.get(operationLog);
	}
	
	/**
	 * 查询分页数据
	 * @param operationLog 查询条件
	 * @param operationLog.page 分页对象
	 * @return
	 */
	@Override
	public Page<OperationLog> findPage(OperationLog operationLog) {
		return super.findPage(operationLog);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param operationLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(OperationLog operationLog) {
		super.save(operationLog);
	}
	
	/**
	 * 更新状态
	 * @param operationLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(OperationLog operationLog) {
		super.updateStatus(operationLog);
	}
	
	/**
	 * 删除数据
	 * @param operationLog
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(OperationLog operationLog) {
		super.delete(operationLog);
	}

	//查询操作日志（包含模糊查询）
	@Transactional(readOnly=false)
	public List<OperationLog> getOperationLog(String keyword,String[] idList) {
		return dao.getOperationLog(keyword,idList);
	}

    public void saveObj(ExamUser examUser, String msg) {
		OperationLog log = new OperationLog();
		log.setLoginName(examUser.getUserNum());
		log.setUserId(examUser.getUserId());
		log.setRequestIp(IpUtils.getRemoteAddr(ServletUtils.getRequest()));
		log.setOperatingContent(msg);
		this.save(log);
    }
}