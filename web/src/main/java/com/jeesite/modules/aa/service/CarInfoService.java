/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.CarInfoDao;
import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.DelegateUser;
import com.jeesite.modules.aa.entity.PictureUser;
import com.jeesite.modules.aa.vo.BaseInfoVO;
import com.jeesite.modules.aa.vo.HomePageVO;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param carInfo      查询条件
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
     * @param baseInfoVO
     * @param examUser
     */
    @Transactional
    public void saveBaseInfo(BaseInfoVO baseInfoVO, ExamUser examUser) {
        DelegateUser delegateUser = baseInfoVO.getDelegateUser();
        if (null == delegateUser) {
            delegateUser = new DelegateUser();
            delegateUser.setExamUserId(examUser.getId());
            delegateUser.setPaperId(examUser.getPaperId());
            DelegateUser user = delegateUserService.getByEntity(delegateUser);
            if (null == user) {
                delegateUserService.save(delegateUser);
            }
        } else {
            delegateUserService.save(delegateUser);
        }

        CarInfo carInfo = baseInfoVO.getCarInfo();
        if (null == carInfo) {
            carInfo = new CarInfo();
        }
        carInfo.setExamUserId(examUser.getId());
        this.save(carInfo);
    }

    /**
     * 加载车辆基本信息、委托方基本信息、图片信息、委托书类型列表、燃油种类列表、车身颜色列表
     *
     * @param examUser     考生用户
     * @param pictureTypeIds 考生图片类型ids，多个id之间用“,”分隔
     * @return
     */
    public BaseInfoVO getBaseInfo(ExamUser examUser, String[] pictureTypeIds) {
        String examUserId = examUser.getId();
        String paperId = examUser.getPaperId();
        BaseInfoVO baseInfoVO = new BaseInfoVO();

        //加载证件列表
        List<PictureUser> picList = pictureUserService.findList(examUser, pictureTypeIds);
        baseInfoVO.setPicList(picList);

        //加载车辆基本信息
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUserId);
        carInfo.setPaperId(paperId);
        baseInfoVO.setCarInfo(this.getByEntity(carInfo));

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
     * @param homePageVO
     * @return
     */
    public CarInfo findCarInfoBySortStu(HomePageVO homePageVO) {
        Map<String,String> hs = new HashMap<>();
        hs.put("examUserId",homePageVO.getCarInfo().getExamUserId());
        hs.put("queryCriteria",homePageVO.getQueryCriteria());
        hs.put("sort",homePageVO.getSort());
        return carInfoDao.findCarInfoBySortStu(hs);
    }
}