/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * 大平台域名设置Entity
 * @author lvchangwei
 * @version 2019-07-24
 */
@Table(name="aa_system_setting", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="ip", attrName="ip", label="ip地址"),
		@Column(name="port", attrName="port", label="端口号"),
		@Column(includeEntity=DataEntity.class),
		@Column(name="context_path", attrName="contextPath", label="项目路径"),
	}, orderBy="a.update_date DESC"
)
public class SystemSetting extends DataEntity<SystemSetting> {
	
	private static final long serialVersionUID = 1L;
	private String ip;		// ip地址
	private String port;		// 端口号
	private String contextPath;		// 项目路径
	
	public SystemSetting() {
		this(null);
	}

	public SystemSetting(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="ip地址长度不能超过 64 个字符")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	@Length(min=0, max=16, message="端口号长度不能超过 16 个字符")
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	@Length(min=0, max=64, message="项目路径长度不能超过 64 个字符")
	public String getContextPath() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
}