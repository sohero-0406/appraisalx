/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.SystemSetting;
import com.jeesite.modules.aa.dao.SystemSettingDao;

/**
 * 大平台域名设置Service
 * @author lvchangwei
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly=true)
public class SystemSettingService extends CrudService<SystemSettingDao, SystemSetting> {
	
	/**
	 * 获取单条数据
	 * @param systemSetting
	 * @return
	 */
	@Override
	public SystemSetting get(SystemSetting systemSetting) {
		return super.get(systemSetting);
	}
	
	/**
	 * 查询分页数据
	 * @param systemSetting 查询条件
	 * @param systemSetting.page 分页对象
	 * @return
	 */
	@Override
	public Page<SystemSetting> findPage(SystemSetting systemSetting) {
		return super.findPage(systemSetting);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param systemSetting
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(SystemSetting systemSetting) {
		super.save(systemSetting);
	}
	
	/**
	 * 更新状态
	 * @param systemSetting
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(SystemSetting systemSetting) {
		super.updateStatus(systemSetting);
	}
	
	/**
	 * 删除数据
	 * @param systemSetting
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(SystemSetting systemSetting) {
		super.delete(systemSetting);
	}

	public SystemSetting getByEntity(SystemSetting systemSetting) {
		return dao.getByEntity(systemSetting);
	}

	@Transactional(readOnly=false)
	public void  phyDeleteByEntity(SystemSetting systemSetting){
		dao.phyDeleteByEntity(systemSetting);
	}

	@Transactional(readOnly=false)
	public long  insertSystemSetting(SystemSetting systemSetting){
        return dao.insert(systemSetting);
	}

}