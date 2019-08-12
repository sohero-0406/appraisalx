/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;

/**
 * 检查可交易车辆Entity
 * @author lvchangwei
 * @version 2019-07-01
 */
@Table(name="aa_check_tradable_vehicles", alias="a", columns={
		@Column(name="id", attrName="id", label="主键", isPK=true),
		@Column(name="exam_user_id", attrName="examUserId", label="外键id"),
		@Column(name="paper_id", attrName="paperId", label="试卷id"),
		@Column(name="vehicle_maintenance", attrName="vehicleMaintenance", label="车辆维保记录"),
		@Column(name="compensation_record", attrName="compensationRecord", label="出险记录"),
		@Column(name="check1", attrName="check1", label="是否达到国家强制报废标准"),
		@Column(name="check2", attrName="check2", label="是否为抵押期间或海关监管期间"),
		@Column(name="check3", attrName="check3", label="是否为人民法院、检察院、行政执法等部门依法查封、扣押期间的车辆"),
		@Column(name="check4", attrName="check4", label="是否为通过盗窃、抢劫、诈骗等违法犯罪手段获得的车辆"),
		@Column(name="check5", attrName="check5", label="发动机号与机动车登记证书登记号码是否一致，且无凿改痕迹"),
		@Column(name="check6", attrName="check6", label="车辆识别代号或车架号码与机动车登记证书登记号码是否一致，且无凿改痕迹"),
		@Column(name="check7", attrName="check7", label="是否走私、非法拼组装车辆"),
		@Column(name="check8", attrName="check8", label="是否法律法规禁止经营的车辆"),
		@Column(name="is_trade", attrName="isTrade", label="是否可交易"),
		@Column(name="traffic_illegal_record", attrName="trafficIllegalRecord", label="有无交通违法记录（1有 0无）"),
		@Column(name="is_accident", attrName="isAccident", label="是否事故车", comment="是否事故车（正常车，事故车）"),
		@Column(name="file_during", attrName="fileDuring", label="归档时效性"),
		@Column(includeEntity=DataEntity.class),
	}, orderBy="a.update_date DESC"
)
public class CheckTradableVehicles extends PreEntity<CheckTradableVehicles> {
	
	private static final long serialVersionUID = 1L;
	private String examUserId;		// 外键id
	private String paperId;		// 试卷id
	private String vehicleMaintenance;		// 车辆维保记录
	private String compensationRecord;		// 出险记录
	private String check1;		// 是否达到国家强制报废标准
	private String check2;		// 是否为抵押期间或海关监管期间
	private String check3;		// 是否为人民法院、检察院、行政执法等部门依法查封、扣押期间的车辆
	private String check4;		// 是否为通过盗窃、抢劫、诈骗等违法犯罪手段获得的车辆
	private String check5;		// 发动机号与机动车登记证书登记号码是否一致，且无凿改痕迹
	private String check6;		// 车辆识别代号或车架号码与机动车登记证书登记号码是否一致，且无凿改痕迹
	private String check7;		// 是否走私、非法拼组装车辆
	private String check8;		// 是否法律法规禁止经营的车辆
	private String isTrade;		// 是否可交易
	private String trafficIllegalRecord;		//有无交通违法记录(1有 0无)
	private String isAccident;		// 是否事故车（正常车，事故车）
	private String fileDuring;		// 归档时效性
	
	public CheckTradableVehicles() {
		this(null);
	}

	public CheckTradableVehicles(String id){
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
	
	@Length(min=0, max=10, message="车辆维保记录长度不能超过 10 个字符")
	public String getVehicleMaintenance() {
		return vehicleMaintenance;
	}

	public void setVehicleMaintenance(String vehicleMaintenance) {
		this.vehicleMaintenance = vehicleMaintenance;
	}
	
	@Length(min=0, max=10, message="出险记录长度不能超过 10 个字符")
	public String getCompensationRecord() {
		return compensationRecord;
	}

	public void setCompensationRecord(String compensationRecord) {
		this.compensationRecord = compensationRecord;
	}
	
	@Length(min=0, max=10, message="是否达到国家强制报废标准长度不能超过 10 个字符")
	public String getCheck1() {
		return check1;
	}

	public void setCheck1(String check1) {
		this.check1 = check1;
	}
	
	@Length(min=0, max=10, message="是否为抵押期间或海关监管期间长度不能超过 10 个字符")
	public String getCheck2() {
		return check2;
	}

	public void setCheck2(String check2) {
		this.check2 = check2;
	}
	
	@Length(min=0, max=10, message="是否为人民法院、检察院、行政执法等部门依法查封、扣押期间的车辆长度不能超过 10 个字符")
	public String getCheck3() {
		return check3;
	}

	public void setCheck3(String check3) {
		this.check3 = check3;
	}
	
	@Length(min=0, max=10, message="是否为通过盗窃、抢劫、诈骗等违法犯罪手段获得的车辆长度不能超过 10 个字符")
	public String getCheck4() {
		return check4;
	}

	public void setCheck4(String check4) {
		this.check4 = check4;
	}
	
	@Length(min=0, max=10, message="发动机号与机动车登记证书登记号码是否一致，且无凿改痕迹长度不能超过 10 个字符")
	public String getCheck5() {
		return check5;
	}

	public void setCheck5(String check5) {
		this.check5 = check5;
	}
	
	@Length(min=0, max=10, message="车辆识别代号或车架号码与机动车登记证书登记号码是否一致，且无凿改痕迹长度不能超过 10 个字符")
	public String getCheck6() {
		return check6;
	}

	public void setCheck6(String check6) {
		this.check6 = check6;
	}
	
	@Length(min=0, max=10, message="是否走私、非法拼组装车辆长度不能超过 10 个字符")
	public String getCheck7() {
		return check7;
	}

	public void setCheck7(String check7) {
		this.check7 = check7;
	}
	
	@Length(min=0, max=10, message="是否法律法规禁止经营的车辆长度不能超过 10 个字符")
	public String getCheck8() {
		return check8;
	}

	public void setCheck8(String check8) {
		this.check8 = check8;
	}
	
	@Length(min=0, max=10, message="是否可交易长度不能超过 10 个字符")
	public String getIsTrade() {
		return isTrade;
	}

	public void setIsTrade(String isTrade) {
		this.isTrade = isTrade;
	}

	@Length(min=0, max=10, message="有无交通违法记录不能超过 10 个字符")
	public String getTrafficIllegalRecord() {
		return trafficIllegalRecord;
	}

	public void setTrafficIllegalRecord(String trafficIllegalRecord) {
		this.trafficIllegalRecord = trafficIllegalRecord;
	}

	@Length(min=0, max=10, message="是否事故车长度不能超过 10 个字符")
	public String getIsAccident() {
		return isAccident;
	}

	public void setIsAccident(String isAccident) {
		this.isAccident = isAccident;
	}
	
	@Length(min=0, max=256, message="归档时效性长度不能超过 256 个字符")
	public String getFileDuring() {
		return fileDuring;
	}

	public void setFileDuring(String fileDuring) {
		this.fileDuring = fileDuring;
	}
	
}