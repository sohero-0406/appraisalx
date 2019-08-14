package com.jeesite.modules.aa.vo;

import com.jeesite.modules.aa.entity.PictureUser;

public class PictureUserVO {
    private String id;
    private String url;
    private String pictureTypeId;
    private String name;
    private String detail;

    public PictureUserVO(PictureUser pictureUser){
        this.id = pictureUser.getId();
        this.url = pictureUser.getUrl();
        this.pictureTypeId = pictureUser.getPictureTypeId();
        this.name = pictureUser.getName();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getPictureTypeId() {
        return pictureTypeId;
    }

    public void setPictureTypeId(String pictureTypeId) {
        this.pictureTypeId = pictureTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
