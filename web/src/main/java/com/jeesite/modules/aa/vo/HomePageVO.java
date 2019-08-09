package com.jeesite.modules.aa.vo;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.entity.VehicleGradeAssess;
import com.jeesite.modules.common.entity.VehicleInfo;

public class HomePageVO {

    private CarInfo carInfo;        //委托车辆信息
    private PictureUser pictureUser;        //用户图片表
    private VehicleGradeAssess vehicleGradeAssess;      //车辆等级评定
    private JSONObject vehicleInfo;        //车辆配置全表
    private String sort;        //排序方式
    private String queryCriteria;       //查询条件

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public PictureUser getPictureUser() {
        return pictureUser;
    }

    public void setPictureUser(PictureUser pictureUser) {
        this.pictureUser = pictureUser;
    }

    public VehicleGradeAssess getVehicleGradeAssess() {
        return vehicleGradeAssess;
    }

    public void setVehicleGradeAssess(VehicleGradeAssess vehicleGradeAssess) {
        this.vehicleGradeAssess = vehicleGradeAssess;
    }

    public JSONObject getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(JSONObject vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getQueryCriteria() {
        return queryCriteria;
    }

    public void setQueryCriteria(String queryCriteria) {
        this.queryCriteria = queryCriteria;
    }
}
