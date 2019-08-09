package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.DelegateUser;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.sys.entity.DictData;

import javax.validation.Valid;
import java.util.List;

public class BaseInfoVO {
    //用户图片列表
    private List<PictureUser> picList;
    //车辆基本信息
    @Valid
    private CarInfo carInfo;
    //委托方基本信息
    @Valid
    private DelegateUser delegateUser;
    //委托书类型列表
    private List<DictData> entrustTypeList;
    //燃油种类列表
    private List<DictData> fuelTypeList;
    //生产方式
    private List<DictData> modeProductList;
    //车身颜色
    private List<DictData> colorList;
    //使用性质
    private List<DictData> usageList;
    //车辆级别
    private List<DictData> levelList;
    //环保标准
    private List<DictData> environmentalStandardList;

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public DelegateUser getDelegateUser() {
        return delegateUser;
    }

    public void setDelegateUser(DelegateUser delegateUser) {
        this.delegateUser = delegateUser;
    }

    public List<PictureUser> getPicList() {
        return picList;
    }

    public void setPicList(List<PictureUser> picList) {
        this.picList = picList;
    }

    public List<DictData> getEntrustTypeList() {
        return entrustTypeList;
    }

    public void setEntrustTypeList(List<DictData> entrustTypeList) {
        this.entrustTypeList = entrustTypeList;
    }

    public List<DictData> getFuelTypeList() {
        return fuelTypeList;
    }

    public void setFuelTypeList(List<DictData> fuelTypeList) {
        this.fuelTypeList = fuelTypeList;
    }

    public List<DictData> getModeProductList() {
        return modeProductList;
    }

    public void setModeProductList(List<DictData> modeProductList) {
        this.modeProductList = modeProductList;
    }

    public List<DictData> getColorList() {
        return colorList;
    }

    public void setColorList(List<DictData> colorList) {
        this.colorList = colorList;
    }

    public List<DictData> getUsageList() {
        return usageList;
    }

    public void setUsageList(List<DictData> usageList) {
        this.usageList = usageList;
    }

    public List<DictData> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<DictData> levelList) {
        this.levelList = levelList;
    }

    public List<DictData> getEnvironmentalStandardList() {
        return environmentalStandardList;
    }

    public void setEnvironmentalStandardList(List<DictData> environmentalStandardList) {
        this.environmentalStandardList = environmentalStandardList;
    }
}
