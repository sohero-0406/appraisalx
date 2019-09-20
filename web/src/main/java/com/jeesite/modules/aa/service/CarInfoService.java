/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.CarInfoDao;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.DelegateUser;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.vo.BaseInfoVO;
import com.jeesite.modules.aa.vo.HomePageVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.common.service.OperationLogService;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 委托车辆信息Service
 *
 * @author chenlitao
 * @version 2019-06-29
 */
@Service
@Transactional(readOnly = true)
public class CarInfoService extends CrudService<CarInfoDao, CarInfo> {
    @Autowired
    private DelegateUserService delegateUserService;
    @Autowired
    private PictureUserService pictureUserService;
    @Autowired
    private CarInfoDao carInfoDao;
    @Autowired
    private OperationLogService operationLogService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 获取单条数据
     *
     * @param carInfo
     * @return
     */
    @Override
    public CarInfo get(CarInfo carInfo) {
        return super.get(carInfo);
    }

    /**
     * 查询分页数据
     *
     * @param carInfo 查询条件
     * @return
     */
    @Override
    public Page<CarInfo> findPage(CarInfo carInfo) {
        return super.findPage(carInfo);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param carInfo
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CarInfo carInfo) {
        super.save(carInfo);
    }

    /**
     * 更新状态
     *
     * @param carInfo
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CarInfo carInfo) {
        super.updateStatus(carInfo);
    }

    /**
     * 删除数据
     *
     * @param carInfo
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CarInfo carInfo) {
        super.delete(carInfo);
    }

    /**
     * 保存车辆基本信息和委托方基本信息
     *
     * @param delegateUser
     * @param carInfo
     * @param examUser
     */
    @Transactional
    public void saveBaseInfo(DelegateUser delegateUser, CarInfo carInfo, ExamUser examUser) {
        if (null == delegateUser) {
            delegateUser = new DelegateUser();
        }
        DelegateUser user = new DelegateUser();
        user.setExamUserId(examUser.getId());
        user.setPaperId(examUser.getPaperId());
        user = delegateUserService.getByEntity(user);
        if (null == user) {
            //生成委托书编号
            delegateUserService.createDelegateLetterNum(delegateUser);
        }
        delegateUser.setExamUserId(examUser.getId());
        delegateUser.setPaperId(examUser.getPaperId());
        delegateUserService.save(delegateUser);

        if (null == carInfo) {
            carInfo = new CarInfo();
        }
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        this.save(carInfo);
        operationLogService.saveObj(examUser,"保存委托书及车辆基本信息成功");
    }

    /**
     * 加载车辆基本信息、委托方基本信息、图片信息、委托书类型列表、燃油种类列表、车身颜色列表
     *
     * @param examUser       考生用户
     * @param pictureTypeIds 考生图片类型ids
     * @return
     */
    public BaseInfoVO getBaseInfo(ExamUser examUser, String[] pictureTypeIds) {
        String examUserId = examUser.getId();
        String paperId = examUser.getPaperId();
        BaseInfoVO baseInfoVO = new BaseInfoVO();

        //加载证件列表
        List<PictureUser> picList = pictureUserService.findPictureList(examUser, pictureTypeIds);
        baseInfoVO.setPicList(picList);

        //加载车辆基本信息
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUserId);
        carInfo.setPaperId(paperId);
        carInfo = this.getByEntity(carInfo);
        //品牌 車系
        String pinpaichexi = "";
        if(null!=carInfo){
            if(StringUtils.isNotBlank(carInfo.getBrand())){
                Map<String,String> map = new HashMap<>();
                map.put("pinpaiId",carInfo.getBrand());
                CommonResult result =  httpClientService.post(ServiceConstant.COMMON_VEHICLE_BRAND_GET_BY_ENTITY,map);
                if(CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())){
                    if(null!=result.getData()){
                        JSONObject o = (JSONObject)result.getData();
                        String pinpai =  o.getString("pinpai");
                        pinpaichexi= pinpaichexi+pinpai;
                    }
                }
            }
            if(StringUtils.isNotBlank(carInfo.getSeries())){
                Map<String,String> map = new HashMap<>();
                map.put("chexiId",carInfo.getSeries());
                CommonResult result =  httpClientService.post(ServiceConstant.COMMON_VEHICLE_SERIES_GET_BY_ENTITY,map);
                if(CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())){
                    if(null!=result.getData()){
                        JSONObject o = (JSONObject)result.getData();
                        String chexi =  o.getString("chexi");
                        pinpaichexi = pinpaichexi + chexi;
                    }
                }
            }
            carInfo.setBrandName(pinpaichexi);
            baseInfoVO.setCarInfo(carInfo);
        }

        //加载委托方基本信息
        DelegateUser delegateUser = new DelegateUser();
        delegateUser.setExamUserId(examUserId);
        delegateUser.setPaperId(paperId);
        baseInfoVO.setDelegateUser(delegateUserService.getByEntity(delegateUser));

        //加载委托书类型
        List<DictData> entrustTypeList = DictUtils.getDictList("aa_entrust_file_type");
        baseInfoVO.setEntrustTypeList(entrustTypeList);

        //加载燃油种类
        List<DictData> fuelTypeList = DictUtils.getDictList("aa_fuel_type");
        baseInfoVO.setFuelTypeList(fuelTypeList);

        //加载使用性质
        List<DictData> usageList = DictUtils.getDictList("aa_usage_type");
        baseInfoVO.setUsageList(usageList);

        //加载生产方式
        List<DictData> modeProductList = DictUtils.getDictList("aa_mode_product");
        baseInfoVO.setModeProductList(modeProductList);

        //加载车身颜色
        List<DictData> colorList = DictUtils.getDictList("aa_vehicle_color");
        baseInfoVO.setColorList(colorList);

        //加载车辆级别
        List<DictData> levelList = DictUtils.getDictList("aa_vehicle_level");
        baseInfoVO.setLevelList(levelList);

        //加载环保标准
        List<DictData> environmentalStandardList = DictUtils.getDictList("aa_environmental_standard");
        baseInfoVO.setEnvironmentalStandardList(environmentalStandardList);
        return baseInfoVO;
    }

    public CarInfo getByEntity(CarInfo carInfo) {
        return dao.getByEntity(carInfo);
    }

    /**
     * 根据排序规则（正序或者倒序）返回列表
     *
     * @param homePageVO
     * @return
     */
    public CarInfo findCarInfoBySortStu(HomePageVO homePageVO) {
        Map<String, String> hs = new HashMap<>();
        hs.put("examUserId", homePageVO.getCarInfo().getExamUserId());
        hs.put("queryCriteria", homePageVO.getQueryCriteria());
        hs.put("sort", homePageVO.getSort());
        return carInfoDao.findCarInfoBySortStu(hs);
    }

    /**
     * 根据车型id获取车型数据
     */
    public CommonResult getVehicleFunctionalInfo(String model){
        Map<String, String> map = new HashMap<>();
        map.put("chexingId",model);
        return httpClientService.post(ServiceConstant.VEHICLEINFO_GET_BY_ENTITY,map);
    }
}