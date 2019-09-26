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
 * 委托车辆信息Entity
 * @author chenlitao
 * @version 2019-06-29
 */
@Table(name="aa_car_info", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="exam_user_id", attrName="examUserId", label="外键id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="license_plate_num", attrName="licensePlateNum", label="车牌号"),
		@Column(name="vin_code", attrName="vinCode", label="vin码"),
		@Column(name="brand", attrName="brand", label="品牌"),
		@Column(name="series", attrName="series", label="车系"),
		@Column(name="model", attrName="model", label="车型"),
		@Column(name="mileage", attrName="mileage", label="行驶里程", comment="行驶里程(公里)"),
		@Column(name="vehicle_type", attrName="vehicleType", label="车辆类型"),
		@Column(name="label_type", attrName="labelType", label="厂牌型号"),
		@Column(name="usage", attrName="usage", label="使用用途"),
		@Column(name="total_quality", attrName="totalQuality", label="总质量", comment="总质量（kg）"),
		@Column(name="sear", attrName="sear", label="座位"),
		@Column(name="displacement", attrName="displacement", label="排量", comment="排量（ml）"),
		@Column(name="fuel_type", attrName="fuelType", label="燃油种类"),
		@Column(name="register_date", attrName="registerDate", label="初次登记日期"),
		@Column(name="color", attrName="color", label="车身颜色"),
		@Column(name="use_year", attrName="useYear", label="已使用年限-年"),
		@Column(name="use_month", attrName="useMonth", label="已使用年限-月"),
		@Column(name="engine_overhaul_times", attrName="engineOverhaulTimes", label="发动机大修次数"),
		@Column(name="car_overhaul_times", attrName="carOverhaulTimes", label="整车大修次数"),
		@Column(name="maintenance_situation", attrName="maintenanceSituation", label="维修情况"),
		@Column(name="accident", attrName="accident", label="事故情况"),
		@Column(name="purchase_date", attrName="purchaseDate", label="购置日期"),
		@Column(name="original_price", attrName="originalPrice", label="原始价格"),
		@Column(name="note", attrName="note", label="备注"),
		@Column(name="level", attrName="level", label="级别"),
		@Column(name="mode_product", attrName="modeProduct", label="生产方式"),
		@Column(name="manufacture_date", attrName="manufactureDate", label="出厂日期"),
		@Column(name="change_num", attrName="changeNum", label="过户次数"),
		@Column(name="environmental_standard", attrName="environmentalStandard", label="环保标准"),
		@Column(name="body_structure", attrName="bodyStructure", label="车身结构"),
		@Column(name="curb_weight", attrName="curbWeight", label="整备质量"),
		@Column(name="engine_model", attrName="engineModel", label="发动机型号"),
		@Column(name="engine_num", attrName="engineNum", label="发动机号码"),
		@Column(name="body_size", attrName="bodySize", label="车身尺寸"),
		@Column(name="year_check_due", attrName="yearCheckDue", label="年检到期"),
		@Column(name="insurance_due", attrName="insuranceDue", label="保险到期"),
		@Column(name="vehicle_warranty", attrName="vehicleWarranty", label="整车质保"),
		@Column(name="engine_power", attrName="enginePower", label="发动机功率"),
		@Column(name="vehicle_brand", attrName="vehicleBrand", label="品牌名称"),
		@Column(name="brand_name", attrName="brandName", label="发动机功率"),
		@Column(name="model_name", attrName="modelName", label="品牌名称"),
		@Column(name="fa_dong_ji_xing_hao", attrName="fadongjixinghao", label="发动机型号"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class CarInfo extends PreEntity<CarInfo> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键id
	private String paperId;		// 试卷id
	private String licensePlateNum;		// 车牌号
	private String vinCode;		// vin码
	private String brand;		// 品牌
	private String series;		// 车系
	private String model;		// 车型
	private String mileage;		// 行驶里程(公里)
	private String vehicleType;		// 车辆类型
	private String labelType;		// 厂牌型号
	private String usage;		// 使用用途
	private String totalQuality;		// 总质量（kg）
	private String sear;		// 座位
	private String displacement;		// 排量（ml）
	private String fuelType;		// 燃油种类
	private String registerDate;		// 初次登记日期
	private String color;		// 车身颜色
	private Integer useYear;		// 已使用年限-年
	private Integer useMonth;		// 已使用年限-月
	private String engineOverhaulTimes;		// 发动机大修次数
	private String carOverhaulTimes;		// 整车大修次数
	private String maintenanceSituation;		// 维修情况
	private String accident;		// 事故情况
	private String purchaseDate;		// 购置日期
	private String originalPrice;		// 原始价格
	private String note;		// 备注
	private String level;		// 级别
	private String modeProduct;		//生产方式
	private String manufactureDate;		// 出厂日期
	private Integer changeNum;		// 过户次数
	private String environmentalStandard;		// 环保标准
	private String bodyStructure;		// 车身结构
	private String curbWeight;		// 整备质量
	private String engineModel;		// 发动机型号
	private String engineNum;		// 发动机号码
	private String bodySize;		// 车身尺寸
	private String yearCheckDue;		// 年检到期
	private String insuranceDue;		// 保险到期
	private String vehicleWarranty;		// 整车质保
	private String enginePower;         // 发动机功率
	private String vehicleBrand;        //品牌名称


	private String brandName;
	private String modelName;//年款型号
	private String fadongjixinghao;



	//非数据库字段
	private  String evaluator;

	public String getFadongjixinghao() {
		return fadongjixinghao;
	}

	public void setFadongjixinghao(String fadongjixinghao) {
		this.fadongjixinghao = fadongjixinghao;
	}

	public String getEvaluator() {
		return evaluator;
	}

	public void setEvaluator(String evaluator) {
		this.evaluator = evaluator;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public CarInfo() {
		this(null);
	}

	public CarInfo(String id){
		super(id);
	}
	
	@Length(min=0, max=64, message="外键id长度不能超过 64 个字符")
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

    @Length(min=0, max=32, message="车牌号长度不能超过 32 个字符")
	public String getLicensePlateNum() {
		return licensePlateNum;
	}

	public void setLicensePlateNum(String licensePlateNum) {
		this.licensePlateNum = licensePlateNum;
	}
	
	@Length(min=0, max=64, message="vin码长度不能超过 64 个字符")
	public String getVinCode() {
		return vinCode;
	}

	public void setVinCode(String vinCode) {
		this.vinCode = vinCode;
	}
	
	@Length(min=0, max=64, message="品牌长度不能超过 64 个字符")
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=64, message="车系长度不能超过 64 个字符")
	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}
	
	@Length(min=0, max=64, message="车型长度不能超过 64 个字符")
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	@Length(min=0, max=16, message="行驶里程长度不能超过 16 个字符")
	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	
	@Length(min=0, max=32, message="车辆类型长度不能超过 32 个字符")
	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	
	@Length(min=0, max=32, message="厂牌型号长度不能超过 32 个字符")
	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}
	
	@Length(min=0, max=10, message="使用用途长度不能超过 10 个字符")
	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	@Length(min=0, max=10, message="总质量长度不能超过 10 个字符")
	public String getTotalQuality() {
		return totalQuality;
	}

	public void setTotalQuality(String totalQuality) {
		this.totalQuality = totalQuality;
	}
	
	@Length(min=0, max=10, message="座位长度不能超过 10 个字符")
	public String getSear() {
		return sear;
	}

	public void setSear(String sear) {
		this.sear = sear;
	}
	
	@Length(min=0, max=16, message="排量长度不能超过 16 个字符")
	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	
	@Length(min=0, max=10, message="燃油种类长度不能超过 10 个字符")
	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	
	@Length(min=0, max=16, message="初次登记日期长度不能超过 16 个字符")
	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	
	@Length(min=0, max=10, message="车身颜色长度不能超过 10 个字符")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public Integer getUseYear() {
		return useYear;
	}

	public void setUseYear(Integer useYear) {
		this.useYear = useYear;
	}

	public Integer getUseMonth() {
		return useMonth;
	}

	public void setUseMonth(Integer useMonth) {
		this.useMonth = useMonth;
	}

	@Length(min=0, max=10, message="发动机大修次数长度不能超过 10 个字符")
	public String getEngineOverhaulTimes() {
		return engineOverhaulTimes;
	}

	public void setEngineOverhaulTimes(String engineOverhaulTimes) {
		this.engineOverhaulTimes = engineOverhaulTimes;
	}
	
	@Length(min=0, max=10, message="整车大修次数长度不能超过 10 个字符")
	public String getCarOverhaulTimes() {
		return carOverhaulTimes;
	}

	public void setCarOverhaulTimes(String carOverhaulTimes) {
		this.carOverhaulTimes = carOverhaulTimes;
	}
	
	@Length(min=0, max=256, message="维修情况长度不能超过 256 个字符")
	public String getMaintenanceSituation() {
		return maintenanceSituation;
	}

	public void setMaintenanceSituation(String maintenanceSituation) {
		this.maintenanceSituation = maintenanceSituation;
	}
	
	@Length(min=0, max=1024, message="事故情况长度不能超过 1024 个字符")
	public String getAccident() {
		return accident;
	}

	public void setAccident(String accident) {
		this.accident = accident;
	}
	
	@Length(min=0, max=16, message="购置日期长度不能超过 16 个字符")
	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	@Length(min=0, max=16, message="原始价格长度不能超过 16 个字符")
	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}
	
	@Length(min=0, max=1024, message="备注长度不能超过 1024 个字符")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	@Length(min=0, max=32, message="级别长度不能超过 32 个字符")
	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Length(min=0, max=10, message="生产方式不能超过 10 个字符")
	public String getModeProduct() {
		return modeProduct;
	}

	public void setModeProduct(String modeProduct) {
		this.modeProduct = modeProduct;
	}

	@Length(min=0, max=64, message="出厂日期长度不能超过 64 个字符")
	public String getManufactureDate() {
		return manufactureDate;
	}

	public void setManufactureDate(String manufactureDate) {
		this.manufactureDate = manufactureDate;
	}
	
	public Integer getChangeNum() {
		return changeNum;
	}

	public void setChangeNum(Integer changeNum) {
		this.changeNum = changeNum;
	}
	
	@Length(min=0, max=32, message="环保标准长度不能超过 32 个字符")
	public String getEnvironmentalStandard() {
		return environmentalStandard;
	}

	public void setEnvironmentalStandard(String environmentalStandard) {
		this.environmentalStandard = environmentalStandard;
	}
	
	@Length(min=0, max=64, message="车身结构长度不能超过 64 个字符")
	public String getBodyStructure() {
		return bodyStructure;
	}

	public void setBodyStructure(String bodyStructure) {
		this.bodyStructure = bodyStructure;
	}
	
	@Length(min=0, max=32, message="整备质量长度不能超过 32 个字符")
	public String getCurbWeight() {
		return curbWeight;
	}

	public void setCurbWeight(String curbWeight) {
		this.curbWeight = curbWeight;
	}
	
	@Length(min=0, max=64, message="发动机型号长度不能超过 64 个字符")
	public String getEngineModel() {
		return engineModel;
	}

	public void setEngineModel(String engineModel) {
		this.engineModel = engineModel;
	}
	
	@Length(min=0, max=64, message="发动机号码长度不能超过 64 个字符")
	public String getEngineNum() {
		return engineNum;
	}

	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}
	
	@Length(min=0, max=64, message="车身尺寸长度不能超过 64 个字符")
	public String getBodySize() {
		return bodySize;
	}

	public void setBodySize(String bodySize) {
		this.bodySize = bodySize;
	}
	
	@Length(min=0, max=64, message="年检到期长度不能超过 64 个字符")
	public String getYearCheckDue() {
		return yearCheckDue;
	}

	public void setYearCheckDue(String yearCheckDue) {
		this.yearCheckDue = yearCheckDue;
	}
	
	@Length(min=0, max=64, message="保险到期长度不能超过 64 个字符")
	public String getInsuranceDue() {
		return insuranceDue;
	}

	public void setInsuranceDue(String insuranceDue) {
		this.insuranceDue = insuranceDue;
	}
	
	@Length(min=0, max=32, message="整车质保长度不能超过 32 个字符")
	public String getVehicleWarranty() {
		return vehicleWarranty;
	}

	public void setVehicleWarranty(String vehicleWarranty) {
		this.vehicleWarranty = vehicleWarranty;
	}

	public String getEnginePower() {
		return enginePower;
	}

	public void setEnginePower(String enginePower) {
		this.enginePower = enginePower;
	}
	@Length(min=0, max=64, message="品牌名称长度不能超过 64 个字符")
	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
}