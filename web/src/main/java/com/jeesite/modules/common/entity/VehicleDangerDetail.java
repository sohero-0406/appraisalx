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
 * 出险记录详情表Entity
 * @author liangtao
 * @version 2019-07-12
 */
@Table(name="common_vehicle_danger_detail", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，出险记录详情表", isPK=true),
		@Column(name="common_vehicle_danger_id", attrName="commonVehicleDangerId", label="出险记录表id"),
		@Column(name="danger_single_type", attrName="dangerSingleType", label="理赔项类型", comment="理赔项类型（更换，维修，材料）"),
		@Column(name="danger_single_name", attrName="dangerSingleName", label="理赔名称", queryType=QueryType.LIKE),
		@Column(name="danger_single_money", attrName="dangerSingleMoney", label="理赔金额", comment="理赔金额（单位：分）"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.id DESC"
)
public class VehicleDangerDetail extends PreEntity<VehicleDangerDetail> {
	
	private static final long serialVersionUID = 1L;
	private String commonVehicleDangerId;		// 出险记录表id
	private String dangerSingleType;		// 理赔项类型（更换，维修，材料）
	private String dangerSingleName;		// 理赔名称
	private String dangerSingleMoney;		// 理赔金额（单位：分）
	
	public VehicleDangerDetail() {
		this(null);
	}

	public VehicleDangerDetail(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="出险记录表id长度不能超过 64 个字符")
	public String getCommonVehicleDangerId() {
		return commonVehicleDangerId;
	}

	public void setCommonVehicleDangerId(String commonVehicleDangerId) {
		this.commonVehicleDangerId = commonVehicleDangerId;
	}
	
	@Length(min=0, max=16, message="理赔项类型长度不能超过 16 个字符")
	public String getDangerSingleType() {
		return dangerSingleType;
	}

	public void setDangerSingleType(String dangerSingleType) {
		this.dangerSingleType = dangerSingleType;
	}
	
	@Length(min=0, max=128, message="理赔名称长度不能超过 128 个字符")
	public String getDangerSingleName() {
		return dangerSingleName;
	}

	public void setDangerSingleName(String dangerSingleName) {
		this.dangerSingleName = dangerSingleName;
	}
	
	@Length(min=0, max=20, message="理赔金额长度不能超过 20 个字符")
	public String getDangerSingleMoney() {
		return dangerSingleMoney;
	}

	public void setDangerSingleMoney(String dangerSingleMoney) {
		this.dangerSingleMoney = dangerSingleMoney;
	}
	
}