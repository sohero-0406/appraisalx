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
 * 车辆出险总表Entity
 * @author liangtao
 * @version 2019-07-12
 */
@Table(name="common_vehicle_danger_total", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，车辆出险总记录表", isPK=true),
		@Column(name="service_sum_count", attrName="serviceSumCount", label="出险总次数"),
		@Column(name="service_sum_money", attrName="serviceSumMoney", label="出险总金额", comment="出险总金额（单位：分）"),
		@Column(name="vin_code", attrName="vinCode", label="VIN码"),
		@Column(name="vehicle_type", attrName="vehicleType", label="车辆类型"),
		@Column(includeEntity=DataEntity.class)
	}, orderBy="a.id DESC"
)
public class VehicleDangerTotal extends PreEntity<VehicleDangerTotal> {
	
	private static final long serialVersionUID = 1L;
	private Long serviceSumCount;		// 出险总次数
	private String serviceSumMoney;		// 出险总金额（单位：分）
	private String vinCode;		// VIN码
	private String vehicleType;		// 车辆类型
	
	public VehicleDangerTotal() {
		this(null);
	}

	public VehicleDangerTotal(String id){
		super(id);
	}
	
	public Long getServiceSumCount() {
		return serviceSumCount;
	}

	public void setServiceSumCount(Long serviceSumCount) {
		this.serviceSumCount = serviceSumCount;
	}
	
	@Length(min=0, max=32, message="出险总金额长度不能超过 32 个字符")
	public String getServiceSumMoney() {
		return serviceSumMoney;
	}

	public void setServiceSumMoney(String serviceSumMoney) {
		this.serviceSumMoney = serviceSumMoney;
	}
	
	@Length(min=0, max=64, message="VIN码长度不能超过 64 个字符")
	public String getVinCode() {
		return vinCode;
	}

	public void setVinCode(String vinCode) {
		this.vinCode = vinCode;
	}
	
	@Length(min=0, max=64, message="车辆类型长度不能超过 64 个字符")
	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
}