/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.dao.IdentifyTecDao;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.vo.*;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import com.jeesite.modules.common.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴定技术状况Service
 *
 * @author lvchangwei
 * @version 2019-07-04
 */
@Service
@Transactional(readOnly = true)
public class IdentifyTecService extends CrudService<IdentifyTecDao, IdentifyTec> {

    @Autowired
    private IdentifyTecDetailService identifyTecDetailService;

    @Autowired
    private CarInfoService carInfoService;

    @Autowired
    private VehicleDocumentInfoService vehicleDocumentInfoService;

    @Autowired
    private DelegateUserService delegateUserService;

    @Autowired
    private VehicleInstallInfoService vehicleInstallInfoService;

    @Autowired
    private CheckTradableVehiclesService checkTradableVehiclesService;

    @Autowired
    private CheckBodySkeletonService checkBodySkeletonService;

    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;

    @Autowired
    private DelegateLetterService delegateLetterService;

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private ExamService examService;

    /**
     * 获取单条数据
     *
     * @param identifyTec
     * @return
     */
    @Override
    public IdentifyTec get(IdentifyTec identifyTec) {
        return super.get(identifyTec);
    }

    /**
     * 查询分页数据
     *
     * @param identifyTec 查询条件
     * @return
     */
    @Override
    public Page<IdentifyTec> findPage(IdentifyTec identifyTec) {
        return super.findPage(identifyTec);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param identifyTec
     */
    @Override
    @Transactional(readOnly = false)
    public void save(IdentifyTec identifyTec) {
        super.save(identifyTec);
    }

    /**
     * 更新状态
     *
     * @param identifyTec
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(IdentifyTec identifyTec) {
        super.updateStatus(identifyTec);
    }

    /**
     * 删除数据
     *
     * @param identifyTec
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(IdentifyTec identifyTec) {
        super.delete(identifyTec);
    }

    /**
     * 保存鉴定技术状况数据
     */
    @Transactional
    public void saveData(ExamUser examUser, String itemJson) {
        IdentifyTec identifyTec = new IdentifyTec();
        identifyTec.setExamUserId(examUser.getId());
        identifyTec.setPaperId(examUser.getPaperId());
        //业务开始
        JSONObject object = JSONObject.parseObject(itemJson);
        identifyTec.setId(object.getString("id"));
        identifyTec.setType(object.getString("type"));
        identifyTec.setTotalDeduct(object.getString("totalDeduct"));
        identifyTec.setDescription(object.getString("description"));
        super.save(identifyTec);
        JSONArray itemList = JSONObject.parseArray(object.getString("itemList"));

        if (!CollectionUtils.isEmpty(itemList)) {
            for (Object o : itemList) {
                JSONObject item = (JSONObject) o;
                IdentifyTecDetail detail = new IdentifyTecDetail();
                detail.setId(item.getString("id"));
                detail.setTechnologyInfoId(item.getString("technologyInfoId"));
                if(StringUtils.isBlank(detail.getTechnologyInfoId())){
                     throw new RuntimeException("technologyInfoId 不能为空！");
                }
                detail.setCode(item.getString("code"));
                detail.setDeductNum(item.getString("deductNum"));
                detail.setDegree(item.getString("degree"));
                detail.setIndentityTecId(identifyTec.getId());
                identifyTecDetailService.save(detail);
            }
        }
        operationLogService.saveObj(examUser, "保存鉴定技术状况数据成功");
    }

    public IdentifyTec getByEntity(IdentifyTec identifyTec) {
        return dao.getByEntity(identifyTec);
    }

    /**
     * @description: 加载二手车技术状况表
     * @param: [examUser]
     * @return: com.jeesite.modules.common.entity.CommonResult
     * @author: Jiangyf
     * @date: 2019/8/8
     * @time: 18:32
     */
    @Transactional(readOnly = true)
    public CommonResult findTechnicalStatusTable(ExamUser examUser) {
        CommonResult commonResult = new CommonResult();
        // TechnicalStatusTableVO 对象属性填充初始化
        TechnicalStatusTableVO technicalStatusTableVO = new TechnicalStatusTableVO();

        // 1. 车辆基本信息
        VehicleBasicInfoVO vehicleBasicInfo = new VehicleBasicInfoVO();
        // 数据查询
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
//        if (null == carInfo) {
//            return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "车辆信息对象为空");
//        }
        // 车辆配置信息
        JSONObject vehicleInfo = new JSONObject();
        if(null != carInfo){
            Map<String, String> map = new HashMap<>();
            map.put("chexingId", carInfo.getModel());
            CommonResult result = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, map);
            if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
                if(null!=result.getData()){
                    vehicleInfo = JSONObject.parseObject(result.getData().toString());
                }
            }
        }

