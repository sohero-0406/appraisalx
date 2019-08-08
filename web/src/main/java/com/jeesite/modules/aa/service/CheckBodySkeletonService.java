/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.CheckBodySkeletonDao;
import com.jeesite.modules.aa.entity.CheckBodySkeleton;
import com.jeesite.modules.aa.entity.CheckTradableVehicles;
import com.jeesite.modules.aa.entity.ExamDetail;
import com.jeesite.modules.aa.vo.CheckBodySkeletonVO;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 检查车体骨架Service
 *
 * @author lvchangwei
 * @version 2019-07-02
 */
@Service
@Transactional(readOnly = true)
public class CheckBodySkeletonService extends CrudService<CheckBodySkeletonDao, CheckBodySkeleton> {

    @Autowired
    private CheckBodySkeletonDao checkBodySkeletonDao;

    @Autowired
    private CheckTradableVehiclesService checkTradableVehiclesService;

    @Autowired
    private ExamDetailService examDetailService;

    @Autowired
    private ExamService examService;
    @Autowired
    private OperationLogService operationLogService;

    /**
     * 获取单条数据
     *
     * @param checkBodySkeleton
     * @return
     */
    @Override
    public CheckBodySkeleton get(CheckBodySkeleton checkBodySkeleton) {
        return super.get(checkBodySkeleton);
    }

    /**
     * 查询分页数据
     *
     * @param checkBodySkeleton      查询条件
     * @return
     */
    @Override
    public Page<CheckBodySkeleton> findPage(CheckBodySkeleton checkBodySkeleton) {
        return super.findPage(checkBodySkeleton);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param checkBodySkeleton
     */
    @Override
    @Transactional(readOnly = false)
    public void save(CheckBodySkeleton checkBodySkeleton) {
        super.save(checkBodySkeleton);
    }

    /**
     * 更新状态
     *
     * @param checkBodySkeleton
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(CheckBodySkeleton checkBodySkeleton) {
        super.updateStatus(checkBodySkeleton);
    }

    /**
     * 删除数据
     *
     * @param checkBodySkeleton
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(CheckBodySkeleton checkBodySkeleton) {
        super.delete(checkBodySkeleton);
    }

    @Transactional
    public void saveBatch(ExamUser examUser, String veliclePicJson) {
        JSONArray jsonArray = JSONObject.parseArray(veliclePicJson);
        if (!CollectionUtils.isEmpty(jsonArray)) {
            for (Object o : jsonArray) {
                JSONObject object = (JSONObject) o;
                CheckBodySkeleton checkBodySkeleton = new CheckBodySkeleton();
                checkBodySkeleton.setExamUserId(examUser.getId());
                checkBodySkeleton.setPaperId(examUser.getPaperId());
                checkBodySkeleton.setId(object.getString("id"));
                checkBodySkeleton.setTechnologyInfoId(object.getString("project"));
                checkBodySkeleton.setState(object.getString("state"));
                checkBodySkeleton.setDescription(object.getString("description"));
                super.save(checkBodySkeleton);
            }
            operationLogService.saveObj(examUser,"保存检查车体骨架数据成功");
        }
    }

    /**
     * 判定事故车加载
     */
    public CheckBodySkeletonVO findAccidentVehicle(ExamUser examUser) {
        CheckBodySkeletonVO vo = new CheckBodySkeletonVO();

        CheckBodySkeleton checkBodySkeleton = new CheckBodySkeleton();
        CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
        //查询启用项
        ExamDetail examDetail = new ExamDetail();
        //考生
        if (StringUtils.isNotBlank(examUser.getExamId())) {
            examDetail.setExamId(examUser.getExamId());
            examDetail = examDetailService.getByEntity(examDetail);
            vo.setExamDetail(examDetail);
            //未查到或者为启用，均查询学生答案，否则查询正确答案
            if (null == examDetail || "1".equals(examDetail.getEnableCheckBodySkeleton())) {
                checkBodySkeleton.setExamUserId(examUser.getId());
                checkTradableVehicles.setExamUserId(examUser.getId());
            } else {
                Exam exam = examService.get(examUser.getExamId());
                if (null != exam) {
                    checkBodySkeleton.setPaperId(exam.getPaperId());
                    checkTradableVehicles.setPaperId(examUser.getPaperId());
                }
            }
        } else {
            //教师
            checkBodySkeleton.setPaperId(examUser.getPaperId());
            checkTradableVehicles.setPaperId(examUser.getPaperId());
        }
        List<CheckBodySkeleton> list = checkBodySkeletonDao.findAccidentVehicle(checkBodySkeleton);
        vo.setSkeletonList(list);

        checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);
        vo.setCheckTradableVehicles(checkTradableVehicles);
//        //查询启用项
//        ExamDetail examDetail = new ExamDetail();
//        examDetail.setExamId(examUser.getExamId());
//        vo.setExamDetail(examDetailService.getByEntity(examDetail));
        return vo;
    }

    public CheckBodySkeletonVO findBodySkeleton(ExamUser examUser) {
        CheckBodySkeletonVO vo = new CheckBodySkeletonVO();

        CheckBodySkeleton checkBodySkeleton = new CheckBodySkeleton();
        //查询启用项
        ExamDetail examDetail = new ExamDetail();
        //考生
        if (StringUtils.isNotBlank(examUser.getExamId())) {
            examDetail.setExamId(examUser.getExamId());
            examDetail = examDetailService.getByEntity(examDetail);
            vo.setExamDetail(examDetail);
            //未查到或者为启用，均查询学生答案，否则查询正确答案
            if (null == examDetail || "1".equals(examDetail.getEnableCheckBodySkeleton())) {
                checkBodySkeleton.setExamUserId(examUser.getId());
            } else {
                Exam exam = examService.get(examUser.getExamId());
                if (null != exam) {
                    checkBodySkeleton.setPaperId(exam.getPaperId());
                }
            }
        } else {
            //教师
            checkBodySkeleton.setPaperId(examUser.getPaperId());
        }
        vo.setSkeletonList(findList(checkBodySkeleton));
        return vo;
    }

    public CheckBodySkeleton getByEntity(CheckBodySkeleton checkBodySkeleton){ return  dao.getByEntity(checkBodySkeleton); }

    /**
     * 查询鉴定项
     */
    public String getTechnologyInfo(CheckBodySkeleton checkBodySkeleton) {
        return checkBodySkeletonDao.getTechnologyInfo(checkBodySkeleton);
    }
}