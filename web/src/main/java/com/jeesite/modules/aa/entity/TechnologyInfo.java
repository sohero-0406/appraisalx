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
 * 技术状况项目表Entity
 * @author lvchangwei
 * @version 2019-07-04
 */
@Table(name="aa_technology_info", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="name", attrName="name", label="项目名称", queryType=QueryType.LIKE),
		@Column(name="type", attrName="type", label="所属分类"),
		@Column(name="sort", attrName="sort", label="排序"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class TechnologyInfo extends PreEntity<TechnologyInfo> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 项目名称
	private String type;		// 所属分类
	private Integer sort;		// 排序

	//非数据库字段
	private List<IdentifyTecDetail> itemList;
	
	public TechnologyInfo() {
		this(null);
	}

	public TechnologyInfo(String id){
		super(id);
	}
	
	@Length(min=0, max=256, message="项目名称长度不能超过 256 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=10, message="所属分类长度不能超过 10 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public List<IdentifyTecDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<IdentifyTecDetail> itemList) {
		this.itemList = itemList;
	}
}