        VehicleDocumentInfo vehicleDocumentInfo = new VehicleDocumentInfo();
        vehicleDocumentInfo.setExamUserId(examUser.getId());
        vehicleDocumentInfo.setPaperId(examUser.getPaperId());
        List<VehicleDocumentInfo> vehicleDocumentInfos = vehicleDocumentInfoService.findExistedDocuments(vehicleDocumentInfo);
        List<String> otherDocuments = vehicleDocumentInfoService.findOtherDocuments(vehicleDocumentInfo);
        StringBuilder otherDocumentName = new StringBuilder();
        int len = otherDocuments.size();
        for(int i=0;i<otherDocuments.size();i++){
            if(i==len-1){
                otherDocumentName.append(otherDocuments.get(i));
            }else{
                otherDocumentName.append(otherDocuments.get(i)+"、");
            }
        }
        DelegateUser delegateUser = new DelegateUser();
        delegateUser.setExamUserId(examUser.getId());
        delegateUser.setPaperId(examUser.getPaperId());
        delegateUser = delegateUserService.getByEntity(delegateUser);
        if (null == delegateUser) {
            return new CommonResult(CodeConstant.REQUEST_SUCCESSFUL, "委托人信息为空");
        }
        // 数据处理填充
        vehicleBasicInfo.setLabelType(carInfo.getLabelType());
        vehicleBasicInfo.setLicensePlateNum(carInfo.getLicensePlateNum());
        vehicleBasicInfo.setEngineNum(carInfo.getEngineNum());
        vehicleBasicInfo.setVinCode(carInfo.getVinCode());
        vehicleBasicInfo.setRegisterDate(carInfo.getRegisterDate());
        vehicleBasicInfo.setMileage(carInfo.getMileage());
        vehicleBasicInfo.setBrandName(vehicleInfo.getString("chexingmingcheng"));
        vehicleBasicInfo.setBrandType(DictUtils.getDictLabel("aa_mode_product", carInfo.getModeProduct(), ""));
        vehicleBasicInfo.setColor(DictUtils.getDictLabel("aa_vehicle_color", carInfo.getColor(), ""));
        // 注: 状态(1是 0无) 若该单证存在且存在有效期, 便返回其有效期; 若不存在, 返回"无"
        vehicleDocumentInfos.forEach(v -> {
            // 车险证明
            if ("6".equals(v.getProject())) {
                vehicleBasicInfo.setProject6Validity(v.getValidity());
            } else {
                vehicleBasicInfo.setProject6Validity("无");
            }
            // 购置税证书 无有效期 返回"有"
            if ("8".equals(v.getProject())) {
                vehicleBasicInfo.setProject8("有");
            } else {
                vehicleBasicInfo.setProject8("无");
            }
            // 车船税证明
            if ("3".equals(v.getProject())) {
                vehicleBasicInfo.setProject3Validity(v.getValidity());
            } else {
                vehicleBasicInfo.setProject3Validity("无");
            }
            // 交强险
            if ("4".equals(v.getProject())) {
                vehicleBasicInfo.setProject4Validity(v.getValidity());
            } else {
                vehicleBasicInfo.setProject4Validity("无");
            }
        });
        vehicleBasicInfo.setUsage(DictUtils.getDictLabel("aa_usage_type", carInfo.getUsage(), ""));
        // 结合字典表，单证信息(state = 1)进行dict_label返回 上述设置过的除外(不包括强制保险单 project = 4)
        if (StringUtils.isNotBlank(carInfo.getLicensePlateNum())) {
            // 额外单证信息 机动车号牌
            if(StringUtils.isNotBlank(otherDocumentName)){
                otherDocumentName.append("、机动车号牌");
            }else{
                otherDocumentName.append("机动车号牌");
            }
        }
        vehicleBasicInfo.setOtherDocuments(otherDocumentName.toString());
        vehicleBasicInfo.setName(delegateUser.getName());
        vehicleBasicInfo.setIdNum(delegateUser.getIdNum());

