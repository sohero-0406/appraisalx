/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.entity.VehicleDocumentInfo;

import java.util.List;

public class VehicleDocumentInfoVO {

    /**
     * 车辆单证信息
     */
    private List<VehicleDocumentInfo> infoList;

    /**
     * 单证图片
     */
    private List<PictureUser> pictureList;

    public List<VehicleDocumentInfo> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<VehicleDocumentInfo> infoList) {
        this.infoList = infoList;
    }

    public List<PictureUser> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<PictureUser> pictureList) {
        this.pictureList = pictureList;
    }
}