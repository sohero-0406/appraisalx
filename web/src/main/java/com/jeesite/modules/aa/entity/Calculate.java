/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 计算车辆价值Entity
 * @author chenlitao
 * @version 2019-07-04
 */
@Table(name="aa_calculate", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="exam_user_id", attrName="examUserId", label="外键id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="type", attrName="type", label="算法类型"),
		@Column(name="begin_price", attrName="beginPrice", label="得分价格开始值"),
		@Column(name="end_price", attrName="endPrice", label="得分价格结束值"),
		@Column(name="second_car_price", attrName="secondCarPrice", label="二手车市场参考价格(万元)"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class Calculate extends PreEntity<Calculate> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键id
	private String paperId;		// 试卷id
	private String type;		// 算法类型（1-折旧率估值法 2-公里数估值法 3-重置成本法 4-现行市价法）
	private Double beginPrice;		// 得分价格开始值
	private Double endPrice;		// 得分价格结束值
	private String secondCarPrice;  // 二手车市场参考价格
	
	public Calculate() {
		this(null);
	}

	public Calculate(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getExamUserId() {
		return examUserId;
	}

	public void setExamUserId(String examUserId) {
		this.examUserId = examUserId;
	}
	
	@Length(min=0, max=64, message="试卷id长度不能超过 64 个字符")
	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	
	@Length(min=0, max=10, message="算法类型长度不能超过 10 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Double getBeginPrice() {
		return beginPrice;
	}

	public void setBeginPrice(Double beginPrice) {
		this.beginPrice = beginPrice;
	}
	
	public Double getEndPrice() {
		return endPrice;
	}

	public void setEndPrice(Double endPrice) {
		this.endPrice = endPrice;
	}

	@Length(min=0, max=10, message="二手车市场参考价格长度不能超过 10 个字符")
	public String getSecondCarPrice() {
		return secondCarPrice;
	}

	public void setSecondCarPrice(String secondCarPrice) {
		this.secondCarPrice = secondCarPrice;
	}
}