        // 2. 重要配置
        ImportantConfig importantConfig = new ImportantConfig();
        // 数据查询

        // 数据处理填充
        importantConfig.setFuelLabel(vehicleInfo.getString("ranyoubiaohao"));
        importantConfig.setDisplacement(carInfo.getDisplacement());
        importantConfig.setCylindersNum(vehicleInfo.getString("qigangshuGe"));
        importantConfig.setEnginePower(carInfo.getEnginePower());
        importantConfig.setEmissionStandards(vehicleInfo.getString("huanbaobiaozhun"));
        importantConfig.setTransmissionForm(vehicleInfo.getString("biansuxiangleixing"));
        importantConfig.setAirbag(ariBagTotal(vehicleInfo));
        importantConfig.setDriveMode(vehicleInfo.getString("qudongfangshi"));
        // 数据库字段 ABSfangbaosi 对应的值为 "●"
        if (StringUtils.isEmpty(vehicleInfo.getString("absfangbaosi"))) {
            importantConfig.setAbs("无");
        } else {
            importantConfig.setAbs("有");
        }
        // 车辆加装信息 返回全部加装配置信息进行返回 用户选定的加装配置 selected = true
        importantConfig.setVehicleInstallInfos(vehicleInstallInfoService.getVehicleInstallProject(examUser));

