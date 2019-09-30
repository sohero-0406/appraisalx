/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CarInfoThreeVO {

    private String id;
    @NotBlank
    private String brand;        // 品牌
    @NotBlank
    private String series;        // 车系
    @NotBlank
    private String model;        // 车型
    @NotBlank
    private String level;        // 级别
    @NotBlank
    private String vinCode;        // vin码
    @NotBlank
    private String registerDate;        // 初次登记日期
    @NotBlank
    private String modeProduct;        //生产方式
    @NotBlank
    private String manufactureDate;        // 出厂日期
    @NotBlank
    private String color;        // 车身颜色
    @NotBlank
    private String mileage;        // 行驶里程(公里)
    @NotBlank
    private String displacement;        // 排量（ml）
    @NotNull
    private Integer changeNum;        // 过户次数
    @NotBlank
    private String fuelType;        // 燃油种类
    @NotBlank
    private String environmentalStandard;        // 环保标准
    @NotBlank
    private String usingNature;        // 使用用途
    @NotBlank
    private String bodyStructure;        // 车身结构
    @NotBlank
    private String curbWeight;        // 整备质量
    @NotBlank
    private String engineModel;        // 发动机型号
    @NotBlank
    private String engineNum;        // 发动机号码
    @NotBlank
    private String bodySize;        // 车身尺寸
    @NotBlank
    private String yearCheckDue;        // 年检到期
    @NotBlank
    private String insuranceDue;        // 保险到期
    @NotBlank
    private String vehicleWarranty;        // 整车质保

}