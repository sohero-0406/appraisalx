/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.common.dao.VehicleInfoDao;
import com.jeesite.modules.common.entity.VehicleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆配置全表Service
 * @author chenlitao
 * @version 2019-07-04
 */
@Service
@Transactional(readOnly=true)
public class VehicleInfoService extends CrudService<VehicleInfoDao, VehicleInfo> {
	@Autowired
	private VehicleInfoDao vehicleInfoDao;
	/**
	 * 获取单条数据
	 * @param vehicleInfo
	 * @return
	 */
	@Override
	public VehicleInfo get(VehicleInfo vehicleInfo) {
		return super.get(vehicleInfo);
	}
	
	/**
	 * 查询分页数据
	 * @param vehicleInfo 查询条件
	 * @return
	 */
	@Override
	public Page<VehicleInfo> findPage(VehicleInfo vehicleInfo) {
		return super.findPage(vehicleInfo);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param vehicleInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(VehicleInfo vehicleInfo) {
		super.save(vehicleInfo);
	}
	
	/**
	 * 更新状态
	 * @param vehicleInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(VehicleInfo vehicleInfo) {
		super.updateStatus(vehicleInfo);
	}
	
	/**
	 * 删除数据
	 * @param vehicleInfo
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(VehicleInfo vehicleInfo) {
		super.delete(vehicleInfo);
	}

	public List<VehicleInfo> findVehicleName(String chexiId){
		if(chexiId == null || chexiId.trim().length() <= 0){
			return null;
		}
		return vehicleInfoDao.findVehicleName(chexiId);
	}

	public VehicleInfo getByEntity(VehicleInfo vehicleInfo){ return dao.getByEntity(vehicleInfo); }

	/**
	 * 查询车型(返回车型)
	 */
	public VehicleInfo getCarModel(VehicleInfo vehicleInfo){
		return vehicleInfoDao.getCarModel(vehicleInfo.getChexingId());
	}

}