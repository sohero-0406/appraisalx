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
 * 现行市价法Entity
 * @author lvchangwei
 * @version 2019-07-22
 */
@Table(name="aa_calculate_current", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，现行市价法", isPK=true),
		@Column(name="is_compute_new_rate", attrName="isComputeNewRate", label="是否计算成新率，1", comment="是否计算成新率，1：是，0：否"),
		@Column(name="calculate_id", attrName="calculateId", label="外键id"),
		@Column(name="p1_type", attrName="p1Type", label="参照物1的价格更有益的车辆0", comment="参照物1的价格更有益的车辆0：参照车辆，1是评估车辆"),
		@Column(name="param1_id", attrName="param1Id", label="参照物1的id"),
		@Column(name="p1_excellent_price", attrName="p1ExcellentPrice", label="参照物1的优异价格"),
		@Column(name="p1_k1", attrName="p1K1", label="参照物1的k1"),
		@Column(name="p1_k2", attrName="p1K2", label="参照物1的k2"),
		@Column(name="p1_k3", attrName="p1K3", label="参照物1的k3"),
		@Column(name="p1_k4", attrName="p1K4", label="参照物1的k4"),
		@Column(name="p1_price", attrName="p1Price", label="参照物1的评估值"),
		@Column(name="param2_id", attrName="param2Id", label="参照物2的id"),
		@Column(name="p2_type", attrName="p2Type", label="参照物2的价格更有益的车辆0", comment="参照物2的价格更有益的车辆0：参照车辆，1是评估车辆"),
		@Column(name="p2_excellent_price", attrName="p2ExcellentPrice", label="参照物2的优异价格"),
		@Column(name="p2_k1", attrName="p2K1", label="参照物2的k1"),
		@Column(name="p2_k2", attrName="p2K2", label="参照物2的k2"),
		@Column(name="p2_k3", attrName="p2K3", label="参照物2的k3"),
		@Column(name="p2_k4", attrName="p2K4", label="参照物2的k4"),
		@Column(name="p2_price", attrName="p2Price", label="参照物2的评估值"),
		@Column(name="price", attrName="price", label="评估价格"),
		@Column(name="p1_process", attrName="p1Process", label="参照物1的计算过程"),
		@Column(name="p2_process", attrName="p2Process", label="参照物2的计算过程"),
		@Column(name="configuration_type", attrName="configurationType", label="车辆配置类型"),
		@Column(name="engine_type", attrName="engineType", label="发动机类型"),
		@Column(name="transmission_type", attrName="transmissionType", label="变速箱类型"),
		@Column(name="trade_time", attrName="tradeTime", label="交易时间"),
		@Column(name="use_year", attrName="useYear", label="使用年限-年"),
		@Column(name="new_rate", attrName="newRate", label="成新率", comment="成新率(%)"),
		@Column(name="payment_type", attrName="paymentType", label="付款方式"),
		@Column(name="trade_place", attrName="tradePlace", label="二手车交易地点"),
		@Column(name="price_index", attrName="priceIndex", label="物价指数"),
		@Column(name="process", attrName="process", label="计算过程"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class CalculateCurrent extends PreEntity<CalculateCurrent> {
	
	private static final long serialVersionUID = 1L;
	private String isComputeNewRate;		// 是否计算成新率，1：是，0：否
	private String calculateId;		// 外键id
	private String p1Type;		// 参照物1的价格更有益的车辆0：参照车辆，1是评估车辆
	private String param1Id;		// 参照物1的id
	private Double p1ExcellentPrice;		// 参照物1的优异价格
	private Double p1K1;		// 参照物1的k1
	private Double p1K2;		// 参照物1的k2
	private Double p1K3;		// 参照物1的k3
	private Double p1K4;		// 参照物1的k4
	private Double p1Price;		// 参照物1的评估值
	private String param2Id;		// 参照物2的id
	private String p2Type;		// 参照物2的价格更有益的车辆0：参照车辆，1是评估车辆
	private Double p2ExcellentPrice;		// 参照物2的优异价格
	private Double p2K1;		// 参照物2的k1
	private Double p2K2;		// 参照物2的k2
	private Double p2K3;		// 参照物2的k3
	private Double p2K4;		// 参照物2的k4
	private Double p2Price;		// 参照物2的评估值
	private Double price;		// 评估价格
	private String p1Process;		// 参照物1的计算过程
	private String p2Process;		// 参照物2的计算过程
	private String configurationType;		// 车辆配置类型
	private String engineType;		// 发动机类型
	private String transmissionType;		// 变速箱类型
	private String tradeTime;		// 交易时间
	private Integer useYear;		// 使用年限-年
	private String newRate;		// 成新率(%)
	private String paymentType;		// 付款方式
	private String tradePlace;		// 二手车交易地点
	private String priceIndex;		// 物价指数
	private String process;		// 计算过程
	
	public CalculateCurrent() {
		this(null);
	}

	public CalculateCurrent(String id){
		super(id);
	}
	
	@Length(min=0, max=1, message="是否计算成新率，1长度不能超过 1 个字符")
	public String getIsComputeNewRate() {
		return isComputeNewRate;
	}

	public void setIsComputeNewRate(String isComputeNewRate) {
		this.isComputeNewRate = isComputeNewRate;
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getCalculateId() {
		return calculateId;
	}

	public void setCalculateId(String calculateId) {
		this.calculateId = calculateId;
	}

	@Length(min=0, max=1, message="参照物1的价格更有益的车辆0长度不能超过 1 个字符")
	public String getP1Type() {
		return p1Type;
	}

	public void setP1Type(String p1Type) {
		this.p1Type = p1Type;
	}
	
	@Length(min=0, max=64, message="参照物1的id长度不能超过 64 个字符")
	public String getParam1Id() {
		return param1Id;
	}

	public void setParam1Id(String param1Id) {
		this.param1Id = param1Id;
	}
	
	public Double getP1ExcellentPrice() {
		return p1ExcellentPrice;
	}

	public void setP1ExcellentPrice(Double p1ExcellentPrice) {
		this.p1ExcellentPrice = p1ExcellentPrice;
	}
	
	public Double getP1K1() {
		return p1K1;
	}

	public void setP1K1(Double p1K1) {
		this.p1K1 = p1K1;
	}
	
	public Double getP1K2() {
		return p1K2;
	}

	public void setP1K2(Double p1K2) {
		this.p1K2 = p1K2;
	}
	
	public Double getP1K3() {
		return p1K3;
	}

	public void setP1K3(Double p1K3) {
		this.p1K3 = p1K3;
	}
	
	public Double getP1K4() {
		return p1K4;
	}

	public void setP1K4(Double p1K4) {
		this.p1K4 = p1K4;
	}
	
	public Double getP1Price() {
		return p1Price;
	}

	public void setP1Price(Double p1Price) {
		this.p1Price = p1Price;
	}
	
	@Length(min=0, max=64, message="参照物2的id长度不能超过 64 个字符")
	public String getParam2Id() {
		return param2Id;
	}

	public void setParam2Id(String param2Id) {
		this.param2Id = param2Id;
	}
	
	@Length(min=0, max=1, message="参照物2的价格更有益的车辆0长度不能超过 1 个字符")
	public String getP2Type() {
		return p2Type;
	}

	public void setP2Type(String p2Type) {
		this.p2Type = p2Type;
	}
	
	public Double getP2ExcellentPrice() {
		return p2ExcellentPrice;
	}

	public void setP2ExcellentPrice(Double p2ExcellentPrice) {
		this.p2ExcellentPrice = p2ExcellentPrice;
	}
	
	public Double getP2K1() {
		return p2K1;
	}

	public void setP2K1(Double p2K1) {
		this.p2K1 = p2K1;
	}
	
	public Double getP2K2() {
		return p2K2;
	}

	public void setP2K2(Double p2K2) {
		this.p2K2 = p2K2;
	}
	
	public Double getP2K3() {
		return p2K3;
	}

	public void setP2K3(Double p2K3) {
		this.p2K3 = p2K3;
	}
	
	public Double getP2K4() {
		return p2K4;
	}

	public void setP2K4(Double p2K4) {
		this.p2K4 = p2K4;
	}
	
	public Double getP2Price() {
		return p2Price;
	}

	public void setP2Price(Double p2Price) {
		this.p2Price = p2Price;
	}
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Length(min=0, max=2048, message="参照物1的计算过程长度不能超过 2048 个字符")
	public String getP1Process() {
		return p1Process;
	}

	public void setP1Process(String p1Process) {
		this.p1Process = p1Process;
	}
	
	@Length(min=0, max=2048, message="参照物2的计算过程长度不能超过 2048 个字符")
	public String getP2Process() {
		return p2Process;
	}

	public void setP2Process(String p2Process) {
		this.p2Process = p2Process;
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
	
	@Length(min=0, max=10, message="变速箱类型长度不能超过 10 个字符")
	public String getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
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
	
	@Length(min=0, max=32, message="成新率长度不能超过 32 个字符")
	public String getNewRate() {
		return newRate;
	}

	public void setNewRate(String newRate) {
		this.newRate = newRate;
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
	
	@Length(min=0, max=512, message="计算过程长度不能超过 512 个字符")
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}
	
}