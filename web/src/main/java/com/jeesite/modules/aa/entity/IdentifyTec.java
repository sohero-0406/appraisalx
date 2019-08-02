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

import java.util.List;
import java.util.Map;

/**
 * 鉴定技术状况Entity
 * @author lvchangwei
 * @version 2019-07-04
 */
@Table(name="aa_identify_tec", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="exam_user_id", attrName="examUserId", label="外键Id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="type", attrName="type", label="所属模块"),
		@Column(name="total_deduct", attrName="totalDeduct", label="扣分合计"),
		@Column(name="description", attrName="description", label="缺陷描述"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class IdentifyTec extends PreEntity<IdentifyTec> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键Id
	private String paperId;		// 试卷id
	private String type;		// 所属模块
	// 非数据库字段 - 字典表映射名称 - 对应上述type
	private String typeName;
	private String totalDeduct;		// 扣分合计
	private String description;		// 缺陷描述

	//非数据库字段
	private List<IdentifyTecDetail> itemList;
	//学生或教师已作答的题目
	private Map<String,IdentifyTecDetail> itemMap;
	private ExamDetail examDetail;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public IdentifyTec() {
		this(null);
	}

	public IdentifyTec(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="外键Id长度不能超过 64 个字符")
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
	
	@Length(min=0, max=10, message="所属模块长度不能超过 10 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=16, message="扣分合计长度不能超过 16 个字符")
	public String getTotalDeduct() {
		return totalDeduct;
	}

	public void setTotalDeduct(String totalDeduct) {
		this.totalDeduct = totalDeduct;
	}
	
	@Length(min=0, max=512, message="缺陷描述长度不能超过 512 个字符")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<IdentifyTecDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<IdentifyTecDetail> itemList) {
		this.itemList = itemList;
	}

	public Map<String, IdentifyTecDetail> getItemMap() {
		return itemMap;
	}

	public void setItemMap(Map<String, IdentifyTecDetail> itemMap) {
		this.itemMap = itemMap;
	}

	public ExamDetail getExamDetail() {
		return examDetail;
	}

	public void setExamDetail(ExamDetail examDetail) {
		this.examDetail = examDetail;
	}
}