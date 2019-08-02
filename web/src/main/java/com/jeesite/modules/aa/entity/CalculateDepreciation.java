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
 * 折旧率估值法Entity
 * @author chenlitao
 * @version 2019-07-05
 */
@Table(name="aa_calculate_depreciation", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，折旧率估值法", isPK=true),
		@Column(name="sale_price", attrName="salePrice", label="车辆销售价格"),
		@Column(name="depreciation_rate", attrName="depreciationRate", label="年限折旧率之和"),
		@Column(name="process", attrName="process", label="计算过程"),
		@Column(name="price", attrName="price", label="评估价格"),
		@Column(name="calculate_id", attrName="calculateId", label="外键id"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class CalculateDepreciation extends PreEntity<CalculateDepreciation> {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal salePrice;		// 车辆销售价格
	private String depreciationRate;		// 年限折旧率之和
	private String process;		//计算过程
	private BigDecimal price;		// 评估价格
	private String calculateId;		// 外键id

	//非数据库字段
	private int useYear;		//使用年限
	
	public CalculateDepreciation() {
		this(null);
	}

	public CalculateDepreciation(String id){
		super(id);
	}
	
	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@Length(min=0, max=10, message="年限折旧率之和长度不能超过 10 个字符")
	public String getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(String depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	@Length(min=0, max=512, message="计算过程长度不能超过 512 个字符")
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
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getCalculateId() {
		return calculateId;
	}

	public void setCalculateId(String calculateId) {
		this.calculateId = calculateId;
	}

	public int getUseYear() {
		return useYear;
	}

	public void setUseYear(int useYear) {
		this.useYear = useYear;
	}
}