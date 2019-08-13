/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.entity;

import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;

/**
 * common_maintenance_typeEntity
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Table(name="common_maintenance_type", alias="a", columns={
		@Column(name="id", attrName="id", label="id", isPK=true),
		@Column(name="maintenance_id", attrName="maintenanceId", label="维保记录主键"),
		@Column(name="repair_date", attrName="repairDate", label="repair_date"),
		@Column(name="mileage", attrName="mileage", label="mileage"),
		@Column(name="main_tain_date", attrName="mainTainDate", label="main_tain_date"),
		@Column(name="type", attrName="type", label="type"),
		@Column(name="content", attrName="content", label="维修详细内容"),
		@Column(name="maintenance_type", attrName="maintenanceType", label="外观覆盖件详细维修记录,重要组成部件详细维修记录,该车所有的详细维修记录"),
		@Column(name="material", attrName="material", label="维修材料"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class MaintenanceType extends PreEntity<MaintenanceType> {
	
	private static final long serialVersionUID = 1L;
	private String maintenanceId;		// 维保记录主键
	private String repairDate;		// repair_date
	private String mileage;		// mileage
	private String mainTainDate;		// main_tain_date
	private String type;		// type
	private String content;		// 维修详细内容
	private String maintenanceType;		// 外观覆盖件详细维修记录,重要组成部件详细维修记录,该车所有的详细维修记录
	private String material;		// 维修材料
	
	public MaintenanceType() {
		this(null);
	}

	public MaintenanceType(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="维保记录主键长度不能超过 64 个字符")
	public String getMaintenanceId() {
		return maintenanceId;
	}

	public void setMaintenanceId(String maintenanceId) {
		this.maintenanceId = maintenanceId;
	}
	
	@Length(min=0, max=32, message="repair_date长度不能超过 32 个字符")
	public String getRepairDate() {
		return repairDate;
	}

	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
	}
	
	@Length(min=0, max=128, message="mileage长度不能超过 128 个字符")
	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
	@Length(min=0, max=32, message="main_tain_date长度不能超过 32 个字符")
	public String getMainTainDate() {
		return mainTainDate;
	}

	public void setMainTainDate(String mainTainDate) {
		this.mainTainDate = mainTainDate;
	}
	
	@Length(min=0, max=16, message="type长度不能超过 16 个字符")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=2048, message="维修详细内容长度不能超过 2048 个字符")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=64, message="外观覆盖件详细维修记录,重要组成部件详细维修记录,该车所有的详细维修记录长度不能超过 64 个字符")
	public String getMaintenanceType() {
		return maintenanceType;
	}

	public void setMaintenanceType(String maintenanceType) {
		this.maintenanceType = maintenanceType;
	}
	
	@Length(min=0, max=255, message="维修材料长度不能超过 255 个字符")
	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}
	
}