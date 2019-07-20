/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.List;

import com.jeesite.modules.aa.vo.VehicleGradeAssessVO;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.aa.dao.VehicleGradeAssessDao;

/**
 * 车辆等级评定Service
 * @author lvchangwei
 * @version 2019-07-09
 */
@Service
@Transactional(readOnly=true)
public class VehicleGradeAssessService extends CrudService<VehicleGradeAssessDao, VehicleGradeAssess> {
	
	/**
	 * 获取单条数据
	 * @param vehicleGradeAssess
	 * @return
	 */
	@Override
	public VehicleGradeAssess get(VehicleGradeAssess vehicleGradeAssess) {
		return super.get(vehicleGradeAssess);
	}
	
	/**
	 * 查询分页数据
	 * @param vehicleGradeAssess 查询条件
	 * @return
	 */
	@Override
	public Page<VehicleGradeAssess> findPage(VehicleGradeAssess vehicleGradeAssess) {
		return super.findPage(vehicleGradeAssess);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param vehicleGradeAssess
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(VehicleGradeAssess vehicleGradeAssess) {
		super.save(vehicleGradeAssess);
	}
	
	/**
	 * 更新状态
	 * @param vehicleGradeAssess
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(VehicleGradeAssess vehicleGradeAssess) {
		super.updateStatus(vehicleGradeAssess);
	}
	
	/**
	 * 删除数据
	 * @param vehicleGradeAssess
	 */
	@Override
	@Transactional(readOnly=false)
	public void delete(VehicleGradeAssess vehicleGradeAssess) {
		super.delete(vehicleGradeAssess);
	}

	/**
	 * 查询车辆等级评定
	 * @param examUser
	 * @return
	 */
    public VehicleGradeAssessVO getDetail(ExamUser examUser) {
		VehicleGradeAssessVO vo = new VehicleGradeAssessVO();
		VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
		vehicleGradeAssess.setExamUserId(examUser.getId());
		vehicleGradeAssess.setPaperId(examUser.getPaperId());
		vehicleGradeAssess = this.getByEntity(vehicleGradeAssess);
		vo.setVehicleGradeAssess(vehicleGradeAssess);

		//加载技术状况
		List<DictData> technicalStatusList = DictUtils.getDictList("aa_technical_status");
		vo.setTechnicalStatusList(technicalStatusList);
		return vo;
    }

	public VehicleGradeAssess getByEntity(VehicleGradeAssess vehicleGradeAssess) {
    	return dao.getByEntity(vehicleGradeAssess);
	}
}