/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

import java.util.List;

/**
 * 考试评分分类总分数Entity
 * @author liangtao
 * @version 2019-07-16
 */
@Table(name="aa_exam_score_classify", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="exam_id", attrName="examId", label="考试评分表Id"),
		@Column(name="exam_score_info_id", attrName="examScoreInfoId", label="外键id"),
		@Column(name="score", attrName="score", label="考试评分分类总分值"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class ExamScoreClassify extends PreEntity<ExamScoreClassify> {
	
	private static final long serialVersionUID = 1L;
	private String examId;		// 考试评分表Id
	private String examScoreInfoId;		// 外键id
	private String score;		// 考试评分分类总分值exam_score_info_id

 	//非数据库字段
	private List<ExamScoreDetail> itemList;
	private List<ExamResultsDetail> examResultsDetailList;
	private String name;
	private String totalScore;

	public String getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	public ExamScoreClassify() {
		this(null);
	}

	public ExamScoreClassify(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="考试评分表Id长度不能超过 64 个字符")
	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getExamScoreInfoId() {
		return examScoreInfoId;
	}

	public void setExamScoreInfoId(String examScoreInfoId) {
		this.examScoreInfoId = examScoreInfoId;
	}
	
	@Length(min=0, max=16, message="考试评分分类总分值长度不能超过 16 个字符")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public List<ExamScoreDetail> getItemList() {
		return itemList;
	}

	public void setItemList(List<ExamScoreDetail> itemList) {
		this.itemList = itemList;
	}

	public List<ExamResultsDetail> getExamResultsDetailList() {
		return examResultsDetailList;
	}

	public void setExamResultsDetailList(List<ExamResultsDetail> examResultsDetailList) {
		this.examResultsDetailList = examResultsDetailList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}