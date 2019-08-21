/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;


/**
 * common_exam_userEntity
 * @author lvchangwei
 * @version 2019-06-27
 */
@Table(name="common_exam_user", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="user_id", attrName="userId", label="用户id"),
		@Column(name="exam_id", attrName="examId", label="考试id"),
		@Column(name="score", attrName="score", label="分数"),
		@Column(name="start_time", attrName="startTime", label="考试开始时间"),
		@Column(name="end_time", attrName="endTime", label="考试结束时间"),
		@Column(name="server_exam_user_id", attrName="serverExamUserId", label="大平台考生id（上传成绩用）"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class ExamUser extends PreEntity<ExamUser> {
	
	private static final long serialVersionUID = 1L;
	private String userId;		// 用户id
	private String examId;		// 考试id
	private String score;		// 分数
	private Date startTime;		//考试开始时间
	private Date endTime;		//考试结束时间
	private String serverExamUserId;		//大平台考生id（上传成绩用）

	//非数据库字段
    private String userNum;        // 用户名
	private String paperId;		//试卷id
	private String duration;  //考试时长
    //大平台--成绩批量导出
	private String 	trueName;    //真名
	private String schoolName;  //学校名
	private String majorName;   //专业
	private String className;   //班级
	private String gender;      //性别
	private String examName;    //考试名称
	private Boolean isSelect;    //是否选中
	//大平台--教师角色
	private String roleType;
	private String token;        //token


	public ExamUser() {
		this(null);
	}

	public ExamUser(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="用户id长度不能超过 64 个字符")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Length(min=0, max=64, message="考试id长度不能超过 64 个字符")
	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	@Length(min=0, max=32, message="用户名长度不能超过 32 个字符")
	@ExcelField(title="登录名（身份证号）", align=ExcelField.Align.CENTER, sort=1,width = 25*256)
	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}
	@ExcelField(title="成绩", align=ExcelField.Align.CENTER, sort=8,width = 25*256)
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getServerExamUserId() {
		return serverExamUserId;
	}

	public void setServerExamUserId(String serverExamUserId) {
		this.serverExamUserId = serverExamUserId;
	}
	@ExcelField(title="姓名", align=ExcelField.Align.CENTER, sort=2,width = 25*256)
	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	@ExcelField(title="学校", align=ExcelField.Align.CENTER, sort=4,width = 25*256)
	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}
	@ExcelField(title="专业", align=ExcelField.Align.CENTER, sort=5,width = 25*256)
	public String getMajorName() {
		return majorName;
	}

	public void setMajorName(String majorName) {
		this.majorName = majorName;
	}
	@ExcelField(title="班级", align=ExcelField.Align.CENTER, sort=6,width = 25*256)
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	@ExcelField(title="性别", align=ExcelField.Align.CENTER, sort=3,width = 25*256)
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	@ExcelField(title="考试名称", align=ExcelField.Align.CENTER, sort=7,width = 25*256)
	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public Boolean getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Boolean select) {
		isSelect = select;
	}

	public Boolean getSelect() {
		return isSelect;
	}

	public void setSelect(Boolean select) {
		isSelect = select;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}