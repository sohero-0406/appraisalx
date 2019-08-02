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
 * 委托书信息Entity
 * @author lvchangwei
 * @version 2019-07-18
 */
@Table(name="aa_delegate_letter", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="organization_name", attrName="organizationName", label="鉴定评估机构名称", queryType=QueryType.LIKE),
		@Column(name="id_num", attrName="idNum", label="法人代码证"),
		@Column(name="organization_address", attrName="organizationAddress", label="鉴定评估机构地址"),
		@Column(name="contact", attrName="contact", label="联系人"),
		@Column(name="phone", attrName="phone", label="电话"),
		@Column(name="check_name", attrName="checkName", label="复核人姓名", queryType=QueryType.LIKE),
		@Column(name="name", attrName="name", label="受托方姓名", queryType=QueryType.LIKE),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class DelegateLetter extends PreEntity<DelegateLetter> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键id
	private String paperId;		// 试卷id
	private String organizationName;		// 鉴定评估机构名称
	private String idNum;
	private String organizationAddress;		// 鉴定评估机构地址
	private String contact;		// 联系人
	private String phone;		// 电话
	private String name;		// 受托方姓名

	private String checkName;		// 复核人姓名
	
	public DelegateLetter() {
		this(null);
	}

	public DelegateLetter(String id){
		super(id);
	}

	@Length(min=0, max=64, message="试卷id长度不能超过 64 个字符")
	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	@Length(min=0, max=128, message="鉴定评估机构名称长度不能超过 128 个字符")
	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	@Length(min=0, max=32, message="鉴定评估机构地址长度不能超过 32 个字符")
	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	@Length(min=0, max=256, message="鉴定评估机构地址长度不能超过 256 个字符")
	public String getOrganizationAddress() {
		return organizationAddress;
	}

	public void setOrganizationAddress(String organizationAddress) {
		this.organizationAddress = organizationAddress;
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
	
	@Length(min=0, max=32, message="受托方姓名长度不能超过 32 个字符")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=32, message="复核人姓名长度不能超过 32 个字符")
	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	
}