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
 * 考试评分详情表Entity
 * @author liangtao
 * @version 2019-07-16
 */
@Table(name="aa_exam_score_detail", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="score_classify_id", attrName="scoreClassifyId", label="考试评分id"),
		@Column(name="score_info_id", attrName="scoreInfoId", label="考试评分项id"),
		@Column(name="score", attrName="score", label="分值"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class ExamScoreDetail extends PreEntity<ExamScoreDetail> {
	
	private static final long serialVersionUID = 1L;
	private String scoreClassifyId;		// 考试评分id
	private String scoreInfoId;		// 考试评分项id
	private String score;		// 分值
	private String infoName;

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExamScoreDetail() {
		this(null);
	}

	public ExamScoreDetail(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="考试评分id长度不能超过 64 个字符")
	public String getScoreClassifyId() {
		return scoreClassifyId;
	}

	public void setScoreClassifyId(String scoreClassifyId) {
		this.scoreClassifyId = scoreClassifyId;
	}
	
	@Length(min=0, max=64, message="考试评分项id长度不能超过 64 个字符")
	public String getScoreInfoId() {
		return scoreInfoId;
	}

	public void setScoreInfoId(String scoreInfoId) {
		this.scoreInfoId = scoreInfoId;
	}
	
	@Length(min=0, max=16, message="分值长度不能超过 16 个字符")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}
	
}