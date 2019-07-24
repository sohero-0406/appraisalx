package com.jeesite.modules.aa.service;


import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.CheckBodySkeleton;
import com.jeesite.modules.aa.entity.VehicleDocumentInfo;
import com.jeesite.modules.aa.entity.VehicleInstallInfo;
import com.jeesite.modules.aa.vo.AppraisalJobTableVO;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.entity.VehicleInfo;
import com.jeesite.modules.common.service.VehicleInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private VehicleInfoService vehicleInfoService;

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
        if("1".equals(carInfo.getLevel())){
            carInfo.setLevel("A0级");
        }else if("2".equals(carInfo.getLevel())){
            carInfo.setLevel("A级");
        }else if("3".equals(carInfo.getLevel())){
            carInfo.setLevel("B级");
        }else if("4".equals(carInfo.getLevel())){
            carInfo.setLevel("C级");
        }else if("5".equals(carInfo.getLevel())){
            carInfo.setLevel("D级");
        }else if("6".equals(carInfo.getLevel())){
            carInfo.setLevel("SUV");
        }
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
        if("1".equals(carInfo.getEnvironmentalStandard())){
            carInfo.setEnvironmentalStandard("国I");
        }else if("2".equals(carInfo.getEnvironmentalStandard())){
            carInfo.setEnvironmentalStandard("国II");
        }else if("3".equals(carInfo.getEnvironmentalStandard())){
            carInfo.setEnvironmentalStandard("国III");
        }else if("4".equals(carInfo.getEnvironmentalStandard())){
            carInfo.setEnvironmentalStandard("国IV");
        }else if("5".equals(carInfo.getEnvironmentalStandard())){
            carInfo.setEnvironmentalStandard("国V");
        }else if("6".equals(carInfo.getEnvironmentalStandard())){
            carInfo.setEnvironmentalStandard("国VI");
        }
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
        appraisalJobTableVO.setVehicleInstallInfoList(vehicleInstallInfoList);

        //车辆单证信息
        VehicleDocumentInfo vehicleDocumentInfo = new VehicleDocumentInfo();
        vehicleDocumentInfo.setExamUserId(examUser.getExamId());
        vehicleDocumentInfo.setPaperId(examUser.getPaperId());
        List<VehicleDocumentInfo> vehicleDocumentInfoList = vehicleDocumentInfoService.findList(vehicleDocumentInfo);
        appraisalJobTableVO.setVehicleDocumentInfoList(vehicleDocumentInfoList);

        //车辆配置全表
        VehicleInfo vehicleInfo = new VehicleInfo();
        vehicleInfo.setChexingId(carInfo.getModel());
        vehicleInfo = vehicleInfoService.getVehicleInfo(vehicleInfo);
        carInfo.setBrand(vehicleInfo.getPinpai());      //设置品牌
        appraisalJobTableVO.setVehicleInfo(vehicleInfo);


        //检查车体骨架
        CheckBodySkeleton checkBodySkeleton = new CheckBodySkeleton();
        checkBodySkeleton.setExamUserId(examUser.getExamId());
        checkBodySkeleton.setPaperId(examUser.getPaperId());
        List<CheckBodySkeleton> checkBodySkeletonList = checkBodySkeletonService.findList(checkBodySkeleton);
        appraisalJobTableVO.setCheckBodySkeletonList(checkBodySkeletonList);







        //设置年款型号
        String[] chexingArr = vehicleInfo.getChexingmingcheng().split(" ");
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < chexingArr.length; i++){
            sb.append(chexingArr[i]);
        }
        appraisalJobTableVO.setNiankuanxinghao(sb.toString());

        return appraisalJobTableVO;
    }

}
