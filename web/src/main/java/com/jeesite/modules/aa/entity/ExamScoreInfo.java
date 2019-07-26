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

/**
 * 考试评分点Entity
 * @author liangtao
 * @version 2019-07-16
 */
@Table(name="aa_exam_score_info", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="name", attrName="name", label="评分点", comment="评分点(项目名称)", queryType=QueryType.LIKE),
		@Column(name="parent_id", attrName="parentId", label="所属分类"),
		@Column(name="sort", attrName="sort", label="排序"),
		@Column(name="score", attrName="score", label="默认分数"),
		@Column(includeEntity=DataEntity.class),
}, orderBy="a.update_date DESC"
)
public class ExamScoreInfo extends PreEntity<ExamScoreInfo> {

	private static final long serialVersionUID = 1L;
	private String name;		// 评分点(项目名称)
	private String parentId;		// 所属分类
	private Integer sort;		// 排序
	private String score;    //默认分数

	private List<ExamScoreInfo> itemList;



	public ExamScoreInfo() {
		this(null);
	}

	public ExamScoreInfo(String id){
		super(id);
	}

	@Length(min=0, max=256, message="评分点长度不能超过 256 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=64, message="所属分类长度不能超过 64 个字符")
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@Length(min=0, max=10, message="所属分类长度不能超过 10 个字符")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public List<ExamScoreInfo> getItemList() {
		return itemList;
	}

	public void setItemList(List<ExamScoreInfo> itemList) {
		this.itemList = itemList;
	}
}