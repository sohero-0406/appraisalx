/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CarInfoOneVO {

    private String id;
    @NotBlank
    private String licensePlateNum;        // 车牌号
    @NotBlank
    private String vinCode;        // vin码
    @NotBlank
    private String vehicleType;        // 车辆类型
}