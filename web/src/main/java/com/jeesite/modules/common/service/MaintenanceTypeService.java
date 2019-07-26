/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.entity.MaintenanceType;
import com.jeesite.modules.common.dao.MaintenanceTypeDao;

/**
 * common_maintenance_typeService
 * @author liangtao
 * @version 2019-07-26
 */
@Service
@Transactional(readOnly=true)
public class MaintenanceTypeService extends CrudService<MaintenanceTypeDao, MaintenanceType> {
	
	/**
	 * 获取单条数据
	 * @param maintenanceType
	 * @return
	 */
	@Override
	public MaintenanceType get(MaintenanceType maintenanceType) {
		return super.get(maintenanceType);
	}
	
	/**
	 * 查询分页数据
	 * @param maintenanceType 查询条件
	 * @param maintenanceType.page 分页对象
	 * @return
	 */
	@Override
	public Page<MaintenanceType> findPage(MaintenanceType maintenanceType) {
		return super.findPage(maintenanceType);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param maintenanceType
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(MaintenanceType maintenanceType) {
		super.save(maintenanceType);
	}
	
	/**
	 * 更新状态
	 * @param maintenanceType
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(MaintenanceType maintenanceType) {
		super.updateStatus(maintenanceType);
	}
	
	/**
	 * 删除数据
	 * @param maintenanceType
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(MaintenanceType maintenanceType) {
		super.delete(maintenanceType);
	}
	
}