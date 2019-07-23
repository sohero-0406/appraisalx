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
 * 鉴定技术状况详情Entity
 * @author lvchangwei
 * @version 2019-07-04
 */
@Table(name="aa_identify_tec_detail", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="indentity_tec_id", attrName="indentityTecId", label="外键id"),
		@Column(name="technology_info_id", attrName="technologyInfoId", label="鉴定项id"),
		@Column(name="code", attrName="code", label="缺陷状态代码"),
		@Column(name="degree", attrName="degree", label="缺陷程度"),
		@Column(name="deduct_num", attrName="deductNum", label="扣分数值"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class IdentifyTecDetail extends PreEntity<IdentifyTecDetail> {
	
	private static final long serialVersionUID = 1L;
	private String indentityTecId;		// 外键id
	private String technologyInfoId;		// 鉴定项id
	private String code;		// 缺陷状态代码
	private String degree;		// 缺陷程度
	private String deductNum;		// 扣分数值

	//非数据库字段
	private String typeName;	//检查项名称
	private String checkRes;	//有缺陷/正常
	private String stuOrTec;  //判断是否为老师答案/学生答案
	
	public IdentifyTecDetail() {
		this(null);
	}

	public IdentifyTecDetail(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getIndentityTecId() {
		return indentityTecId;
	}

	public void setIndentityTecId(String indentityTecId) {
		this.indentityTecId = indentityTecId;
	}
	
	@Length(min=0, max=64, message="鉴定项id长度不能超过 64 个字符")
	public String getTechnologyInfoId() {
		return technologyInfoId;
	}

	public void setTechnologyInfoId(String technologyInfoId) {
		this.technologyInfoId = technologyInfoId;
	}
	
	@Length(min=0, max=128, message="缺陷状态代码长度不能超过 128 个字符")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Length(min=0, max=512, message="缺陷程度长度不能超过 512 个字符")
	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}
	
	@Length(min=0, max=16, message="扣分数值长度不能超过 16 个字符")
	public String getDeductNum() {
		return deductNum;
	}

	public void setDeductNum(String deductNum) {
		this.deductNum = deductNum;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getCheckRes() {
		return checkRes;
	}

	public void setCheckRes(String checkRes) {
		this.checkRes = checkRes;
	}

	public String getStuOrTec() {
		return stuOrTec;
	}

	public void setStuOrTec(String stuOrTec) {
		this.stuOrTec = stuOrTec;
	}
}