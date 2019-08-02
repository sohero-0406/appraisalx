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
 * 委托方信息Entity
 * @author chenlitao
 * @version 2019-06-29
 */
@Table(name="aa_delegate_user", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="exam_user_id", attrName="examUserId", label="外键Id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="name", attrName="name", label="委托方姓名", queryType=QueryType.LIKE),
		@Column(name="id_num", attrName="idNum", label="身份证号"),
		@Column(name="national", attrName="national", label="民族"),
		@Column(name="birthday", attrName="birthday", label="生日"),
		@Column(name="address", attrName="address", label="委托方地址"),
		@Column(name="gender", attrName="gender", label="性别"),
		@Column(name="entrust_num", attrName="entrustNum", label="委托书编号"),
		@Column(name="entrust_day", attrName="entrustDay", label="委托书日期"),
		@Column(name="contact", attrName="contact", label="联系人"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="entrust_type", attrName="entrustType", label="委托书类型"),
		@Column(name="apply_reason", attrName="applyReason", label="申请缘由"),
		@Column(name="complete_date", attrName="completeDate", label="完成日期"),
		@Column(name="appraisal_date", attrName="appraisalDate", label="评报字-时间"),
		@Column(name="appraisal_num", attrName="appraisalNum", label="评报字-数字（8位）"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class DelegateUser extends PreEntity<DelegateUser> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键Id
	private String paperId;		// 试卷id
	private String name;		// 委托方姓名
	private String idNum;		// 身份证号
	private String national;		// 民族
	private String birthday;		// 生日
	private String address;		// 委托方地址
	private String gender;		// 性别
	private String entrustNum;		// 委托书编号
	private String entrustDay;		//委托书日期
	private String contact;		// 联系人
	private String phone;		// 电话
	private String entrustType;		// 委托书类型
	private String applyReason;		// 申请缘由
	private String completeDate;		// 完成日期

	private String appraisalDate;		// 评报字-时间
	private String appraisalNum;		// 评报字-时间
	
	public DelegateUser() {
		this(null);
	}

	public DelegateUser(String id){
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
	
	@Length(min=0, max=32, message="委托方姓名长度不能超过 32 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=32, message="身份证号长度不能超过 32 个字符")
	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	
	@Length(min=0, max=16, message="民族长度不能超过 16 个字符")
	public String getNational() {
		return national;
	}

	public void setNational(String national) {
		this.national = national;
	}
	
	@Length(min=0, max=64, message="生日长度不能超过 64 个字符")
	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	@Length(min=0, max=256, message="委托方地址长度不能超过 256 个字符")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=16, message="性别长度不能超过 16 个字符")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Length(min=0, max=128, message="委托书编号长度不能超过 128 个字符")
	public String getEntrustNum() {
		return entrustNum;
	}

	public void setEntrustNum(String entrustNum) {
		this.entrustNum = entrustNum;
	}

	@Length(min=0, max=32, message="委托书日期长度不能超过 32 个字符")
	public String getEntrustDay() {
		return entrustDay;
	}

	public void setEntrustDay(String entrustDay) {
		this.entrustDay = entrustDay;
	}

	@Length(min=0, max=32, message="联系人长度不能超过 32 个字符")
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	@Length(min=0, max=32, message="电话长度不能超过 32 个字符")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Length(min=0, max=10, message="委托书类型长度不能超过 10 个字符")
	public String getEntrustType() {
		return entrustType;
	}

	public void setEntrustType(String entrustType) {
		this.entrustType = entrustType;
	}
	
	@Length(min=0, max=256, message="申请缘由长度不能超过 256 个字符")
	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	
	@Length(min=0, max=32, message="完成日期长度不能超过 32 个字符")
	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}




	@Length(min=0, max=32, message="评报字-时间长度不能超过 16 个字符")
	public String getAppraisalDate() {
		return appraisalDate;
	}

	public void setAppraisalDate(String appraisalDate) {
		this.appraisalDate = appraisalDate;
	}

	@Length(min=0, max=10, message="评报字-数字（8位）长度不能超过 8 个字符")
	public String getAppraisalNum() {
		return appraisalNum;
	}

	public void setAppraisalNum(String appraisalNum) {
		this.appraisalNum = appraisalNum;
	}
	
}