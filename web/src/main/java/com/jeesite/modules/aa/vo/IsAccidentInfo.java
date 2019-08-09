package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.CheckBodySkeleton;

import java.util.List;

/**
 * @author by Jiangyf
 * @classname IsAccidentInfo
 * @description 是否为事故车 - 技术状况表使用
 * @date 2019/8/8 17:51
 */
public class IsAccidentInfo {

    /**
     * 是否为事故车
     */
    private String isAccident;

    /**
     * 损伤位置及损伤状况
     */
    private List<CheckBodySkeleton> damageLocationAndStatus;

    public IsAccidentInfo() {
    }

    public String getIsAccident() {
        return isAccident;
    }

    public void setIsAccident(String isAccident) {
        this.isAccident = isAccident;
    }

    public List<CheckBodySkeleton> getDamageLocationAndStatus() {
        return damageLocationAndStatus;
    }

    public void setDamageLocationAndStatus(List<CheckBodySkeleton> damageLocationAndStatus) {
        this.damageLocationAndStatus = damageLocationAndStatus;
    }

    @Override
    public String toString() {
        return "IsAccidentInfo{" +
                "isAccident='" + isAccident + '\'' +
                ", damageLocationAndStatus=" + damageLocationAndStatus +
                '}';
    }
}
