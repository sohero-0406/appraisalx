/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 试卷Entity
 * @author lvchangwei
 * @version 2019-07-16
 */
@Table(name="aa_paper", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="name", attrName="name", label="试卷名称", queryType=QueryType.LIKE),
		@Column(name="total_score", attrName="totalScore", label="试卷总分"),
		@Column(name="state", attrName="state", label="启用禁用状态"),
		@Column(name="create_by", attrName="createBy", label="创建者"),
		@Column(name="update_date", attrName="createDate", label="创建时间"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class Paper extends PreEntity<Paper> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String name;		// 试卷名称
	private BigDecimal totalScore;		// 试卷总分
	private String state;		//启用禁用状态
	private String createBy;		//创建者
	private Timestamp createDate;		//创建时间

	private CarInfo carInfo;
	
	public Paper() {
		this(null);
	}

	public Paper(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="用户id长度不能超过 64 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=255, message="试卷名称长度不能超过 255 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public void setStatus(String status) {
		this.status = status;
	}

	public CarInfo getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(CarInfo carInfo) {
		this.carInfo = carInfo;
	}

	@Length(min=0, max=16, message="启用禁用状态长度不能超过 16 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String getCreateBy() {
		return createBy;
	}

	@Override
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	@Override
	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
}