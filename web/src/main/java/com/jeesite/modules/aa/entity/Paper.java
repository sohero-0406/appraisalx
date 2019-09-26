/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.entity;

import com.jeesite.common.entity.DataEntity;
import com.jeesite.common.mybatis.annotation.Column;
import com.jeesite.common.mybatis.annotation.Table;
import com.jeesite.common.mybatis.mapper.query.QueryType;
import com.jeesite.modules.common.entity.PreEntity;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 试卷Entity
 *
 * @author lvchangwei
 * @version 2019-07-16
 */
@Table(name = "aa_paper", alias = "a", columns = {
        @Column(name = "id", attrName = "id", label = "主键", isPK = true),
        @Column(name = "user_id", attrName = "userId", label = "用户id"),
        @Column(name = "name", attrName = "name", label = "试卷名称", queryType = QueryType.LIKE),
        @Column(name = "state", attrName = "state", label = "启用禁用状态"),
        @Column(name = "true_name", attrName = "trueName", label = "真实姓名"),
        @Column(includeEntity = DataEntity.class),
}, orderBy = "a.update_date DESC"
)
public class Paper extends PreEntity<Paper> {

    private static final long serialVersionUID = 1L;
    private String userId;        // 用户id
    private String name;        // 试卷名称
    private String state;        //启用禁用状态
    private String trueName;        //真实姓名

    // 数据库不存在字段
    private CarInfo carInfo;
    private String licensePlateNum;
    private String vinCode;

    public String getLicensePlateNum() {
        return licensePlateNum;
    }

    public void setLicensePlateNum(String licensePlateNum) {
        this.licensePlateNum = licensePlateNum;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public Paper() {
        this(null);
    }

    public Paper(String id) {
        super(id);
    }

    @Length(min = 0, max = 64, message = "用户id长度不能超过 64 个字符")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Length(min = 0, max = 255, message = "试卷名称长度不能超过 255 个字符")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 16, message = "启用禁用状态长度不能超过 16 个字符")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Length(min = 0, max = 64, message = "真实姓名长度不能超过 16 个字符")
    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}