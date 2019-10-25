package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.*;
import lombok.Data;

@Data
public class CalculateVO {
    private Calculate calculate;
    private CalculateCurrent calculateCurrent;
    private CalculateDepreciation calculateDepreciation;
    private CalculateKm calculateKm;
    private CalculateReplaceCost calculateReplaceCost;
    private String newCarPrice;         //新车市场参考价格
    private String teacherType;     //教师使用的算法
}
