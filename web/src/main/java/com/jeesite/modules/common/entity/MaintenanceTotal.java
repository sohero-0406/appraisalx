/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 车辆维保总表Entity
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Table(name="common_maintenance_total", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，车辆维保总记录表", isPK=true),
		@Column(name="maintenance_count", attrName="maintenanceCount", label="维保次数"),
		@Column(name="vin_code", attrName="vinCode", label="VIN码"),
		@Column(name="model_name", attrName="modelName", label="车型名称", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class MaintenanceTotal extends PreEntity<MaintenanceTotal> {
	
	private static final long serialVersionUID = 1L;
	private Long maintenanceCount;		// 维保次数
	private String vinCode;		// VIN码
	private String modelName;		// 车型名称
	
	public MaintenanceTotal() {
		this(null);
	}

	public MaintenanceTotal(String id){
		super(id);
	}
	
	public Long getMaintenanceCount() {
		return maintenanceCount;
	}

	public void setMaintenanceCount(Long maintenanceCount) {
		this.maintenanceCount = maintenanceCount;
	}
	
	@Length(min=0, max=64, message="VIN码长度不能超过 64 个字符")
	public String getVinCode() {
		return vinCode;
	}

	public void setVinCode(String vinCode) {
		this.vinCode = vinCode;
	}
	
	@Length(min=0, max=64, message="车型名称长度不能超过 64 个字符")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
}