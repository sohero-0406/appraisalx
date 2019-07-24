package com.jeesite.modules.aa.service;


import com.jeesite.modules.aa.entity.CarInfo;
import com.jeesite.modules.aa.entity.VehicleInstallInfo;
import com.jeesite.modules.aa.vo.AppraisalJobTableVO;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.VehicleInfoService;
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
     * 生成鉴定评估作业本
     */
    public AppraisalJobTableVO findAppraisalJobTable(ExamUser examUser) {
        AppraisalJobTableVO appraisalJobTableVO = new AppraisalJobTableVO();

        //委托车辆信息
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getExamId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInforService.getByEntity(carInfo);
        appraisalJobTableVO.setCarInfo(carInfo);

        //车辆加装信息
        VehicleInstallInfo vehicleInstallInfo = new VehicleInstallInfo();
        vehicleInstallInfo.setExamUserId(examUser.getExamId());
        vehicleInstallInfo.setPaperId(examUser.getPaperId());
        List<VehicleInstallInfo> vehicleInstallInfoList = vehicleInstallInfoService.findList(vehicleInstallInfo);

        //

        return appraisalJobTableVO;
    }

}
