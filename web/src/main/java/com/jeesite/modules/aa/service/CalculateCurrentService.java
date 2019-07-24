/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import com.jeesite.modules.common.entity.ExamUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.CalculateCurrent;
import com.jeesite.modules.aa.dao.CalculateCurrentDao;

/**
 * 现行市价法Service
 * @author lvchangwei
 * @version 2019-07-22
 */
@Service
@Transactional(readOnly=true)
public class CalculateCurrentService extends CrudService<CalculateCurrentDao, CalculateCurrent> {
	
	/**
	 * 获取单条数据
	 * @param calculateCurrent
	 * @return
	 */
	@Override
	public CalculateCurrent get(CalculateCurrent calculateCurrent) {
		return super.get(calculateCurrent);
	}
	
	/**
	 * 查询分页数据
	 * @param calculateCurrent 查询条件
	 * @param calculateCurrent.page 分页对象
	 * @return
	 */
	@Override
	public Page<CalculateCurrent> findPage(CalculateCurrent calculateCurrent) {
		return super.findPage(calculateCurrent);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param calculateCurrent
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(CalculateCurrent calculateCurrent) {
		super.save(calculateCurrent);
	}
	
	/**
	 * 更新状态
	 * @param calculateCurrent
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(CalculateCurrent calculateCurrent) {
		super.updateStatus(calculateCurrent);
	}
	
	/**
	 * 删除数据
	 * @param calculateCurrent
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(CalculateCurrent calculateCurrent) {
		super.delete(calculateCurrent);
	}

	public CalculateCurrent getByEntity(CalculateCurrent calculateCurrent) {
		return dao.getByEntity(calculateCurrent);
	}

	/**
	 * 计算
	 * @param calculateCurrent
	 * @param examUser
	 * @return
	 */
	public CalculateCurrent calculate(CalculateCurrent calculateCurrent, ExamUser examUser) {





		return calculateCurrent;
	}
}