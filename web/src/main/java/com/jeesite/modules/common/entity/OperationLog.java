/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.entity;

import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.fieldtype.CompanyType;
import com.jeesite.common.utils.excel.fieldtype.OfficeType;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

import javax.validation.Valid;

/**
 * 操作日志Entity
 * @author liangtao
 * @version 2019-07-29
 */
@Table(name="common_operation_log", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="login_name", attrName="loginName", label="登录名", queryType=QueryType.LIKE),
		@Column(name="request_ip", attrName="requestIp", label="请求ip"),
		@Column(name="operating_content", attrName="operatingContent", label="操作内容"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class OperationLog extends PreEntity<OperationLog> {
	
	private static final long serialVersionUID = 1L;
	private String loginName;		// 登录名
	private String requestIp;		// 请求ip
	private String operatingContent;		// 操作内容
	private String operatingTime;
	
	public OperationLog() {
		this(null);
	}

	public OperationLog(String id){
		super(id);
	}
	
	@Length(min=0, max=32, message="登录名长度不能超过 32 个字符")
	@ExcelField(title="登录名", align=ExcelField.Align.CENTER, sort=1)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Length(min=0, max=64, message="请求ip长度不能超过 64 个字符")
    @ExcelField(title="请求IP长度", align=ExcelField.Align.CENTER, sort=1)
	public String getRequestIp() {
		return requestIp;
	}

	public void setRequestIp(String requestIp) {
		this.requestIp = requestIp;
	}
	
	@Length(min=0, max=255, message="操作内容长度不能超过 255 个字符")
    @ExcelField(title="操作内容", align=ExcelField.Align.CENTER, sort=1)
	public String getOperatingContent() {
		return operatingContent;
	}

	public void setOperatingContent(String operatingContent) {
		this.operatingContent = operatingContent;
	}

	@ExcelField(title="操作时间", align=ExcelField.Align.CENTER, sort=1)
	public String getOperatingTime() {
		return operatingTime;
	}

	public void setOperatingTime(String operatingTime) {
		this.operatingTime = operatingTime;
	}

}