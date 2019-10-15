/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.entity;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.jeesite.common.mybatis.annotation.JoinTable;
import com.jeesite.common.mybatis.annotation.JoinTable.Type;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * common_examEntity
 * @author lvchangwei
 * @version 2019-07-10
 */
@Table(name="common_exam", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="score_id", attrName="scoreId", label="评分表id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="name", attrName="name", label="考试名称", queryType=QueryType.LIKE),
		@Column(name="duration", attrName="duration", label="考试时长", comment="考试时长(分钟)"),
		@Column(name="start_time", attrName="startTime", label="开始时间"),
		@Column(name="end_time", attrName="endTime", label="结束时间"),
		@Column(name = "state", attrName = "state", label = "状态（1:未开始;3:考试中;5:未统计;6:统计成绩中;7:已出分）"),
		@Column(name = "upload_score", attrName = "uploadScore", label = "上传成绩（0-未上传 1-已上传）"),
		@Column(name="type", attrName="type", label="考试类型"),
		@Column(name="exam_type", attrName="examType", label="考试计时", comment="考试计时（1、倒计时 2、倒计时）"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class Exam extends PreEntity<Exam> {
	
	private static final long serialVersionUID = 1L;
	private String scoreId;		// 评分表id
	private String paperId;		// 试卷id
	private String name;		// 考试名称
	private Integer duration;		// 考试时长(分钟)
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String state;        // 状态（1:未开始;3:考试中;5:未统计;6:统计成绩中;7:已出分）
	private String uploadScore;        //（0-未上传 1-已上传）
	private String type;		// 考试类型
	private String examType;		// 考试计时（1、倒计时 2、倒计时）
	private String paperName; //考试名称

	private String createTime; //创建时间

	public Exam() {
		this(null);
	}

	public Exam(String id){
		super(id);
	}

    @Length(min=0, max=64, message="评分表id长度不能超过 64 个字符")
	public String getScoreId() {
		return scoreId;
	}

	public void setScoreId(String scoreId) {
		this.scoreId = scoreId;
	}
	
	@Length(min=0, max=64, message="试卷id长度不能超过 64 个字符")
	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	
	@Length(min=0, max=32, message="考试名称长度不能超过 32 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=10, message="状态长度不能超过 10 个字符")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Length(min = 0, max = 10, message = "上传成绩长度不能超过 10 个字符")
	public String getUploadScore() {
		return uploadScore;
	}

	public void setUploadScore(String uploadScore) {
		this.uploadScore = uploadScore;
	}

	@Length(min=0, max=10, message="考试类型长度不能超过 10 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPaperName() {
		return paperName;
	}

	public void setPaperName(String paperName) {
		this.paperName = paperName;
	}

	@Length(min=0, max=10, message="考试计时长度不能超过 10 个字符")
	public String getExamType() {
		return examType;
	}

	public void setExamType(String examType) {
		this.examType = examType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}