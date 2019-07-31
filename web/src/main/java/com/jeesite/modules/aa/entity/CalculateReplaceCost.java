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

import java.math.BigDecimal;

/**
 * 重置成本法Entity
 * @author chenlitao
 * @version 2019-07-05
 */
@Table(name="aa_calculate_replace_cost", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="calculate_id", attrName="calculateId", label="外键id"),
		@Column(name="sale_price", attrName="salePrice", label="新车销售价"),
		@Column(name="purchase_tax", attrName="purchaseTax", label="车辆购置税"),
		@Column(name="license_fee", attrName="licenseFee", label="牌照费"),
		@Column(name="update_repeat_cost", attrName="updateRepeatCost", label="更新重置成本"),
		@Column(name="provide_use_year", attrName="provideUseYear", label="规定使用年限"),
		@Column(name="tec_new_rate_coefficient", attrName="tecNewRateCoefficient", label="技术鉴定成新率系数"),
		@Column(name="tec_new_rate", attrName="tecNewRate", label="技术鉴定成新率"),
		@Column(name="year_new_rate_coefficient", attrName="yearNewRateCoefficient", label="年限成新率系数"),
		@Column(name="year_new_rate", attrName="yearNewRate", label="年限成新率"),
		@Column(name="all_new_rate", attrName="allNewRate", label="综合成新率"),
		@Column(name="process", attrName="process", label="计算过程"),
		@Column(name="price", attrName="price", label="评估价格"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class CalculateReplaceCost extends PreEntity<CalculateReplaceCost> {
	
	private static final long serialVersionUID = 1L;
	private String calculateId;		// 外键id
	private BigDecimal salePrice;		// 新车销售价
	private BigDecimal purchaseTax;		// 车辆购置税
	private BigDecimal licenseFee;		// 牌照费
	private BigDecimal updateRepeatCost;		// 更新重置成本
	private String provideUseYear;		// 规定使用年限
	private String tecNewRateCoefficient;		// 技术鉴定成新率系数
	private String tecNewRate;		// 技术鉴定成新率
	private String yearNewRateCoefficient;		// 年限成新率系数
	private String yearNewRate;		// 年限成新率
	private String allNewRate;		// 综合成新率
	private String process;		//计算过程
	private BigDecimal price;		// 评估价格

	//非数据库字段
	private String score;		// 车辆技术状况分值
	private String identifyDate;		//鉴定日期
	
	public CalculateReplaceCost() {
		this(null);
	}

	public CalculateReplaceCost(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getCalculateId() {
		return calculateId;
	}

	public void setCalculateId(String calculateId) {
		this.calculateId = calculateId;
	}
	
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	
	public BigDecimal getPurchaseTax() {
		return purchaseTax;
	}

	public void setPurchaseTax(BigDecimal purchaseTax) {
		this.purchaseTax = purchaseTax;
	}
	
	public BigDecimal getLicenseFee() {
		return licenseFee;
	}

	public void setLicenseFee(BigDecimal licenseFee) {
		this.licenseFee = licenseFee;
	}
	
	public BigDecimal getUpdateRepeatCost() {
		return updateRepeatCost;
	}

	public void setUpdateRepeatCost(BigDecimal updateRepeatCost) {
		this.updateRepeatCost = updateRepeatCost;
	}
	
	@Length(min=0, max=2, message="规定使用年限长度不能超过 2 个字符")
	public String getProvideUseYear() {
		return provideUseYear;
	}

	public void setProvideUseYear(String provideUseYear) {
		this.provideUseYear = provideUseYear;
	}
	
	@Length(min=0, max=8, message="技术鉴定成新率系数长度不能超过 8 个字符")
	public String getTecNewRateCoefficient() {
		return tecNewRateCoefficient;
	}

	public void setTecNewRateCoefficient(String tecNewRateCoefficient) {
		this.tecNewRateCoefficient = tecNewRateCoefficient;
	}
	
	@Length(min=0, max=8, message="技术鉴定成新率长度不能超过 8 个字符")
	public String getTecNewRate() {
		return tecNewRate;
	}

	public void setTecNewRate(String tecNewRate) {
		this.tecNewRate = tecNewRate;
	}
	
	@Length(min=0, max=8, message="年限成新率系数长度不能超过 8 个字符")
	public String getYearNewRateCoefficient() {
		return yearNewRateCoefficient;
	}

	public void setYearNewRateCoefficient(String yearNewRateCoefficient) {
		this.yearNewRateCoefficient = yearNewRateCoefficient;
	}
	
	@Length(min=0, max=8, message="年限成新率长度不能超过 8 个字符")
	public String getYearNewRate() {
		return yearNewRate;
	}

	public void setYearNewRate(String yearNewRate) {
		this.yearNewRate = yearNewRate;
	}
	
	@Length(min=0, max=8, message="综合成新率长度不能超过 8 个字符")
	public String getAllNewRate() {
		return allNewRate;
	}

	public void setAllNewRate(String allNewRate) {
		this.allNewRate = allNewRate;
	}

	@Length(min=0, max=2048, message="计算过程长度不能超过 2048 个字符")
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getIdentifyDate() {
		return identifyDate;
	}

	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}
}