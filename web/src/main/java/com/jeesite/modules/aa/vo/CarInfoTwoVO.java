/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CarInfoTwoVO {

    private String id;
    @NotBlank
    private String mileage;        // 行驶里程(公里)
    @NotBlank
    private String labelType;        // 厂牌型号
    @NotBlank
    private String usage;        // 使用用途
    @NotBlank
    private String totalQuality;        // 总质量（kg）
    @NotBlank
    private String sear;        // 座位
    @NotBlank
    private String displacement;        // 排量（ml）
    @NotBlank
    private String fuelType;        // 燃油种类
    @NotBlank
    private String registerDate;        // 初次登记日期
    @NotBlank
    private String color;        // 车身颜色
    @NotNull
    private Integer useYear;        // 已使用年限-年
    @NotNull
    private Integer useMonth;        // 已使用年限-月
    @NotBlank
    private String engineOverhaulTimes;        // 发动机大修次数
    @NotBlank
    private String carOverhaulTimes;        // 整车大修次数
    @NotBlank
    private String maintenanceSituation;        // 维修情况
    @NotBlank
    private String accident;        // 事故情况
    @NotBlank
    private String purchaseDate;        // 购置日期
    @NotBlank
    private String originalPrice;        // 原始价格
    @NotBlank
    private String note;        // 备注
    @NotBlank
    private String enginePower;         // 发动机功率
    @NotBlank
    private String vehicleBrand;        //品牌名称

    private String engineNum;  //发动机号码
}