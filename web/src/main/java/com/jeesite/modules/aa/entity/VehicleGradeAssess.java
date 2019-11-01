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

import javax.validation.constraints.NotBlank;

/**
 * 车辆等级评定Entity
 * @author lvchangwei
 * @version 2019-07-09
 */
@Table(name="aa_vehicle_grade_assess", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="exam_user_id", attrName="examUserId", label="外键Id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="score", attrName="score", label="综合状况分值"),
		@Column(name="technical_status", attrName="technicalStatus", label="技术状况"),
		@Column(name="evaluator", attrName="evaluator", label="评估师"),
		@Column(name="description", attrName="description", label="综合评论"),
		@Column(name="start_score", attrName="startScore", label="得分区间开始值"),
		@Column(name="end_score", attrName="endScore", label="得分区间结束值"),
		@Column(name="identify_date", attrName="identifyDate", label="鉴定日期"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class VehicleGradeAssess extends PreEntity<VehicleGradeAssess> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键Id
	private String paperId;		// 试卷id
    @NotBlank
    private String score;		// 综合状况分值
    @NotBlank
    private String technicalStatus;		// 技术状况
	private String evaluator;		// 评估师
	@NotBlank
	private String description;		// 综合评论
	private String startScore;		// 得分区间开始值
	private String endScore;		// 得分区间结束值
    @NotBlank
    private String identifyDate;		//鉴定日期
	
	public VehicleGradeAssess() {
		this(null);
	}

	public VehicleGradeAssess(String id){
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
	
	@Length(min=0, max=16, message="综合状况分值长度不能超过 16 个字符")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@Length(min=0, max=10, message="技术状况长度不能超过 10 个字符")
	public String getTechnicalStatus() {
		return technicalStatus;
	}

	public void setTechnicalStatus(String technicalStatus) {
		this.technicalStatus = technicalStatus;
	}
	
	@Length(min=0, max=64, message="评估师长度不能超过 64 个字符")
	public String getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(String evaluator) {
		this.evaluator = evaluator;
	}
	
	@Length(min=0, max=1024, message="综合评论长度不能超过 1024 个字符")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Length(min=0, max=16, message="得分区间开始值长度不能超过 16 个字符")
	public String getStartScore() {
		return startScore;
	}

	public void setStartScore(String startScore) {
		this.startScore = startScore;
	}
	
	@Length(min=0, max=16, message="得分区间结束值长度不能超过 16 个字符")
	public String getEndScore() {
		return endScore;
	}

	public void setEndScore(String endScore) {
		this.endScore = endScore;
	}

	@Length(min=0, max=32, message="鉴定日期长度不能超过 32 个字符")
	public String getIdentifyDate() {
		return identifyDate;
	}

	public void setIdentifyDate(String identifyDate) {
		this.identifyDate = identifyDate;
	}
}