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
 * 车辆维保记录Entity
 * @author jiangyanfei
 * @version 2019-08-05
 */
@Table(name="common_maintenance", alias="a", columns={
		@Column(name="id", attrName="id", label="主键，车辆维保记录表", isPK=true),
		@Column(name="result_code", attrName="resultCode", label="二级编码"),
		@Column(name="result_msg", attrName="resultMsg", label="二级编码描述"),
		@Column(name="brand", attrName="brand", label="品牌"),
		@Column(name="maintenance_total_id", attrName="maintenanceTotalId", label="维保总表记录主键"),
		@Column(name="model_name", attrName="modelName", label="车型", queryType=QueryType.LIKE),
		@Column(name="series_name", attrName="seriesName", label="车系", queryType=QueryType.LIKE),
		@Column(name="vin", attrName="vin", label="vin码"),
		@Column(name="manufacturer", attrName="manufacturer", label="生产厂商"),
		@Column(name="service_type", attrName="serviceType", label="维保类型", comment="维保类型(维修、保养)"),
		@Column(name="make_date", attrName="makeDate", label="生产年份"),
		@Column(name="service_date", attrName="serviceDate", label="维保时间"),
		@Column(name="transmission_type", attrName="transmissionType", label="变速箱类型"),
		@Column(name="displacement", attrName="displacement", label="排量"),
		@Column(name="effluent_standard", attrName="effluentStandard", label="排放标准"),
		@Column(name="car_fire_flag", attrName="carFireFlag", label="是否火烧，否，是"),
		@Column(name="car_water_flag", attrName="carWaterFlag", label="是否水泡"),
		@Column(name="car_component_records_flag", attrName="carComponentRecordsFlag", label="重要组成部件是否有维修"),
		@Column(name="car_construct_records_flag", attrName="carConstructRecordsFlag", label="结构件是否有维修"),
		@Column(name="car_outside_records_flag", attrName="carOutsideRecordsFlag", label="外观覆盖件是否有维修，否；是"),
		@Column(name="mileage_is_normal_flag", attrName="mileageIsNormalFlag", label="公里数是否正常"),
		@Column(name="mileage_estimate", attrName="mileageEstimate", label="预估公里数，0表示为估出"),
		@Column(name="last_main_tain_time", attrName="lastMainTainTime", label="最后一次保养时间"),
		@Column(name="main_tain_times", attrName="mainTainTimes", label="每年保养次数"),
		@Column(name="last_repair_time", attrName="lastRepairTime", label="最后一次维修时间"),
		@Column(name="mileage_every_year", attrName="mileageEveryYear", label="每年行驶公里数"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class Maintenance extends PreEntity<Maintenance> {
	
	private static final long serialVersionUID = 1L;
	private String resultCode;		// 二级编码
	private String resultMsg;		// 二级编码描述
	private String brand;		// 品牌
	private String maintenanceTotalId;		// 维保总表记录主键
	private String modelName;		// 车型
	private String seriesName;		// 车系
	private String vin;		// vin码
	private String manufacturer;		// 生产厂商
	private String serviceType;		// 维保类型(维修、保养)
	private String makeDate;		// 生产年份
	private String serviceDate;		// 维保时间
	private String transmissionType;		// 变速箱类型
	private String displacement;		// 排量
	private String effluentStandard;		// 排放标准
	private String carFireFlag;		// 是否火烧，否，是
	private String carWaterFlag;		// 是否水泡
	private String carComponentRecordsFlag;		// 重要组成部件是否有维修
	private String carConstructRecordsFlag;		// 结构件是否有维修
	private String carOutsideRecordsFlag;		// 外观覆盖件是否有维修，否；是
	private String mileageIsNormalFlag;		// 公里数是否正常
	private String mileageEstimate;		// 预估公里数，0表示为估出
	private String lastMainTainTime;		// 最后一次保养时间
	private String mainTainTimes;		// 每年保养次数
	private String lastRepairTime;		// 最后一次维修时间
	private String mileageEveryYear;		// 每年行驶公里数
	
	public Maintenance() {
		this(null);
	}

	public Maintenance(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="二级编码长度不能超过 64 个字符")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	@Length(min=0, max=128, message="二级编码描述长度不能超过 128 个字符")
	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
	
	@Length(min=0, max=32, message="品牌长度不能超过 32 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=64, message="维保总表记录主键长度不能超过 64 个字符")
	public String getMaintenanceTotalId() {
		return maintenanceTotalId;
	}

	public void setMaintenanceTotalId(String maintenanceTotalId) {
		this.maintenanceTotalId = maintenanceTotalId;
	}
	
	@Length(min=0, max=64, message="车型长度不能超过 64 个字符")
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	@Length(min=0, max=64, message="车系长度不能超过 64 个字符")
	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
	
	@Length(min=0, max=64, message="vin码长度不能超过 64 个字符")
	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}
	
	@Length(min=0, max=64, message="生产厂商长度不能超过 64 个字符")
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	
	@Length(min=0, max=64, message="维保类型长度不能超过 64 个字符")
	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	@Length(min=0, max=32, message="生产年份长度不能超过 32 个字符")
	public String getMakeDate() {
		return makeDate;
	}

	public void setMakeDate(String makeDate) {
		this.makeDate = makeDate;
	}
	
	@Length(min=0, max=32, message="维保时间长度不能超过 32 个字符")
	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}
	
	@Length(min=0, max=64, message="变速箱类型长度不能超过 64 个字符")
	public String getTransmissionType() {
		return transmissionType;
	}

	public void setTransmissionType(String transmissionType) {
		this.transmissionType = transmissionType;
	}
	
	@Length(min=0, max=16, message="排量长度不能超过 16 个字符")
	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	
	@Length(min=0, max=16, message="排放标准长度不能超过 16 个字符")
	public String getEffluentStandard() {
		return effluentStandard;
	}

	public void setEffluentStandard(String effluentStandard) {
		this.effluentStandard = effluentStandard;
	}
	
	@Length(min=0, max=16, message="是否火烧，否，是长度不能超过 16 个字符")
	public String getCarFireFlag() {
		return carFireFlag;
	}

	public void setCarFireFlag(String carFireFlag) {
		this.carFireFlag = carFireFlag;
	}
	
	@Length(min=0, max=16, message="是否水泡长度不能超过 16 个字符")
	public String getCarWaterFlag() {
		return carWaterFlag;
	}

	public void setCarWaterFlag(String carWaterFlag) {
		this.carWaterFlag = carWaterFlag;
	}
	
	@Length(min=0, max=16, message="重要组成部件是否有维修长度不能超过 16 个字符")
	public String getCarComponentRecordsFlag() {
		return carComponentRecordsFlag;
	}

	public void setCarComponentRecordsFlag(String carComponentRecordsFlag) {
		this.carComponentRecordsFlag = carComponentRecordsFlag;
	}
	
	@Length(min=0, max=16, message="结构件是否有维修长度不能超过 16 个字符")
	public String getCarConstructRecordsFlag() {
		return carConstructRecordsFlag;
	}

	public void setCarConstructRecordsFlag(String carConstructRecordsFlag) {
		this.carConstructRecordsFlag = carConstructRecordsFlag;
	}
	
	@Length(min=0, max=16, message="外观覆盖件是否有维修，否；是长度不能超过 16 个字符")
	public String getCarOutsideRecordsFlag() {
		return carOutsideRecordsFlag;
	}

	public void setCarOutsideRecordsFlag(String carOutsideRecordsFlag) {
		this.carOutsideRecordsFlag = carOutsideRecordsFlag;
	}
	
	@Length(min=0, max=16, message="公里数是否正常长度不能超过 16 个字符")
	public String getMileageIsNormalFlag() {
		return mileageIsNormalFlag;
	}

	public void setMileageIsNormalFlag(String mileageIsNormalFlag) {
		this.mileageIsNormalFlag = mileageIsNormalFlag;
	}
	
	@Length(min=0, max=16, message="预估公里数，0表示为估出长度不能超过 16 个字符")
	public String getMileageEstimate() {
		return mileageEstimate;
	}

	public void setMileageEstimate(String mileageEstimate) {
		this.mileageEstimate = mileageEstimate;
	}
	
	@Length(min=0, max=32, message="最后一次保养时间长度不能超过 32 个字符")
	public String getLastMainTainTime() {
		return lastMainTainTime;
	}

	public void setLastMainTainTime(String lastMainTainTime) {
		this.lastMainTainTime = lastMainTainTime;
	}
	
	@Length(min=0, max=10, message="每年保养次数长度不能超过 10 个字符")
	public String getMainTainTimes() {
		return mainTainTimes;
	}

	public void setMainTainTimes(String mainTainTimes) {
		this.mainTainTimes = mainTainTimes;
	}
	
	@Length(min=0, max=32, message="最后一次维修时间长度不能超过 32 个字符")
	public String getLastRepairTime() {
		return lastRepairTime;
	}

	public void setLastRepairTime(String lastRepairTime) {
		this.lastRepairTime = lastRepairTime;
	}
	
	@Length(min=0, max=16, message="每年行驶公里数长度不能超过 16 个字符")
	public String getMileageEveryYear() {
		return mileageEveryYear;
	}

	public void setMileageEveryYear(String mileageEveryYear) {
		this.mileageEveryYear = mileageEveryYear;
	}
	
}