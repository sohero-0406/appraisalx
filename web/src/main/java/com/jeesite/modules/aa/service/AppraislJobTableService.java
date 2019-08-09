package com.jeesite.modules.aa.service;


import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.vo.AppraisalJobTableVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 二手车鉴定评估作业表Service
 * @author lvchangwei
 * @version 2019-07-23
 */
@Service
@Transactional(readOnly = true)
public class AppraislJobTableService {

    @Autowired
    private CarInfoService carInforService;
    @Autowired
    private VehicleDocumentInfoService vehicleDocumentInfoService;
    @Autowired
    private VehicleInstallInfoService vehicleInstallInfoService;
    @Autowired
    private IdentifyTecService identifyTecService;
    @Autowired
    private IdentifyTecDetailService identifyTecDetailService;
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    @Autowired
    private DelegateLetterService delegateLetterService;
    @Autowired
    private CheckBodySkeletonService checkBodySkeletonService;
    @Autowired
    private CheckTradableVehiclesService checkTradableVehiclesService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 生成鉴定评估作业表
     */
    public AppraisalJobTableVO findAppraisalJobTable(ExamUser examUser) {

        AppraisalJobTableVO appraisalJobTableVO = new AppraisalJobTableVO();

        //委托车辆信息
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getExamId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInforService.getByEntity(carInfo);
        //设置级别
        carInfo.setLevel(DictUtils.getDictLabel("aa_vehicle_level",carInfo.getLevel(),""));
        //设置初登日期
        if (StringUtils.isNotBlank(carInfo.getRegisterDate())) {
            String[] registerDateArr = carInfo.getRegisterDate().substring(0, 10).split("-");
            carInfo.setRegisterDate(registerDateArr[0] + "年" + registerDateArr[1] + "月" + registerDateArr[2] + "日");
        }
        //设置出厂日期
        if (StringUtils.isNotBlank(carInfo.getManufactureDate())) {
            String[] manufactureDate = carInfo.getManufactureDate().substring(0, 10).split("-");
            carInfo.setManufactureDate(manufactureDate[0] + "年" + manufactureDate[1] + "月" + manufactureDate[2] + "日");
        }
        //设置环保标准
        carInfo.setEnvironmentalStandard(DictUtils.getDictLabel("aa_environmental_standard",
                carInfo.getEnvironmentalStandard(),""));
        //设置年检到期
        if (StringUtils.isNotBlank(carInfo.getYearCheckDue())) {
            String[] yearCheckDueArr = carInfo.getYearCheckDue().substring(0, 10).split("-");
            carInfo.setYearCheckDue(yearCheckDueArr[0] + "年" + yearCheckDueArr[1] + "月" + yearCheckDueArr[2] + "日");
        }
        //设置保险到期
        if (StringUtils.isNotBlank(carInfo.getInsuranceDue())) {
            String[] insuranceDue = carInfo.getInsuranceDue().substring(0, 10).split("-");
            carInfo.setInsuranceDue(insuranceDue[0] + "年" + insuranceDue[1] + "月" + insuranceDue[2] + "日");
        }
        appraisalJobTableVO.setCarInfo(carInfo);

        //车辆加装信息
        VehicleInstallInfo vehicleInstallInfo = new VehicleInstallInfo();
        vehicleInstallInfo.setExamUserId(examUser.getExamId());
        vehicleInstallInfo.setPaperId(examUser.getPaperId());
        List<VehicleInstallInfo> vehicleInstallInfoList = vehicleInstallInfoService.findList(vehicleInstallInfo);
        //设置加装信息
        for(VehicleInstallInfo vii: vehicleInstallInfoList){
            vii.setProject(vehicleInstallInfoService.getProject(vii));
        }
        appraisalJobTableVO.setVehicleInstallInfoList(vehicleInstallInfoList);

        //车辆单证信息
        VehicleDocumentInfo vehicleDocumentInfo = new VehicleDocumentInfo();
        vehicleDocumentInfo.setExamUserId(examUser.getExamId());
        vehicleDocumentInfo.setPaperId(examUser.getPaperId());
        List<VehicleDocumentInfo> vehicleDocumentInfoList = vehicleDocumentInfoService.findList(vehicleDocumentInfo);
        for(VehicleDocumentInfo vdi: vehicleDocumentInfoList){
            //设置是否
            if("1".equals(vdi.getState())){
                vdi.setState("有");
                //设置有效期
                if (StringUtils.isNotBlank(vdi.getValidity())) {
                    String[] validity = vdi.getValidity().substring(0, 10).split("-");
                    vdi.setValidity(validity[0] + "年" + validity[1] + "月" + validity[2] + "日");
                }
            }else{
                vdi.setState("无");
            }
        }
        appraisalJobTableVO.setVehicleDocumentInfoList(vehicleDocumentInfoList);

        //车辆配置全表
        Map<String, String> map = new HashMap<>();
        map.put("chexingId", carInfo.getModel());
        CommonResult result = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_BY_ENTITY, map);
        if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
            JSONObject vehicleInfo = JSONObject.parseObject(result.getData().toString());
            //设置品牌
            carInfo.setBrand(vehicleInfo.getString("pinpai"));
            appraisalJobTableVO.setVehicleInfo(vehicleInfo);
            //设置年款型号
            String[] chexingArr = vehicleInfo.getString("chexingmingcheng").split(" ");
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < chexingArr.length; i++){
                sb.append(chexingArr[i]);
            }
            appraisalJobTableVO.setNiankuanxinghao(sb.toString());
        }

        //检查车体骨架
        CheckBodySkeleton checkBodySkeleton = new CheckBodySkeleton();
        checkBodySkeleton.setExamUserId(examUser.getExamId());
        checkBodySkeleton.setPaperId(examUser.getPaperId());
        List<CheckBodySkeleton> checkBodySkeletonList = checkBodySkeletonService.findList(checkBodySkeleton);
        //设置鉴定项
        for(CheckBodySkeleton cbs: checkBodySkeletonList){
            cbs.setTechnologyInfoId(checkBodySkeletonService.getTechnologyInfo(cbs));
        }
        appraisalJobTableVO.setCheckBodySkeletonList(checkBodySkeletonList);

        //检查可交易车辆
        CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
        checkTradableVehicles.setExamUserId(examUser.getExamId());
        checkTradableVehicles.setPaperId(examUser.getPaperId());
        checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);
        //设置事故车判定
        if("0".equals(checkTradableVehicles.getIsAccident())){
            //正常车
            checkTradableVehicles.setIsAccident("正常车");
        }else{
            checkTradableVehicles.setIsAccident("事故车");
        }
        appraisalJobTableVO.setCheckTradableVehicles(checkTradableVehicles);

        //鉴定技术状况
        IdentifyTec identifyTec = new IdentifyTec();
        identifyTec.setExamUserId(examUser.getExamId());
        identifyTec.setPaperId(examUser.getPaperId());
        List<IdentifyTec> identifyTecDetailList =  identifyTecDetailService.findIdentityTecCondition(identifyTec);
        appraisalJobTableVO.setIdentifyTecList(identifyTecDetailList);

        //车辆等级评定
        VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
        vehicleGradeAssess.setExamUserId(examUser.getExamId());
        vehicleGradeAssess.setPaperId(examUser.getPaperId());
        vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
        //设置技术状况
        vehicleGradeAssess.setTechnicalStatus(vehicleGradeAssessService.getTechnicalStatus(vehicleGradeAssess));
        appraisalJobTableVO.setVehicleGradeAssess(vehicleGradeAssess);

        // 作业表尾部信息
        DelegateLetter delegateLetter = new DelegateLetter();
        delegateLetter.setPaperId(examUser.getPaperId());
        appraisalJobTableVO.setDelegateLetter(delegateLetterService.getByEntity(delegateLetter));
        return appraisalJobTableVO;
    }

}
