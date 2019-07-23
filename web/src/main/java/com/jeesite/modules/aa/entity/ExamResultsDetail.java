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
 * 学生成绩详情表Entity
 * @author liangtao
 * @version 2019-07-22
 */
@Table(name="aa_exam_results_detail", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="score_classify_id", attrName="scoreClassifyId", label="外键id"),
		@Column(name="exam_user_id", attrName="examUserId", label="外键id"),
		@Column(name="score_points", attrName="scorePoints", label="评分点"),
		@Column(name="score", attrName="score", label="分值项-分值"),
		@Column(name="student_answer", attrName="studentAnswer", label="学生答案"),
		@Column(name="teacher_answer", attrName="teacherAnswer", label="老师答案"),
		@Column(name="right_or_wrong", attrName="rightOrWrong", label="学生回答是否正确", comment="学生回答是否正确（0正确 1错误 2未答）"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class ExamResultsDetail extends PreEntity<ExamResultsDetail> {
	
	private static final long serialVersionUID = 1L;
	private String scoreClassifyId;		// 外键id
	private String examUserId;		// 外键id
	private String scorePoints;		// 评分点
	private String score;		// 分值项-分值
	private String studentAnswer;		// 学生答案
	private String teacherAnswer;		// 老师答案
	private String rightOrWrong;		// 学生回答是否正确（0正确 1错误 2未答）
	
	public ExamResultsDetail() {
		this(null);
	}

	public ExamResultsDetail(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
	public String getScoreClassifyId() {
		return scoreClassifyId;
	}

	public void setScoreClassifyId(String scoreClassifyId) {
		this.scoreClassifyId = scoreClassifyId;
	}
	
	public String getExamUserId() {
		return examUserId;
	}

	public void setExamUserId(String examUserId) {
		this.examUserId = examUserId;
	}
	
	@Length(min=0, max=64, message="评分点长度不能超过 64 个字符")
	public String getScorePoints() {
		return scorePoints;
	}

	public void setScorePoints(String scorePoints) {
		this.scorePoints = scorePoints;
	}
	
	@Length(min=0, max=10, message="分值项-分值长度不能超过 10 个字符")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@Length(min=0, max=255, message="学生答案长度不能超过 255 个字符")
	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}
	
	@Length(min=0, max=255, message="老师答案长度不能超过 255 个字符")
	public String getTeacherAnswer() {
		return teacherAnswer;
	}

	public void setTeacherAnswer(String teacherAnswer) {
		this.teacherAnswer = teacherAnswer;
	}
	
	@Length(min=0, max=1, message="学生回答是否正确长度不能超过 1 个字符")
	public String getRightOrWrong() {
		return rightOrWrong;
	}

	public void setRightOrWrong(String rightOrWrong) {
		this.rightOrWrong = rightOrWrong;
	}
	
}