        // 3. 是否为事故车
        IsAccidentInfo isAccidentInfo = new IsAccidentInfo();
        // 数据查询
        CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
        checkTradableVehicles.setExamUserId(examUser.getId());
        checkTradableVehicles.setPaperId(examUser.getPaperId());
        checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);

        CheckBodySkeleton checkBodySkeleton = new CheckBodySkeleton();
        checkBodySkeleton.setExamUserId(examUser.getId());
        checkBodySkeleton.setPaperId(examUser.getPaperId());
        // 查询事故车辆核查项目结果
        List<CheckBodySkeleton> checkProjectResults = checkBodySkeletonService.findCheckProjectResults(checkBodySkeleton);

        // 数据处理填充
        if ("0".equals(checkTradableVehicles.getIsAccident())) {
            isAccidentInfo.setIsAccident("是");
        }else if("1".equals(checkTradableVehicles.getIsAccident()))
        {
            isAccidentInfo.setIsAccident("否");
        }else{
            isAccidentInfo.setIsAccident("");
        }
        isAccidentInfo.setDamageLocationAndStatus(checkProjectResults);

        // 4. 鉴定结果
        VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
        vehicleGradeAssess.setExamUserId(examUser.getId());
        vehicleGradeAssess.setPaperId(examUser.getPaperId());
        vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);

        // 技术状况登记
        vehicleGradeAssess.setTechnicalStatus(DictUtils.getDictLabel("aa_technical_status", vehicleGradeAssess.getTechnicalStatus(), ""));

        // 5. 车辆技术状况鉴定缺陷描述 无需处理
        List<IdentifyTec> identifyTecList = new ArrayList<>();
        IdentifyTec identifyTec = new IdentifyTec();
        identifyTec.setExamUserId(examUser.getId());
        identifyTec.setPaperId(examUser.getPaperId());
        identifyTecList = this.findVehicleTecStatusResult(identifyTec);


        // 6. 评估师
        technicalStatusTableVO.setEvaluator(vehicleGradeAssess.getEvaluator());
        // 7. 鉴定单位
        DelegateLetter delegateLetter = new DelegateLetter();
        delegateLetter.setPaperId(examUser.getPaperId());
        if(StringUtils.isBlank(examUser.getPaperId())){
            Exam exam = new Exam();
            exam.setId(examUser.getExamId());
            exam = examService.get(exam);
            delegateLetter.setPaperId(exam.getPaperId());
        }
        delegateLetter = delegateLetterService.getByEntity(delegateLetter);

        technicalStatusTableVO.setOrganizationName(delegateLetter.getOrganizationName());
        // 8. 鉴定日期
        technicalStatusTableVO.setIdentifyDate(vehicleGradeAssess.getIdentifyDate());

        // 数据填充返回
        technicalStatusTableVO.setVehicleBasicInfo(vehicleBasicInfo);
        technicalStatusTableVO.setImportantConfig(importantConfig);
        technicalStatusTableVO.setIsAccidentInfo(isAccidentInfo);
        technicalStatusTableVO.setVehicleGradeAssess(vehicleGradeAssess);
        technicalStatusTableVO.setIdentifyTecList(identifyTecList);
        commonResult.setData(technicalStatusTableVO);
        return commonResult;
    }

    /**
     * @description: 车辆技术状况鉴定缺陷描述结果
     * @param: [identifyTec]
     * @return: java.util.List<com.jeesite.modules.aa.entity.IdentifyTec>
     * @author: Jiangyf
     * @date: 2019/8/9
     * @time: 13:42
     */
    private List<IdentifyTec> findVehicleTecStatusResult(IdentifyTec identifyTec) {
        return dao.findVehicleTecStatusResult(identifyTec);
    }

    /**
     * @description: 计算气囊个数 - 仅针对主/副驾驶座安全气囊、前/后排侧气囊、前/后排头部气囊(气帘)
     * @param: [airBagType]
     * @return: java.lang.String
     * @author: Jiangyf
     * @date: 2019/8/10
     * @time: 10:07
     */
    public int airBagCount(String airBagType) {
        int count = 0;
        if (StringUtils.isNotBlank(airBagType)) {
            char[] chars = airBagType.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '●') {
                    count += 1;
                }
            }
            return count;
        }
        return count;
    }

    /**
     * @description: 计算气囊总数
     * @param: [vehicleInfo]
     * @return: java.lang.String
     * @author: Jiangyf
     * @date: 2019/8/10
     * @time: 10:26
     */
    public String ariBagTotal(JSONObject vehicleInfo) {
        int total = 0;
        total += airBagCount(vehicleInfo.getString("zhuFujiashizuoanquanqinang"));
        total += airBagCount(vehicleInfo.getString("qianHoupaiceqinang"));
        total += airBagCount(vehicleInfo.getString("qianHoupaitoubuqinangQilian"));
        // 其他三类气囊类型
        if (StringUtils.isNotBlank(vehicleInfo.getString("xibuqinang")) && "●".equals(vehicleInfo.getString("xibuqinang"))) {
            total += 2;
        }
        if (StringUtils.isNotBlank(vehicleInfo.getString("houpaianquandaishiqinang")) && "●".equals(vehicleInfo.getString("houpaianquandaishiqinang"))) {
            total += 3;
        }
        if (StringUtils.isNotBlank(vehicleInfo.getString("houpaizhongyanganquanqinang")) && "●".equals(vehicleInfo.getString("houpaizhongyanganquanqinang"))) {
            total += 1;
        }
        return String.valueOf(total);
    }
}