/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 参照物表Entity
 * @author lvchangwei
 * @version 2019-07-16
 */
@Table(name="aa_reference", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="model", attrName="model", label="车型id"),
		@Column(name="configuration_type", attrName="configurationType", label="车辆配置类型"),
		@Column(name="engine_type", attrName="engineType", label="发动机类型"),
		@Column(name="engine_capacity", attrName="engineCapacity", label="发动机排量"),
		@Column(name="transmission_type", attrName="transmissionType", label="变速箱类型"),
		@Column(name="exhaust_emission_standard", attrName="exhaustEmissionStandard", label="尾气排放标准"),
		@Column(name="sale_conditions", attrName="saleConditions", label="销售条件"),
		@Column(name="trade_time", attrName="tradeTime", label="交易时间"),
		@Column(name="use_year", attrName="useYear", label="使用年限-年"),
		@Column(name="used_time", attrName="usedTime", label="已使用时间"),
		@Column(name="appraisal_score", attrName="appraisalScore", label="技术鉴定分值"),
		@Column(name="new_rate", attrName="newRate", label="成新率", comment="成新率(%)"),
		@Column(name="trade_number", attrName="tradeNumber", label="交易数量"),
		@Column(name="payment_type", attrName="paymentType", label="付款方式"),
		@Column(name="trade_place", attrName="tradePlace", label="二手车交易地点"),
		@Column(name="price_index", attrName="priceIndex", label="物价指数"),
		@Column(name="car_price", attrName="carPrice", label="二手车交易价格"),
		@Column(name="new_registration", attrName="newRegistration", label="初登记年月"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class Reference extends PreEntity<Reference> {
	
	private static final long serialVersionUID = 1L;
	private String model;		// 车型id
	private String configurationType;		// 车辆配置类型
	private String engineType;		// 发动机类型
	private Integer engineCapacity;		// 发动机排量
	private String transmissionType;		// 变速箱类型
	private String exhaustEmissionStandard;		// 尾气排放标准
	private String saleConditions;		// 销售条件
	private String tradeTime;		// 交易时间
	private Integer useYear;		// 使用年限-年
	private String usedTime;		// 已使用时间
	private String appraisalScore;		// 技术鉴定分值
	private String newRate;		// 成新率(%)
	private Integer tradeNumber;		// 交易数量
	private String paymentType;		// 付款方式
	private String tradePlace;		// 二手车交易地点
	private String priceIndex;		// 物价指数
	private Double carPrice;		// 二手车交易价格
	private String newRegistration;		// 初登记年月
	
	public Reference() {
		this(null);
	}

	public Reference(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="车型id长度不能超过 64 个字符")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	@Length(min=0, max=10, message="车辆配置类型长度不能超过 10 个字符")
	public String getConfigurationType() {
		return configurationType;
	}

	public void setConfigurationType(String configurationType) {
		this.configurationType = configurationType;
	}
	
	@Length(min=0, max=10, message="发动机类型长度不能超过 10 个字符")
	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}
	
	public Integer getEngineCapacity() {
		return engineCapacity;
	}

	public void setEngineCapacity(Integer engineCapacity) {
		this.engineCapacity = engineCapacity;
	}
	
	@Length(min=0, max=10, message="变速箱类型长度不能超过 10 个字符")
	public String getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}
	
	@Length(min=0, max=10, message="尾气排放标准长度不能超过 10 个字符")
	public String getExhaustEmissionStandard() {
		return exhaustEmissionStandard;
	}

	public void setExhaustEmissionStandard(String exhaustEmissionStandard) {
		this.exhaustEmissionStandard = exhaustEmissionStandard;
	}
	
	@Length(min=0, max=16, message="销售条件长度不能超过 16 个字符")
	public String getSaleConditions() {
		return saleConditions;
	}

	public void setSaleConditions(String saleConditions) {
		this.saleConditions = saleConditions;
	}
	
	@Length(min=0, max=32, message="交易时间长度不能超过 32 个字符")
	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	
	public Integer getUseYear() {
		return useYear;
	}

	public void setUseYear(Integer useYear) {
		this.useYear = useYear;
	}
	
	@Length(min=0, max=32, message="已使用时间长度不能超过 32 个字符")
	public String getUsedTime() {
		return usedTime;
	}

	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}
	
	@Length(min=0, max=16, message="技术鉴定分值长度不能超过 16 个字符")
	public String getAppraisalScore() {
		return appraisalScore;
	}

	public void setAppraisalScore(String appraisalScore) {
		this.appraisalScore = appraisalScore;
	}
	
	@Length(min=0, max=32, message="成新率长度不能超过 32 个字符")
	public String getNewRate() {
		return newRate;
	}

	public void setNewRate(String newRate) {
		this.newRate = newRate;
	}
	
	public Integer getTradeNumber() {
		return tradeNumber;
	}

	public void setTradeNumber(Integer tradeNumber) {
		this.tradeNumber = tradeNumber;
	}
	
	@Length(min=0, max=10, message="付款方式长度不能超过 10 个字符")
	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	
	@Length(min=0, max=128, message="二手车交易地点长度不能超过 128 个字符")
	public String getTradePlace() {
		return tradePlace;
	}

	public void setTradePlace(String tradePlace) {
		this.tradePlace = tradePlace;
	}
	
	@Length(min=0, max=16, message="物价指数长度不能超过 16 个字符")
	public String getPriceIndex() {
		return priceIndex;
	}

	public void setPriceIndex(String priceIndex) {
		this.priceIndex = priceIndex;
	}
	
	public Double getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(Double carPrice) {
		this.carPrice = carPrice;
	}
	
	@Length(min=0, max=32, message="初登记年月长度不能超过 32 个字符")
	public String getNewRegistration() {
		return newRegistration;
	}

	public void setNewRegistration(String newRegistration) {
		this.newRegistration = newRegistration;
	}
	
}