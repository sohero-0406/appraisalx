/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.vo.VehicleDocumentInfoVO;
import com.jeesite.modules.common.entity.ExamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.VehicleDocumentInfo;
import com.jeesite.modules.aa.dao.VehicleDocumentInfoDao;
import org.springframework.util.CollectionUtils;

/**
 * 车辆单证信息Service
 *
 * @author lvchangwei
 * @version 2019-07-01
 */
@Service
@Transactional(readOnly = true)
public class VehicleDocumentInfoService extends CrudService<VehicleDocumentInfoDao, VehicleDocumentInfo> {

    @Autowired
    private PictureUserService pictureUserService;

    /**
     * 获取单条数据
     *
     * @param vehicleDocumentInfo
     * @return
     */
    @Override
    public VehicleDocumentInfo get(VehicleDocumentInfo vehicleDocumentInfo) {
        return super.get(vehicleDocumentInfo);
    }

    /**
     * 查询分页数据
     *
     * @param vehicleDocumentInfo      查询条件
     * @return
     */
    @Override
    public Page<VehicleDocumentInfo> findPage(VehicleDocumentInfo vehicleDocumentInfo) {
        return super.findPage(vehicleDocumentInfo);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param vehicleDocumentInfo
     */
    @Override
    @Transactional(readOnly = false)
    public void save(VehicleDocumentInfo vehicleDocumentInfo) {
        super.save(vehicleDocumentInfo);
    }

    /**
     * 更新状态
     *
     * @param vehicleDocumentInfo
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(VehicleDocumentInfo vehicleDocumentInfo) {
        super.updateStatus(vehicleDocumentInfo);
    }

    /**
     * 删除数据
     *
     * @param vehicleDocumentInfo
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(VehicleDocumentInfo vehicleDocumentInfo) {
        super.delete(vehicleDocumentInfo);
    }

    /**
     * 批量插入
     *
     * @param examUser
     * @param velicleDocumentJson
     */
    @Transactional
    public void saveBatch(ExamUser examUser, String velicleDocumentJson) {
        JSONArray jsonArray = JSONObject.parseArray(velicleDocumentJson);
        if (!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject object = (JSONObject) o;
                VehicleDocumentInfo info = new VehicleDocumentInfo();
                info.setExamUserId(examUser.getId());
                info.setPaperId(examUser.getPaperId());
                info.setId(object.getString("id"));
                info.setProject(object.getString("project"));
                info.setState(object.getString("state"));
                info.setValidity(object.getString("validity"));
                super.save(info);
            }
        }
    }

    /**
     * @param vehicleDocumentInfo
     * @return
     */
    @Transactional(readOnly = false)
    public List<VehicleDocumentInfo> findList(VehicleDocumentInfo vehicleDocumentInfo) {
        return dao.findList(vehicleDocumentInfo);
    }

    public VehicleDocumentInfo getByEntity(VehicleDocumentInfo vehicleDocumentInfo){ return dao.getByEntity(vehicleDocumentInfo); }

    /**
     * 车辆单证信息加载
     *
     * @param examUser
     * @return
     */
    public VehicleDocumentInfoVO findDocument(ExamUser examUser) {
        VehicleDocumentInfoVO vo = new VehicleDocumentInfoVO();
        VehicleDocumentInfo info = new VehicleDocumentInfo();
        info.setExamUserId(examUser.getId());
        info.setPaperId(examUser.getPaperId());
        vo.setInfoList(this.findList(info));

        String[] parentTypeIds = {"1143437059610071040"};
        String[] pictureTypeIds = {"1143432856340893696", "1143435061324763136", "1143435514869673984",
                "1143435674886193152"};
        List<PictureUser> pictureList = pictureUserService.findList(examUser, pictureTypeIds);
        pictureList.addAll(pictureUserService.findChildPicture(examUser, parentTypeIds));
        vo.setPictureList(pictureList);
        return vo;
    }
}