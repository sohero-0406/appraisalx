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
 * 车辆出险记录表Entity
 * @author liangtao
 * @version 2019-07-12
 */
@Table(name="common_vehicle_danger", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，车辆出险记录表", isPK=true),
		@Column(name="common_vehicle_danger_total_id", attrName="commonVehicleDangerTotalId", label="车辆出险总记录表id"),
		@Column(name="danger_date", attrName="dangerDate", label="出险日期"),
		@Column(name="service_money", attrName="serviceMoney", label="维修金额", comment="维修金额（单位：分）"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.id DESC"
)
public class VehicleDanger extends PreEntity<VehicleDanger> {
	
	private static final long serialVersionUID = 1L;
	private String commonVehicleDangerTotalId;		// 车辆出险总记录表id
	private String dangerDate;		// 出险日期
	private String serviceMoney;		// 维修金额（单位：分）

	/**
	 * 数据库不存在字段 显示对象用
	 */
	private String vin;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public VehicleDanger() {
		this(null);
	}

	public VehicleDanger(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="车辆出险总记录表id长度不能超过 64 个字符")
	public String getCommonVehicleDangerTotalId() {
		return commonVehicleDangerTotalId;
	}

	public void setCommonVehicleDangerTotalId(String commonVehicleDangerTotalId) {
		this.commonVehicleDangerTotalId = commonVehicleDangerTotalId;
	}
	
	@Length(min=0, max=32, message="出险日期长度不能超过 32 个字符")
	public String getDangerDate() {
		return dangerDate;
	}

	public void setDangerDate(String dangerDate) {
		this.dangerDate = dangerDate;
	}
	
	@Length(min=0, max=20, message="维修金额长度不能超过 20 个字符")
	public String getServiceMoney() {
		return serviceMoney;
	}

	public void setServiceMoney(String serviceMoney) {
		this.serviceMoney = serviceMoney;
	}
	
}