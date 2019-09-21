/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.service.*;
import com.jeesite.modules.common.dao.ExamUserDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.vo.TimingVO;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * common_exam_userService
 *
 * @author lvchangwei
 * @version 2019-06-27
 */
@Service
@Transactional(readOnly = true)
public class ExamUserService extends CrudService<ExamUserDao, ExamUser> {

    @Autowired
    private ExamService examService;


    @Autowired
    private ExamScoreClassifyService examScoreClassifyService;

    // 委托方信息Service
    @Autowired
    private DelegateUserService delegateUserService;
    //委托车辆信息Service
    @Autowired
    private CarInfoService carInfoService;
    //车辆单证信息Service
    @Autowired
    private VehicleDocumentInfoService vehicleDocumentInfoService;
    //检查可交易车辆Service
    @Autowired
    private CheckTradableVehiclesService checkTradableVehiclesService;
    //车辆加装信息
    @Autowired
    private VehicleInstallInfoService vehicleInstallInfoService;
    //鉴定技术情况
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    //计算车辆价值Service
    @Autowired
    private CalculateService calculateService;
    //归档
    @Autowired
    private PlaceFileService placeFileService;
    //学生成绩详情表Service
    @Autowired
    private ExamResultsDetailService examResultsDetailService;
    @Autowired
    private PictureTypeService pictureTypeService;
    //调用大平台--学生数据
    @Autowired
    private HttpClientService httpClientService;
    @Autowired
    private PictureUserService pictureUserService;


    /**
     * 获取单条数据
     *
     * @param examUser
     * @return
     */
    @Override
    public ExamUser get(ExamUser examUser) {
        return super.get(examUser);
    }

    /**
     * 查询分页数据
     *
     * @param examUser 查询条件
     * @return
     */
    @Override
    public Page<ExamUser> findPage(ExamUser examUser) {
        return super.findPage(examUser);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param examUser
     */
    @Override
    @Transactional(readOnly = false)
    public void save(ExamUser examUser) {
        super.save(examUser);
    }

    /**
     * 更新状态
     *
     * @param examUser
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(ExamUser examUser) {
        super.updateStatus(examUser);
    }

    /**
     * 删除数据
     *
     * @param examUser
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(ExamUser examUser) {
        super.delete(examUser);
    }

    public ExamUser getByEntity(ExamUser examUser) {
        return dao.getByEntity(examUser);
    }

    //查看 考试成绩列表依据考试id
    @Transactional(readOnly = false)
    public List<ExamUser> getExamUserScoreList(String examId) {
        List<ExamUser> examUserList = dao.getExamUserScoreList(examId);
        CommonResult comRes = new CommonResult();
        if (CollectionUtils.isNotEmpty(examUserList)) {
            StringBuilder stringBuilder = new StringBuilder();
            int len = examUserList.size();
            for (int i = 0; i < len; i++) {
                if (i == len - 1) {
                    stringBuilder.append(examUserList.get(i).getUserId());
                } else {
                    stringBuilder.append(examUserList.get(i).getUserId() + ",");
                }
            }
            Map<String, String> map = new HashMap<>();
            map.put("ids", stringBuilder.toString());
            comRes = httpClientService.post(ServiceConstant.DERIVE_STUDENT_ACHIEVEMENT, map);
            //请求成功
            if (CodeConstant.REQUEST_SUCCESSFUL.equals(comRes.getCode())) {
                JSONArray array = JSONArray.parseArray(httpClientService.post(ServiceConstant.DERIVE_STUDENT_ACHIEVEMENT, map).getData().toString());
                if (CollectionUtils.isNotEmpty(array)) {
                    for (ExamUser user : examUserList) {
                        for (Object o : array) {
                            JSONObject userJson = (JSONObject) o;
                            //如果考生的userid等于大平台的学生id 数据匹配
                            if (user.getUserId().equals(userJson.getString("id"))) {
                                user.setUserNum(userJson.getString("userName"));
                                user.setTrueName(userJson.getString("trueName"));
                                break;
                            }

                        }
                    }
                }
            }


        }
        return examUserList;
    }

    @Transactional
    public void saveExamEndTime(String examId) {
        //依据考试id 在考试结束时 给未结束考试的考生添加结束考试时间
        dao.updateExamUserEndTime(examId);
    }


    //考生删除
    @Transactional
    public CommonResult deletExamUser(String examUserIdListJson) {
        CommonResult comRes = new CommonResult();
        examUserIdListJson = examUserIdListJson.replace("\n", "");
        examUserIdListJson = examUserIdListJson.replace(" ", "");
        JSONArray jsonArray = JSONObject.parseArray(examUserIdListJson);
        for (Object o : jsonArray) {
            JSONObject examUser = (JSONObject) o;
            String examUserId = examUser.getString("examUserId");
            if (StringUtils.isBlank(examUserId)) {
                comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
                comRes.setMsg("请先选择考生！");
                return comRes;
            }
            dao.deleteExamUser(examUserId);
        }
        comRes.setMsg("删除成功！");
        return comRes;
    }


    //依据考试id和评分项id查询考试想分类id
    public String getScoreClassifyId(String examId, String scoreInfoId) {
        ExamScoreClassify examScoreClassify = new ExamScoreClassify();
        examScoreClassify.setExamId(examId);
        examScoreClassify.setExamScoreInfoId(scoreInfoId);
        examScoreClassify = examScoreClassifyService.getByEntity(examScoreClassify);
        return examScoreClassify.getId();
    }

    /**
     * 学生考试成绩详情接口
     */
    public ExamResultsDetail saveExamDetail(String examUserId, String examId, String scoreInfoId, String scorePoints,
                                            String score, String scoreInfo, String teacherAnswer, String studentAnswer, String righrOrWrong) {
        ExamResultsDetail examResultsDetail = new ExamResultsDetail();
        examResultsDetail.setExamUserId(examUserId);
        examResultsDetail.setScoreClassifyId(getScoreClassifyId(examId, scoreInfoId));
        examResultsDetail.setScorePoints(scorePoints);
        examResultsDetail.setScore(score);
        examResultsDetail.setScoreInfo(scoreInfo);
        examResultsDetail.setStudentAnswer(studentAnswer);
        examResultsDetail.setTeacherAnswer(teacherAnswer);
        examResultsDetail.setRightOrWrong(righrOrWrong);
        examResultsDetailService.save(examResultsDetail);
        return examResultsDetail;
    }

    /**
     * 判卷
     */
    @Transactional(readOnly = false)
    public Object gradePapers(Exam exam) {
        //页面获取考试id  examId
        String examId = exam.getId();
        String paperId = exam.getPaperId(); //页面获取数据

        //创建  分数项--分数
        Map<String, Object> examScoreMap = new HashMap<>();
        //创建  分数项--名称
        Map<String, Object> examNameMap = new HashMap<>();
        //依据考试 获取所有的评分项分类以及评分项
        List<Map<String, Object>> examScoreItems = examScoreClassifyService.getExamScoreInfo(examId);
        //处理评分项 封成一组map 将考试分值项
        for (Map<String, Object> score : examScoreItems) {
            examScoreMap.put((String) score.get("score_info_id"), score.get("score"));
            examNameMap.put((String) score.get("score_info_id"), score.get("name"));
        }

        DelegateUser delegateUserTec = new DelegateUser();  //委托人
        CarInfo carInfoTec = new CarInfo();                 //委托车辆
        VehicleDocumentInfo vehicleDocumentInfoTec = new VehicleDocumentInfo(); // 车辆单证信息
        CheckTradableVehicles checkTradableVehiclesTec = new CheckTradableVehicles(); //检查可交易车辆
        VehicleInstallInfo vehicleInstallInfoTec = new VehicleInstallInfo(); //车辆加装信息
        VehicleGradeAssess vehicleGradeAssessTec = new VehicleGradeAssess();
        Calculate calculateTec = new Calculate();//计算车辆价值
        PlaceFile placeFileTec = new PlaceFile();


        delegateUserTec.setPaperId(paperId);
        carInfoTec.setPaperId(paperId);
        vehicleDocumentInfoTec.setPaperId(paperId);
        checkTradableVehiclesTec.setPaperId(paperId);
        vehicleInstallInfoTec.setPaperId(paperId);
        vehicleGradeAssessTec.setPaperId(paperId);
        calculateTec.setPaperId(paperId);
        placeFileTec.setPaperId(paperId);


        //试卷 答案(老师)
        //一、委托方
        DelegateUser delegateUserT = delegateUserService.getByEntity(delegateUserTec);
        CarInfo carInfoT = carInfoService.getByEntity(carInfoTec);
        //二、将 判别可交易车辆内数据整理到Map内
        List<VehicleDocumentInfo> vehicleDocumentInfoListT = vehicleDocumentInfoService.findList(vehicleDocumentInfoTec);
        CheckTradableVehicles checkTradableVehiclesT = checkTradableVehiclesService.getByEntity(checkTradableVehiclesTec);
        //三、记录车辆基本信息
        List<VehicleInstallInfo> vehicleInstallInfoListT = vehicleInstallInfoService.findList(vehicleInstallInfoTec);
        //四、
        //五、鉴定技术状况
        VehicleGradeAssess vehicleGradeAssessT = vehicleGradeAssessService.getByEntity(vehicleGradeAssessTec);
        //六、估算汽车价值
        Calculate calculateT = calculateService.getByEntity(calculateTec);
        List<String> placeFileListTecList = placeFileService.getFileByAssessedPicture(paperId); //归档
        List<String> movingPictureTec = new ArrayList<>(); //车辆行驶证
        List<String> registrationPictureTec = new ArrayList<>();//机动车登记证书
        List<String> idenPictureTec = new ArrayList<>(); //鉴定评估二手车照片
        for (String typePicture : placeFileListTecList) {
            //车辆行驶证
            if ("1143432856340893696".equals(typePicture) || "1143435061324763136".equals(typePicture)) {
                movingPictureTec.add(typePicture);
                //机动车登记证
            } else if ("1143435514869673984".equals(typePicture) || "1143435674886193152".equals(typePicture)) {
                registrationPictureTec.add(typePicture);
            } else {
                idenPictureTec.add(typePicture);
            }
        }

        //学生答题结果
        //根据考试获取 学生
        ExamUser examUser = new ExamUser();
        examUser.setExamId(examId);
        List<ExamUser> examUserList = dao.findList(examUser);
        //一、学生 --委托方
        DelegateUser delegateUserStu = new DelegateUser();
        CarInfo carInfoStu = new CarInfo();
        //二、学生判别车辆
        VehicleDocumentInfo vehicleDocumentInfoStu = new VehicleDocumentInfo();
        CheckTradableVehicles checkTradableVehiclesStu = new CheckTradableVehicles();
        //三、记录车辆基本信息
        VehicleInstallInfo vehicleInstallInfoStu = new VehicleInstallInfo(); //车辆加装信息
        //四、判别事故车

        //五、鉴定技术状况
        VehicleGradeAssess vehicleGradeAssessStu = new VehicleGradeAssess();
        //六、估算价值
        for (ExamUser user : examUserList) {
            //一、委托人
            delegateUserStu.setExamUserId(user.getId());
            DelegateUser delegateUserS = delegateUserService.getByEntity(delegateUserStu);
            carInfoStu.setExamUserId(user.getId());
            CarInfo carInfoS = carInfoService.getByEntity(carInfoStu);
            BigDecimal delegateCount = getDelegateInfo(delegateUserS, carInfoS, delegateUserT, carInfoT, examScoreMap, user, examNameMap, paperId);

            //二、判别可交易车辆
            vehicleDocumentInfoStu.setExamUserId(user.getId());
            List<VehicleDocumentInfo> vehicleDocumentInfoListS = vehicleDocumentInfoService.findList(vehicleDocumentInfoStu);
            checkTradableVehiclesStu.setExamUserId(user.getId());
            CheckTradableVehicles checkTradableVehiclesS = checkTradableVehiclesService.getByEntity(checkTradableVehiclesStu);
            BigDecimal vehicleDocumentCount = getVehicleDocumentInfo(vehicleDocumentInfoListS, vehicleDocumentInfoListT, examScoreMap, checkTradableVehiclesS, checkTradableVehiclesT, user, examNameMap);

            //三、记录车辆基本信息
            vehicleInstallInfoStu.setExamUserId(user.getId());
            List<VehicleInstallInfo> vehicleInstallInfoListS = vehicleInstallInfoService.findList(vehicleInstallInfoStu);
            BigDecimal carInfoCount = getCarInfo(carInfoT, carInfoS, examScoreMap, vehicleInstallInfoListT, vehicleInstallInfoListS, user, examNameMap, paperId);

//			//四、判别事故车
            BigDecimal accidentCount = getAccidentVehicles(checkTradableVehiclesT, checkTradableVehiclesS, examScoreMap, user, examNameMap);
//			//五、鉴定技术状况
            vehicleGradeAssessStu.setExamUserId(user.getId());
            VehicleGradeAssess vehicleGradeAssessS = vehicleGradeAssessService.getByEntity(vehicleGradeAssessStu);
            BigDecimal identificationCount = getIdentification(vehicleGradeAssessT, vehicleGradeAssessS, examScoreMap, user, examNameMap);
//			//六、估算价值
            List<String> placeFileListS = placeFileService.getPlaceFileByStu(user.getId());

            BigDecimal calculateCount = getCalculate(calculateT, user, examScoreMap, checkTradableVehiclesT, checkTradableVehiclesS
                    , placeFileListS, examNameMap, movingPictureTec, registrationPictureTec, idenPictureTec);


            BigDecimal count = delegateCount.add(vehicleDocumentCount.add(carInfoCount.add(accidentCount.add(identificationCount.add(calculateCount)))));
            user.setScore(String.valueOf(count));
            super.save(user);
        }
        return null;
    }

    //六、估算价值

    /**
     * @param calculateT             老师-估算价值
     * @param user                   考生
     * @param examScoreMap           评分分数项
     * @param checkTradableVehiclesT 检查可交易车辆-老师
     * @param checkTradableVehiclesS 检查可交易车辆-学生
     * @param placeFileListS         归档-学生
     * @param movingPictureTec       车辆行驶证 -老师
     * @param registrationPictureTec 机动车登记证书 -老师
     * @param idenPictureTec         鉴定评估二手车照片 -老师
     * @return
     */
    public BigDecimal getCalculate(Calculate calculateT, ExamUser user, Map<String, Object> examScoreMap,
                                   CheckTradableVehicles checkTradableVehiclesT, CheckTradableVehicles checkTradableVehiclesS,
                                   List<String> placeFileListS, Map<String, Object> examNameMap,
                                   List<String> movingPictureTec, List<String> registrationPictureTec, List<String> idenPictureTec) {
        BigDecimal calculateCount = new BigDecimal("0");
        //学生估算方式
        String studentPrice = null;
        Map<String, String> calculateMap = calculateService.getEstimateByType(user.getId(), "");
        if (calculateMap != null) {
            studentPrice = calculateMap.get("price");
        }
        if (calculateT.getBeginPrice() != null && calculateT.getEndPrice() != null) {
            if (judgeStringBetween(String.valueOf(calculateT.getBeginPrice()), String.valueOf(calculateT.getEndPrice()), studentPrice)) {
                calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665733999"))));
                calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665739992"))));
                saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                        (String) examNameMap.get("1151013343665733999"), (String) examScoreMap.get("1151013343665733999"), (String) examScoreMap.get("1151013343665733999"),
                        calculateT.getBeginPrice() + "-" + calculateT.getEndPrice() + "元",
                        studentPrice + "元", "0");
                saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                        (String) examNameMap.get("1151013343665739992"), (String) examScoreMap.get("1151013343665739992"), (String) examScoreMap.get("1151013343665739992"),
                        calculateT.getBeginPrice() + "-" + calculateT.getEndPrice() + "元",
                        studentPrice + "元", "0");
            } else {
                saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                        (String) examNameMap.get("1151013343665733999"), "0", (String) examScoreMap.get("1151013343665733999"),
                        calculateT.getBeginPrice() + "-" + calculateT.getEndPrice() + "元",
                        studentPrice == null ? "" : (studentPrice + "元"), "1");
                saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                        (String) examNameMap.get("1151013343665739992"), "0", (String) examScoreMap.get("1151013343665739992"),
                        calculateT.getBeginPrice() + "-" + calculateT.getEndPrice() + "元",
                        studentPrice == null ? "" : (studentPrice + "元"), "1");
            }
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343665733999"), "0", (String) examScoreMap.get("1151013343665733999"),
                    calculateT.getBeginPrice() + "-" + calculateT.getEndPrice() + "元",
                    studentPrice + "元", "1");
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343665739992"), "0", (String) examScoreMap.get("1151013343665739992"),
                    calculateT.getBeginPrice() + "-" + calculateT.getEndPrice() + "元",
                    studentPrice + "元", "1");
        }

        //是否查封、抵押车辆
        if (StringUtils.isNotBlank(checkTradableVehiclesT.getCheck3()) && checkTradableVehiclesS != null && checkTradableVehiclesT.getCheck3().equals(checkTradableVehiclesS.getCheck3())) {
            calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663128577"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343663128577"),
                    (String) examScoreMap.get("1151013343663128577"), (String) examScoreMap.get("1151013343663128577"),
                    getYesOrNo(checkTradableVehiclesT.getCheck3()),
                    getYesOrNo(checkTradableVehiclesS.getCheck3()),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343663128577"), "0", (String) examScoreMap.get("1151013343663128577"),
                    getYesOrNo(checkTradableVehiclesT.getCheck3()),
                    checkTradableVehiclesS == null ? "" : getYesOrNo(checkTradableVehiclesS.getCheck3()),
                    "1");
        }
        //未接受处理的交通违法记录：
        if (StringUtils.isNotBlank(checkTradableVehiclesT.getTrafficIllegalRecord()) && checkTradableVehiclesS != null && checkTradableVehiclesT.getTrafficIllegalRecord().equals(checkTradableVehiclesS.getTrafficIllegalRecord())) {
            calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343662706689"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343662706689"), (String) examScoreMap.get("1151013343662706689"), (String) examScoreMap.get("1151013343662706689"),
                    getYesOrNo(checkTradableVehiclesT.getTrafficIllegalRecord()),
                    getYesOrNo(checkTradableVehiclesS.getTrafficIllegalRecord()),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343662706689"), "0", (String) examScoreMap.get("1151013343662706689"),
                    getYesOrNo(checkTradableVehiclesT.getTrafficIllegalRecord()),
                    checkTradableVehiclesS == null ? "" : getYesOrNo(checkTradableVehiclesS.getTrafficIllegalRecord()),
                    "1");
        }
        //车辆鉴定评估价值为人民币（元）   1151013343665739992


        //归档时效
        if (StringUtils.isNotBlank(checkTradableVehiclesT.getFileDuring()) && checkTradableVehiclesS != null && checkTradableVehiclesT.getFileDuring().equals(checkTradableVehiclesS.getFileDuring())) {
            calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151028180617695233"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151028180617695233"), (String) examScoreMap.get("1151028180617695233"), (String) examScoreMap.get("1151028180617695233"),
                    checkTradableVehiclesT.getFileDuring(), checkTradableVehiclesS.getFileDuring(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151028180617695233"), "0", (String) examScoreMap.get("1151028180617695233"),
                    checkTradableVehiclesT.getFileDuring(), checkTradableVehiclesS == null ? "" : checkTradableVehiclesS.getFileDuring(), "1");
        }
        //归档
        List<String> removeList = new ArrayList<>();
        //选中三种证书 等分
        // 二手车鉴定评估委托书
        ExamResultsDetail appraisal = saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                (String) examNameMap.get("1151013343670108155"), "0", (String) examScoreMap.get("1151013343670108155"),
                "二手车鉴定评估委托书", "", "1");
        //二手车鉴定评估作业表
        ExamResultsDetail evaluation = saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                (String) examNameMap.get("1151013343670108567"), "0", (String) examScoreMap.get("1151013343670108567"),
                "二手车鉴定评估作业表", "", "1");
        //二手车鉴定评估报告
        ExamResultsDetail report = saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                (String) examNameMap.get("1151013343670108787"), "0", (String) examScoreMap.get("1151013343670108787"),
                "二手车鉴定评估报告", "", "1");

        for (String pictureTypeS : placeFileListS) {
            //判断 二手车鉴定评估委托书
            if ("1152466716125380608".equals(pictureTypeS)) {
                calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343670108155"))));
                appraisal.setStudentAnswer("二手车鉴定评估委托书");
                appraisal.setScore((String) examScoreMap.get("1151013343670108155"));
                appraisal.setRightOrWrong("0");
                examResultsDetailService.update(appraisal);
                //二手车鉴定评估作业表
            } else if ("1152467065519292416".equals(pictureTypeS)) {
                calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343670108567"))));
                evaluation.setStudentAnswer("二手车鉴定评估作业表");
                evaluation.setScore((String) examScoreMap.get("1151013343670108567"));
                evaluation.setRightOrWrong("0");
                examResultsDetailService.update(evaluation);
                //二手车鉴定评估报告
            } else if ("1152467158926442496".equals(pictureTypeS)) {
                calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343670108787"))));
                report.setStudentAnswer("二手车鉴定评估报告");
                report.setScore((String) examScoreMap.get("1151013343670108787"));
                report.setRightOrWrong("0");
                examResultsDetailService.update(report);
            }
        }
        removeList.add("1152466716125380608");
        removeList.add("1152467065519292416");
        removeList.add("1152467158926442496");

        //学生所选择的 机动车行驶证页面
        if (placeFileListS.containsAll(movingPictureTec) && (CollectionUtils.isNotEmpty(movingPictureTec))) {
            //正确
            calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343670102345"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343670102345"), (String) examScoreMap.get("1151013343670102345"), (String) examScoreMap.get("1151013343670102345"),
                    getPictureName(movingPictureTec), getPictureName(movingPictureTec), "0");
        } else {
            //错误
            List<String> drivingListS = new ArrayList<>();
            drivingListS.addAll(placeFileListS);
            drivingListS.retainAll(movingPictureTec);
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343670102345"), "0", (String) examScoreMap.get("1151013343670102345"),
                    getPictureName(movingPictureTec), getPictureName(drivingListS), "1");
        }
        removeList.add("1143432856340893696");
        removeList.add("1143435061324763136");


        //判断 登记证 1151013343670102898
        if (placeFileListS.containsAll(registrationPictureTec) && (CollectionUtils.isNotEmpty(registrationPictureTec))) {
            //正确
            calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343670102898"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343670102898"), (String) examScoreMap.get("1151013343670102898"), (String) examScoreMap.get("1151013343670102898"),
                    getPictureName(registrationPictureTec), getPictureName(registrationPictureTec), "0");
        } else {
            //错误
            List<String> registrationListS = new ArrayList<>();
            registrationListS.addAll(placeFileListS);
            registrationListS.retainAll(registrationPictureTec);
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343670102898"), "0", (String) examScoreMap.get("1151013343670102898"),
                    getPictureName(registrationPictureTec), getPictureName(registrationListS), "1");
        }
        removeList.add("1143435514869673984");
        removeList.add("1143435674886193152");
        placeFileListS.removeAll(removeList);
        //判断 鉴定评估二手车照片  如果正确 则学生所选全部包含老师所选的
        if (placeFileListS.containsAll(idenPictureTec) && (CollectionUtils.isNotEmpty(idenPictureTec))) {
            calculateCount = calculateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343670108678"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343670108678"), (String) examScoreMap.get("1151013343670108678"), (String) examScoreMap.get("1151013343670108678"),
                    getPictureName(idenPictureTec), getPictureName(placeFileListS), "0");

        } else {
            //不全部包含 不得分
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343670108678"), "0", (String) examScoreMap.get("1151013343670108678"),
                    getPictureName(idenPictureTec), getPictureName(placeFileListS), "1");
        }

        return calculateCount;
    }


    //依据所选的照片或者文档id 转换为中文名称
    public String getPictureName(List<String> pictureTypeId) {
        if (CollectionUtils.isEmpty(pictureTypeId)) {
            return "";
        } else {
            StringBuilder picturename = new StringBuilder();
            String[] pictureType = pictureTypeId.toArray(new String[pictureTypeId.size()]);
            List<PictureType> pictureTypeList = pictureTypeService.findListByIds(pictureType);
            int len = pictureTypeList.size();
            for (int i = 0; i < pictureTypeList.size(); i++) {
                if (i == (len - 1)) {
                    picturename.append(pictureTypeList.get(i).getName());
                } else {
                    picturename.append(pictureTypeList.get(i).getName() + ",");
                }
            }
            return picturename.toString();
        }
    }

    ;


    //五、鉴定技术状况
    public BigDecimal getIdentification(VehicleGradeAssess vehicleGradeAssessT, VehicleGradeAssess vehicleGradeAssessS,
                                        Map<String, Object> examScoreMap, ExamUser user, Map<String, Object> examNameMap) {
        BigDecimal identificationCount = new BigDecimal(0);
        //老师答案不为空
        if (vehicleGradeAssessS != null && this.judgeStringBetween(vehicleGradeAssessT.getStartScore(), vehicleGradeAssessT.getEndScore(), vehicleGradeAssessS.getScore())) {
            identificationCount = identificationCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343662993409")))); //车辆鉴定得分值（区间值）
            saveExamDetail(user.getId(), user.getExamId(), "1151028180616400897",
                    (String) examNameMap.get("1151013343662993409"), (String) examScoreMap.get("1151013343662993409"), (String) examScoreMap.get("1151013343662993409"),
                    vehicleGradeAssessT.getStartScore() + "-" + vehicleGradeAssessT.getEndScore() + "分",
                    vehicleGradeAssessS.getScore() + "分", "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180616400897",
                    (String) examNameMap.get("1151013343662993409"), "0", (String) examScoreMap.get("1151013343662993409"),
                    vehicleGradeAssessT.getStartScore() + "-" + vehicleGradeAssessT.getEndScore() + "分",
                    vehicleGradeAssessS == null ? "" : (vehicleGradeAssessS.getStartScore() == null ? "" : vehicleGradeAssessS.getStartScore() + "分"), "1");

        }
        //鉴定日期
        if (StringUtils.isNotBlank(vehicleGradeAssessT.getIdentifyDate()) && (null != vehicleGradeAssessS) && vehicleGradeAssessT.getIdentifyDate().equals(vehicleGradeAssessS.getIdentifyDate())) {
            identificationCount = identificationCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151028180615864562")))); //鉴定日期
            saveExamDetail(user.getId(), user.getExamId(), "1151028180616400897",
                    (String) examNameMap.get("1151028180615864562"), (String) examScoreMap.get("1151028180615864562"), (String) examScoreMap.get("1151028180615864562"),
                    vehicleGradeAssessT.getIdentifyDate(), vehicleGradeAssessS.getIdentifyDate(), "0");
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343665541121"), (String) examScoreMap.get("1151013343665541121"), (String) examScoreMap.get("1151013343665541121"),
                    setTimePlusNinetydays(vehicleGradeAssessT.getIdentifyDate()),
                    setTimePlusNinetydays(vehicleGradeAssessS.getIdentifyDate()),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180616400897",
                    (String) examNameMap.get("1151028180615864562"), "0", (String) examScoreMap.get("1151028180615864562"),
                    vehicleGradeAssessT.getIdentifyDate(),
                    vehicleGradeAssessS == null ? "" : vehicleGradeAssessS.getIdentifyDate(),
                    "1");
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615860225",
                    (String) examNameMap.get("1151013343665541121"), "0", (String) examScoreMap.get("1151013343665541121"),
                    setTimePlusNinetydays(vehicleGradeAssessT.getIdentifyDate()),
                    vehicleGradeAssessS == null ? "" : setTimePlusNinetydays(vehicleGradeAssessS.getIdentifyDate())
                    , "1");
        }
        return identificationCount;
    }

    //时间加上九十天
    public String setTimePlusNinetydays(String day) {
        if (StringUtils.isNotBlank(day)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date baseDate = sdf.parse(day);
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(baseDate);
                calendar.add(calendar.DATE, 90);
                baseDate = calendar.getTime();
                return sdf.format(baseDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    //判断 某字符串类型 在某字符串类型区间 并判断是或否为空
    //中间值 大于等于开始值 小于等于结束值
    public Boolean judgeStringBetween(String begin, String end, String middle) {
        if (StringUtils.isNotBlank(begin) && StringUtils.isNotBlank(end) && StringUtils.isNotBlank(middle)) {
            BigDecimal middleNumber = new BigDecimal(middle);
            BigDecimal beginNumber = new BigDecimal(begin);
            BigDecimal endNumber = new BigDecimal(end);
            if (middleNumber.compareTo(beginNumber) > -1 && middleNumber.compareTo(endNumber) < 1) {
                return true;
            }
        }
        return false;
    }


    //四、判别事故车

    /**
     * @param checkTradableVehiclesT 教师-检查可交易车辆
     * @param checkTradableVehiclesS 学生-检查可交易车辆
     * @param examScoreMap           分数项分值
     * @return
     */
    public BigDecimal getAccidentVehicles(CheckTradableVehicles checkTradableVehiclesT, CheckTradableVehicles checkTradableVehiclesS,
                                          Map<String, Object> examScoreMap, ExamUser user, Map<String, Object> examNameMap) {
        BigDecimal accidentCount = new BigDecimal(0);
        if (StringUtils.isNotBlank(checkTradableVehiclesT.getIsAccident()) && (null != checkTradableVehiclesS) && checkTradableVehiclesT.getIsAccident().equals(checkTradableVehiclesS.getIsAccident())) {
            accidentCount = accidentCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664201729"))));  //判定事故车选择正确
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615991297",
                    (String) examNameMap.get("1151013343664201729"), (String) examScoreMap.get("1151013343664201729"), (String) examScoreMap.get("1151013343664201729"),
                    getIsAccidentName(checkTradableVehiclesT.getIsAccident()), getIsAccidentName(checkTradableVehiclesS.getIsAccident()), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180615991297",
                    (String) examNameMap.get("1151013343664201729"), "0", (String) examScoreMap.get("1151013343664201729"),
                    getIsAccidentName(checkTradableVehiclesT.getIsAccident()), getIsAccidentName(checkTradableVehiclesS == null ? "" : checkTradableVehiclesS.getIsAccident()), "1");
        }
        return accidentCount;
    }

    //判断是否为事故车 0 正常车，1 非正常车
    public String getIsAccidentName(String isAccident) {
        if (StringUtils.isNotBlank(isAccident)) {
            if ("0".equals(isAccident)) {
                return "正常车";
            } else if ("1".equals(isAccident)) {
                return "事故车";
            }
        }
        return "";
    }


    //根据车辆信息调取大平台数据 (品牌 成型id)  Brand Model
    public String getCarModelByPlatform(String id) {
        String name = "";
        if (StringUtils.isBlank(id)) {
            return name;
        }
        Map<String, String> map = new HashMap<>();
        map.put("chexingId", id);
        CommonResult comRes = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(comRes.getCode())) {
            return name;
        }
        if (null == comRes.getData()) {
            return name;
        }
        name = ((JSONObject) comRes.getData()).getString("chexingmingcheng");
        return name;
    }


    //根据车辆信息调取大平台数据 (品牌 成型id)  Brand Model
    public String getCarModelByPlatform(String id, String pathName, String inParameter, String outParameter) {
        String name = "";
        if (StringUtils.isBlank(id)) {
            return name;
        }
        Map<String, String> map = new HashMap<>();
        map.put(inParameter, id);
        CommonResult comRes = httpClientService.post(pathName, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(comRes.getCode())) {
            return name;
        }
        if (null == comRes.getData()) {
            return name;
        }
        name = ((JSONObject) comRes.getData()).getString(outParameter);
        return name;
    }


    //三、记录车辆基本信息
    public BigDecimal getCarInfo(CarInfo carInfoT, CarInfo carInfoS, Map<String, Object> examScoreMap, List<VehicleInstallInfo> vehicleInstallInfoListT,
                                 List<VehicleInstallInfo> vehicleInstallInfoListS, ExamUser user, Map<String, Object> examNameMap, String paperId) {
        BigDecimal carInfoCount = new BigDecimal(0);
        //品牌加车系
        if (StringUtils.isNotBlank(carInfoT.getBrand()) && StringUtils.isNotBlank(carInfoT.getSeries()) && (null != carInfoS) &&
                carInfoT.getBrand().equals(carInfoS.getBrand()) && carInfoT.equals(carInfoS.getSeries())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664197633")))); //品牌
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664197633"), (String) examScoreMap.get("1151013343664197633"), (String) examScoreMap.get("1151013343664197633"),
                    getCarModelByPlatform(carInfoT.getBrand(), ServiceConstant.COMMON_VEHICLE_BRAND_GET_BY_ENTITY, "pinpaiId", "pinpai") +
                            getCarModelByPlatform(carInfoT.getSeries(), ServiceConstant.COMMON_VEHICLE_SERIES_GET_BY_ENTITY, "chexiId", "chexi"),
                    getCarModelByPlatform(carInfoS.getBrand(), ServiceConstant.COMMON_VEHICLE_BRAND_GET_BY_ENTITY, "pinpaiId", "pinpai") +
                            getCarModelByPlatform(carInfoS.getSeries(), ServiceConstant.COMMON_VEHICLE_SERIES_GET_BY_ENTITY, "chexiId", "chexi"),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664197633"), "0", (String) examScoreMap.get("1151013343664197633"),
                    getCarModelByPlatform(carInfoT.getBrand(), ServiceConstant.COMMON_VEHICLE_BRAND_GET_BY_ENTITY, "pinpaiId", "pinpai") +
                            getCarModelByPlatform(carInfoT.getSeries(), ServiceConstant.COMMON_VEHICLE_SERIES_GET_BY_ENTITY, "chexiId", "chexi"),
                    carInfoS == null ? "" :
                            getCarModelByPlatform(carInfoS.getBrand(), ServiceConstant.COMMON_VEHICLE_BRAND_GET_BY_ENTITY, "pinpaiId", "pinpai") +
                                    getCarModelByPlatform(carInfoS.getSeries(), ServiceConstant.COMMON_VEHICLE_SERIES_GET_BY_ENTITY, "chexiId", "chexi"),
                    "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getModel()) && (null != carInfoS) && carInfoT.getModel().equals(carInfoS.getModel())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664799745")))); //车型
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664799745"), (String) examScoreMap.get("1151013343664799745"), (String) examScoreMap.get("1151013343664799745"),
                    getCarModelByPlatform(carInfoT.getModel(), ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, "chexingId", "chexingmingcheng"),
                    getCarModelByPlatform(carInfoS.getModel(), ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, "chexingId", "chexingmingcheng"),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664799745"), "0", (String) examScoreMap.get("1151013343664799745"),
                    getCarModelByPlatform(carInfoT.getModel(), ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, "chexingId", "chexingmingcheng"),
                    carInfoS == null ? "" :
                            getCarModelByPlatform(carInfoS.getModel(), ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, "chexingId", "chexingmingcheng"),
                    "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getLevel()) && (null != carInfoS) && carInfoT.getLevel().equals(carInfoS.getLevel())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664439297")))); //级别
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664439297"), (String) examScoreMap.get("1151013343664439297"), (String) examScoreMap.get("1151013343664439297"),
                    getDic("aa_vehicle_level", carInfoT.getLevel()), getDic("aa_vehicle_level", carInfoS.getLevel())
                    , "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664439297"), "0", (String) examScoreMap.get("1151013343664439297"),
                    getDic("aa_vehicle_level", carInfoT.getLevel()),
                    carInfoS == null ? "" : getDic("aa_vehicle_level", carInfoS.getLevel()), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getManufactureDate()) && (null != carInfoS) && carInfoT.getManufactureDate().equals(carInfoS.getManufactureDate())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665321234")))); //出厂日期
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343665321234"), (String) examScoreMap.get("1151013343665321234"), (String) examScoreMap.get("1151013343665321234"),
                    carInfoT.getManufactureDate(), carInfoS.getManufactureDate(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343665321234"), "0", (String) examScoreMap.get("1151013343665321234"),
                    carInfoT.getManufactureDate(), carInfoS == null ? "" : carInfoS.getManufactureDate(), "1");
        }
        if ((null != carInfoS) && carInfoT.getChangeNum().equals(carInfoS.getChangeNum())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663411201")))); //过户次数
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663411201"), (String) examScoreMap.get("1151013343663411201"), (String) examScoreMap.get("1151013343663411201"),
                    carInfoT.getChangeNum() == null ? "" : carInfoT.getChangeNum().toString(),
                    carInfoS.getChangeNum() == null ? "" : carInfoS.getChangeNum().toString(),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663411201"), "0", (String) examScoreMap.get("1151013343663411201"),
                    carInfoT.getChangeNum() == null ? "" : carInfoT.getChangeNum().toString(),
                    String.valueOf(carInfoS == null ? "" : (carInfoS.getChangeNum() == null ? "" : carInfoS.getChangeNum().toString())),
                    "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getEnvironmentalStandard()) && (null != carInfoS) && carInfoT.getEnvironmentalStandard().equals(carInfoS.getEnvironmentalStandard())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663699964")))); //环保标准
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663699964"), (String) examScoreMap.get("1151013343663699964"), (String) examScoreMap.get("1151013343663699964"),
                    getDic("aa_environmental_standard", carInfoT.getEnvironmentalStandard()),
                    getDic("aa_environmental_standard", carInfoS.getEnvironmentalStandard()), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663699964"), "0", (String) examScoreMap.get("1151013343663699964"),
                    getDic("aa_environmental_standard", carInfoT.getEnvironmentalStandard()),
                    carInfoS == null ? "" : getDic("aa_environmental_standard", carInfoS.getEnvironmentalStandard()), "1");
        }
        //车身结构 body_structure
        if (StringUtils.isNotBlank(carInfoT.getBodyStructure()) && (null != carInfoS) && carInfoT.getBodyStructure().equals(carInfoS.getBodyStructure())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665090561")))); //整备质量
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343665090561"), (String) examScoreMap.get("1151013343665090561"), (String) examScoreMap.get("1151013343665090561"),
                    carInfoT.getBodyStructure(), carInfoS.getBodyStructure(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343665090561"), "0", (String) examScoreMap.get("1151013343665090561"),
                    carInfoT.getBodyStructure(), carInfoS == null ? "" : carInfoS.getBodyStructure(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getCurbWeight()) && (null != carInfoS) && carInfoT.getCurbWeight().equals(carInfoS.getCurbWeight())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663931393")))); //整备质量
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663931393"), (String) examScoreMap.get("1151013343663931393"), (String) examScoreMap.get("1151013343663931393"),
                    carInfoT.getCurbWeight(), carInfoS.getCurbWeight(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663931393"), "0", (String) examScoreMap.get("1151013343663931393"),
                    carInfoT.getCurbWeight(), carInfoS == null ? "" : carInfoS.getCurbWeight(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getEngineModel()) && (null != carInfoS) && carInfoT.getEngineModel().equals(carInfoS.getEngineModel())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666056124")))); //发动机型号
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343666056124"), (String) examScoreMap.get("1151013343666056124"), (String) examScoreMap.get("1151013343666056124"),
                    carInfoT.getEngineModel(), carInfoS.getEngineModel(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343666056124"), "0", (String) examScoreMap.get("1151013343666056124"),
                    carInfoT.getEngineModel(), carInfoS == null ? "" : carInfoS.getEngineModel(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getEngineNum()) && (null != carInfoS) && carInfoT.getEngineNum().equals(carInfoS.getEngineNum())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666704366")))); //发动机号码
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343666704366"), (String) examScoreMap.get("1151013343666704366"), (String) examScoreMap.get("1151013343666704366"),
                    carInfoT.getEngineNum(), carInfoS.getEngineNum(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343666704366"), "0", (String) examScoreMap.get("1151013343666704366"),
                    carInfoT.getEngineNum(), carInfoS == null ? "" : carInfoS.getEngineNum(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getBodySize()) && (null != carInfoS) && carInfoT.getBodySize().equals(carInfoS.getBodySize())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666286593")))); //车身尺寸
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343666286593"), (String) examScoreMap.get("1151013343666286593"), (String) examScoreMap.get("1151013343666286593"),
                    carInfoT.getBodySize(), carInfoS.getBodySize(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343666286593"), "0", (String) examScoreMap.get("1151013343666286593"),
                    carInfoT.getBodySize(), carInfoS == null ? "" : carInfoS.getBodySize(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getVehicleWarranty()) && (null != carInfoS) && carInfoT.getVehicleWarranty().equals(carInfoS.getVehicleWarranty())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665496065")))); //整车质保
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343665496065"), (String) examScoreMap.get("1151013343665496065"), (String) examScoreMap.get("1151013343665496065"),
                    carInfoT.getVehicleWarranty(), carInfoS.getVehicleWarranty(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343665496065"), "0", (String) examScoreMap.get("1151013343665496065"),
                    carInfoT.getVehicleWarranty(), carInfoS == null ? "" : carInfoS.getVehicleWarranty(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getYearCheckDue()) && (null != carInfoS) && carInfoT.getYearCheckDue().equals(carInfoS.getYearCheckDue())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663656961")))); //年检到期
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663656961"), (String) examScoreMap.get("1151013343663656961"), (String) examScoreMap.get("1151013343663656961"),
                    carInfoT.getYearCheckDue(), carInfoS.getYearCheckDue(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343663656961"), "0", (String) examScoreMap.get("1151013343663656961"),
                    carInfoT.getYearCheckDue(), carInfoS == null ? "" : carInfoS.getYearCheckDue(), "1");
        }
        //定义开关
        Boolean flag = false;
        //车辆加装信息
        for (int i = 0; i < vehicleInstallInfoListT.size(); i++) {
            for (VehicleInstallInfo vehicleStu : vehicleInstallInfoListS) {
                if (StringUtils.isNotBlank(vehicleInstallInfoListT.get(i).getProject()) && vehicleInstallInfoListT.get(i).getProject().equals(vehicleStu.getProject())) {
                    flag = true;
                    break;
                }
            }
            if (i == (vehicleInstallInfoListT.size() - 1) && flag) {
            } else if (flag) {
                flag = false;
            } else {
                break;
            }
        }
        //判断加装信息 老师所选学生答案内全部包含 并且学生不能多选
        if (flag && (vehicleInstallInfoListT.size() == vehicleInstallInfoListS.size())) {
            carInfoCount = carInfoCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664160769")))); //车辆加装信息
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664160769"), (String) examScoreMap.get("1151013343664160769"), (String) examScoreMap.get("1151013343664160769"),
                    vehicleInstallInfoService.getVehicleInstallName(null, paperId), vehicleInstallInfoService.getVehicleInstallName(user.getId(), null), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617760769",
                    (String) examNameMap.get("1151013343664160769"), "0", (String) examScoreMap.get("1151013343664160769"),
                    vehicleInstallInfoService.getVehicleInstallName(null, paperId), vehicleInstallInfoService.getVehicleInstallName(user.getId(), null), "1");

        }
        return carInfoCount;
    }


    /**
     * 二 获取 判别可交易车辆分数
     *
     * @param vehicleDocumentInfoListS 判别可交易车辆学生类
     * @param vehicleDocumentInfoListT 判别可交易车辆 教师答案
     * @param examScoreMap             评分项分值
     * @return
     */
    public BigDecimal getVehicleDocumentInfo(List<VehicleDocumentInfo> vehicleDocumentInfoListS, List<VehicleDocumentInfo> vehicleDocumentInfoListT,
                                             Map<String, Object> examScoreMap, CheckTradableVehicles checkTradableVehiclesS,
                                             CheckTradableVehicles checkTradableVehiclesT, ExamUser user, Map<String, Object> examNameMap) {

        BigDecimal vehicleDocumentCount = new BigDecimal(0);
        for (VehicleDocumentInfo vehicleDocumentT : vehicleDocumentInfoListT) {
            //定义开关 判断学生是否存在未填数据
            Boolean flag = false;
            //学生所填 答案
            VehicleDocumentInfo vehicleDocument = null;
            for (VehicleDocumentInfo vehicleDocumentS : vehicleDocumentInfoListS) {
                //单证项目   是否包含所选项
                if (StringUtils.isNotBlank(vehicleDocumentT.getProject()) && vehicleDocumentT.getProject().equals(vehicleDocumentS.getProject())) {
                    flag = true;
                    vehicleDocument = vehicleDocumentS;
                    break;
                }
            }
            if (flag) {
                if (StringUtils.isNotBlank(vehicleDocumentT.getState()) && vehicleDocumentT.getState().equals(vehicleDocument.getState())) {
                    vehicleDocumentCount = vehicleDocumentCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())))));
                    saveExamDetail(user.getId(), user.getExamId(), "1151028180614815745",
                            (String) examNameMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),
                            (String) examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),
                            (String) examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),
                            getYesOrNo(vehicleDocumentT.getState()), getYesOrNo(vehicleDocument.getState()), "0");
                } else {
                    saveExamDetail(user.getId(), user.getExamId(), "1151028180614815745",
                            (String) examNameMap.get(getExamScoreInfo(vehicleDocumentT.getProject())), "0",
                            (String) examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),
                            getYesOrNo(vehicleDocumentT.getState()), getYesOrNo(vehicleDocument.getState()), "1");
                }
            } else {
                saveExamDetail(user.getId(), user.getExamId(), "1151028180614815745",
                        (String) examNameMap.get(getExamScoreInfo(vehicleDocumentT.getProject())), "0", (String) examScoreMap.get(getExamScoreInfo(vehicleDocumentT.getProject())),
                        getYesOrNo(vehicleDocumentT.getState()), "", "1");
            }


        }
        //判别是否为可交易车辆
        if (StringUtils.isNotBlank(checkTradableVehiclesT.getIsTrade()) && (null != checkTradableVehiclesS) && checkTradableVehiclesT.getIsTrade().equals(checkTradableVehiclesS.getIsTrade())) {
            vehicleDocumentCount = vehicleDocumentCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663693825"))));  //判别是否可交易
            saveExamDetail(user.getId(), user.getExamId(), "1151028180614815745",
                    (String) examNameMap.get("1151013343663693825"), (String) examScoreMap.get("1151013343663693825"), (String) examScoreMap.get("1151013343663693825"),
                    getYesOrNo(checkTradableVehiclesT.getIsTrade()),
                    getYesOrNo(checkTradableVehiclesS.getIsTrade()),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180614815745",
                    (String) examNameMap.get("1151013343663693825"), "0", (String) examScoreMap.get("1151013343663693825"),
                    getYesOrNo(checkTradableVehiclesT.getIsTrade()),
                    checkTradableVehiclesS == null ? "" : getYesOrNo(checkTradableVehiclesS.getIsTrade()),
                    "1");
        }
        return vehicleDocumentCount;
    }

    public String getYesOrNo(String state) {
        if (StringUtils.isNotBlank(state)) {
            if ("1".equals(state)) {
                return "是";
            } else {
                return "否";
            }
        } else {
            return "";
        }
    }

    /**
     * 获取对应的分值项
     *
     * @param project
     * @return
     */
    public String getExamScoreInfo(String project) {
        switch (project) {
            case "1":
                return "1151013343663300609"; //行驶证（正、副页）
            case "2":
                return "1151013343662862337"; //机动车登记证书
            case "3":
                return "1151013343665299457"; //车船使用税缴付凭证
            case "4":
                return "1151013343665225729"; //机动车交通事故责任强制保险单
            case "5":
                return "1151013343664046081"; //机动车综合商业保险保单
            case "6":
                return "1151013343664152577"; //年审检验合格标志（正反页）
            case "7":
                return "1151013343664046021"; //强制保险标志
            case "8":
                return "1151013343663435777"; //车辆购置税完税证明
            default:
        }
        return null;
    }


    /**
     * 委托人 模块
     *
     * @param delegateUserS 委托人-学生
     * @param carInfoS      委托车辆-学生
     * @param delegateUserT 委托人--老师答案
     * @param carInfoT      委托车辆-老师答案
     * @param examScoreMap  考试评分项分值
     * @return
     */
    public BigDecimal getDelegateInfo(DelegateUser delegateUserS, CarInfo carInfoS, DelegateUser delegateUserT,
                                      CarInfo carInfoT, Map<String, Object> examScoreMap, ExamUser user,
                                      Map<String, Object> examNameMap, String paperId) {
        BigDecimal delegateCount = new BigDecimal(0);
        //一、委托人

        if (StringUtils.isNotBlank(delegateUserT.getName()) && (null != delegateUserS) && delegateUserT.getName().equals(delegateUserS.getName())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664762880"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664762880"), (String) examScoreMap.get("1151013343664762880"), (String) examScoreMap.get("1151013343664762880"),
                    delegateUserT.getName(), delegateUserS.getName(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664762880"), "0", (String) examScoreMap.get("1151013343664762880"),
                    delegateUserT.getName(), delegateUserS == null ? "" : delegateUserS.getName(), "1");
        }
        if (StringUtils.isNotBlank(delegateUserT.getIdNum()) && (null != delegateUserS) && delegateUserT.getIdNum().equals(delegateUserS.getIdNum())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663427585"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663427585"), (String) examScoreMap.get("1151013343663427585"), (String) examScoreMap.get("1151013343663427585"),
                    delegateUserT.getIdNum(), delegateUserS.getIdNum(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663427585"), "0", (String) examScoreMap.get("1151013343663427585"),
                    delegateUserT.getIdNum(), delegateUserS == null ? "" : delegateUserS.getIdNum(), "1");
        }
        if (StringUtils.isNotBlank(delegateUserT.getAddress()) && (null != delegateUserS) && delegateUserT.getAddress().equals(delegateUserS.getAddress())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663116289"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663116289"), (String) examScoreMap.get("1151013343663116289"), (String) examScoreMap.get("1151013343663116289"),
                    delegateUserT.getAddress(), delegateUserS.getAddress(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663116289"), "0", (String) examScoreMap.get("1151013343663116289"),
                    delegateUserT.getAddress(), delegateUserS == null ? "" : delegateUserS.getAddress(), "1");
        }
        if (StringUtils.isNotBlank(delegateUserT.getContact()) && (null != delegateUserS) && delegateUserT.getContact().equals(delegateUserS.getContact())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664889857"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664889857"), (String) examScoreMap.get("1151013343664889857"), (String) examScoreMap.get("1151013343664889857"),
                    delegateUserT.getContact(), delegateUserS.getContact(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664889857"), "0", (String) examScoreMap.get("1151013343664889857"),
                    delegateUserT.getContact(), delegateUserS == null ? "" : delegateUserS.getContact(), "1");
        }
        if (StringUtils.isNotBlank(delegateUserT.getPhone()) && (null != delegateUserS) && delegateUserT.getPhone().equals(delegateUserS.getPhone())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664988161"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664988161"), (String) examScoreMap.get("1151013343664988161"), (String) examScoreMap.get("1151013343664988161"),
                    delegateUserT.getPhone(), delegateUserS.getPhone(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664988161"), "0", (String) examScoreMap.get("1151013343664988161"),
                    delegateUserT.getPhone(), delegateUserS == null ? "" : delegateUserS.getPhone(), "1");
        }
        //aa_entrust_file_type
        if (StringUtils.isNotBlank(delegateUserT.getEntrustType()) && (null != delegateUserS) && delegateUserT.getEntrustType().equals(delegateUserS.getEntrustType())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663902721"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663902721"), (String) examScoreMap.get("1151013343663902721"), (String) examScoreMap.get("1151013343663902721"),
                    getDic("aa_entrust_file_type", delegateUserT.getEntrustType()),
                    getDic("aa_entrust_file_type", delegateUserS.getEntrustType()),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663902721"), "0", (String) examScoreMap.get("1151013343663902721"),
                    getDic("aa_entrust_file_type", delegateUserT.getEntrustType()),
                    delegateUserS == null ? "" :  getDic("aa_entrust_file_type", delegateUserS.getEntrustType()) ,
                    "1");
        }
        if (StringUtils.isNotBlank(delegateUserT.getCompleteDate()) && (null != delegateUserS) && delegateUserT.getCompleteDate().equals(delegateUserS.getCompleteDate())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665516545"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665516545"), (String) examScoreMap.get("1151013343665516545"), (String) examScoreMap.get("1151013343665516545"),
                    delegateUserT.getCompleteDate(), delegateUserS.getCompleteDate(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665516545"), "0", (String) examScoreMap.get("1151013343665516545"),
                    delegateUserT.getCompleteDate(), delegateUserS == null ? "" : delegateUserS.getCompleteDate(), "1");
        }

        if (StringUtils.isNotBlank(carInfoT.getLicensePlateNum()) && (null != carInfoS) && carInfoT.getLicensePlateNum().equals(carInfoS.getLicensePlateNum())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666503681"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666503681"), (String) examScoreMap.get("1151013343666503681"), (String) examScoreMap.get("1151013343666503681"),
                    carInfoT.getLicensePlateNum(), carInfoS.getLicensePlateNum(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666503681"), "0", (String) examScoreMap.get("1151013343666503681"),
                    carInfoT.getLicensePlateNum(), carInfoS == null ? "" : carInfoS.getLicensePlateNum(), "1");
        }
        //车辆类型
        if (StringUtils.isNotBlank(carInfoT.getVehicleType()) && (null != carInfoS) && carInfoT.getVehicleType().equals(carInfoS.getVehicleType())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664988163"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664988163"), (String) examScoreMap.get("1151013343664988163"), (String) examScoreMap.get("1151013343664988163"),
                    carInfoT.getVehicleType(), carInfoS.getVehicleType(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664988163"), "0", (String) examScoreMap.get("1151013343664988163"),
                    carInfoT.getVehicleType(), carInfoS == null ? "" : carInfoS.getVehicleType(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getVinCode()) && (null != carInfoS) && carInfoT.getVinCode().equals(carInfoS.getVinCode())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664328705"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664328705"), (String) examScoreMap.get("1151013343664328705"), (String) examScoreMap.get("1151013343664328705"),
                    carInfoT.getVinCode(), carInfoS.getVinCode(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664328705"), "0", (String) examScoreMap.get("1151013343664328705"),
                    carInfoT.getVinCode(), carInfoS == null ? "" : carInfoS.getVinCode(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getLabelType()) && (null != carInfoS) && carInfoT.getLabelType().equals(carInfoS.getLabelType())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666032641"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666032641"), (String) examScoreMap.get("1151013343666032641"), (String) examScoreMap.get("1151013343666032641"),
                    carInfoT.getLabelType(), carInfoS.getLabelType(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666032641"), "0", (String) examScoreMap.get("1151013343666032641"),
                    carInfoT.getLabelType(), carInfoS == null ? "" : carInfoS.getLabelType(), "1");
        }
//        DictUtils.getDictLabel("aa_usage_type", carInfoT.getUsage(), "");

        if (StringUtils.isNotBlank(carInfoT.getUsage()) && (null != carInfoS) && carInfoT.getUsage().equals(carInfoS.getUsage())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666728961"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666728961"), (String) examScoreMap.get("1151013343666728961"), (String) examScoreMap.get("1151013343666728961"),
                    getDic("aa_usage_type", carInfoT.getUsage()), getDic("aa_usage_type", carInfoS.getUsage()), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666728961"), "0", (String) examScoreMap.get("1151013343666728961"),
                    getDic("aa_usage_type", carInfoT.getUsage()), carInfoS == null ? "" : getDic("aa_usage_type", carInfoS.getUsage()), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getTotalQuality()) && (null != carInfoS) && carInfoT.getTotalQuality().equals(carInfoS.getTotalQuality())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663927297"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663927297"), (String) examScoreMap.get("1151013343663927297"), (String) examScoreMap.get("1151013343663927297"),
                    carInfoT.getTotalQuality(), carInfoS.getTotalQuality(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663927297"), "0", (String) examScoreMap.get("1151013343663927297"),
                    carInfoT.getTotalQuality(), carInfoS == null ? "" : carInfoS.getTotalQuality(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getSear()) && (null != carInfoS) && carInfoT.getSear().equals(carInfoS.getSear())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664050177"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664050177"), (String) examScoreMap.get("1151013343664050177"), (String) examScoreMap.get("1151013343664050177"),
                    carInfoT.getSear(), carInfoS.getSear(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664050177"), "0", (String) examScoreMap.get("1151013343664050177"),
                    carInfoT.getSear(), carInfoS == null ? "" : carInfoS.getSear(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getDisplacement()) && (null != carInfoS) && carInfoT.getDisplacement().equals(carInfoS.getDisplacement())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343662784513"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343662784513"), (String) examScoreMap.get("1151013343662784513"), (String) examScoreMap.get("1151013343662784513"),
                    carInfoT.getDisplacement(), carInfoS.getDisplacement(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343662784513"), "0", (String) examScoreMap.get("1151013343662784513"),
                    carInfoT.getDisplacement(), carInfoS == null ? "" : carInfoS.getDisplacement(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getFuelType()) && (null != carInfoS) && carInfoT.getFuelType().equals(carInfoS.getFuelType())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665344513"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665344513"), (String) examScoreMap.get("1151013343665344513"), (String) examScoreMap.get("1151013343665344513"),
                    getDic("aa_fuel_type", carInfoT.getFuelType()), getDic("aa_fuel_type", carInfoS.getFuelType()), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665344513"), "0", (String) examScoreMap.get("1151013343665344513"),
                    getDic("aa_fuel_type", carInfoT.getFuelType()), carInfoS == null ? "" : getDic("aa_fuel_type", carInfoS.getFuelType()), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getRegisterDate()) && (null != carInfoS) && carInfoT.getRegisterDate().equals(carInfoS.getRegisterDate())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343662788609"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343662788609"), (String) examScoreMap.get("1151013343662788609"), (String) examScoreMap.get("1151013343662788609"),
                    carInfoT.getRegisterDate(), carInfoS.getRegisterDate(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343662788609"), "0", (String) examScoreMap.get("1151013343662788609"),
                    carInfoT.getRegisterDate(), carInfoS == null ? "" : carInfoS.getRegisterDate(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getColor()) && (null != carInfoS) && carInfoT.getColor().equals(carInfoS.getColor())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664787457"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664787457"), (String) examScoreMap.get("1151013343664787457"), (String) examScoreMap.get("1151013343664787457"),
                    getDic("aa_vehicle_color", carInfoT.getColor()),
                    getDic("aa_vehicle_color", carInfoS.getColor()),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664787457"), "0", (String) examScoreMap.get("1151013343664787457"),
                    getDic("aa_vehicle_color", carInfoT.getColor()),
                    carInfoS == null ? "" :
                            getDic("aa_vehicle_color", carInfoS.getColor()), "1");
        }
        if (null != carInfoT.getUseYear() && null != carInfoT.getUseMonth() && (null != carInfoS) && carInfoT.getUseYear().equals(carInfoS.getUseYear()) && carInfoT.getUseMonth().equals(carInfoS.getUseMonth())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665397761"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665397761"), (String) examScoreMap.get("1151013343665397761"), (String) examScoreMap.get("1151013343665397761"),
                    setNullToEmpty(carInfoT.getUseYear(), "年") + setNullToEmpty(carInfoT.getUseMonth(), "月"),
                    setNullToEmpty(carInfoS.getUseYear(), "年") + setNullToEmpty(carInfoS.getUseMonth(), "月"),
                    "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665397761"), "0", (String) examScoreMap.get("1151013343665397761"),
                    setNullToEmpty(carInfoT.getUseYear(), "年") + setNullToEmpty(carInfoT.getUseMonth(), "月"),
                    setNullToEmpty(carInfoS == null ? "" : carInfoS.getUseYear(), "年") + setNullToEmpty(carInfoS == null ? "" : carInfoS.getUseMonth(), "月"),
                    "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getMileage()) && (null != carInfoS) && carInfoT.getMileage().equals(carInfoS.getMileage())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665954817"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665954817"), (String) examScoreMap.get("1151013343665954817"), (String) examScoreMap.get("1151013343665954817"),
                    carInfoT.getMileage(), carInfoS.getMileage(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665954817"), "0", (String) examScoreMap.get("1151013343665954817"),
                    carInfoT.getMileage(), carInfoS == null ? "" : carInfoS.getMileage(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getEngineOverhaulTimes()) && (null != carInfoS) && carInfoT.getEngineOverhaulTimes().equals(carInfoS.getEngineOverhaulTimes())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666315265"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666315265"), (String) examScoreMap.get("1151013343666315265"), (String) examScoreMap.get("1151013343666315265"),
                    carInfoT.getEngineOverhaulTimes(), carInfoS.getEngineOverhaulTimes(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666315265"), "0", (String) examScoreMap.get("1151013343666315265"),
                    carInfoT.getEngineOverhaulTimes(), carInfoS == null ? "" : carInfoS.getEngineOverhaulTimes(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getCarOverhaulTimes()) && (null != carInfoS) && carInfoT.getCarOverhaulTimes().equals(carInfoS.getCarOverhaulTimes())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343664455681"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664455681"), (String) examScoreMap.get("1151013343664455681"), (String) examScoreMap.get("1151013343664455681"),
                    carInfoT.getCarOverhaulTimes(), carInfoS.getCarOverhaulTimes(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343664455681"), "0", (String) examScoreMap.get("1151013343664455681"),
                    carInfoT.getCarOverhaulTimes(), carInfoS == null ? "" : carInfoS.getCarOverhaulTimes(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getMaintenanceSituation()) && (null != carInfoS) && carInfoT.getMaintenanceSituation().equals(carInfoS.getMaintenanceSituation())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665635329"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665635329"), (String) examScoreMap.get("1151013343665635329"), (String) examScoreMap.get("1151013343665635329"),
                    carInfoT.getMaintenanceSituation(), carInfoS.getMaintenanceSituation(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665635329"), "0", (String) examScoreMap.get("1151013343665635329"),
                    carInfoT.getMaintenanceSituation(), carInfoS == null ? "" : carInfoS.getMaintenanceSituation(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getAccident()) && (null != carInfoS) && carInfoT.getAccident().equals(carInfoS.getAccident())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343665983489"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665983489"), (String) examScoreMap.get("1151013343665983489"), (String) examScoreMap.get("1151013343665983489"),
                    carInfoT.getAccident(), carInfoS.getAccident(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343665983489"), "0", (String) examScoreMap.get("1151013343665983489"),
                    carInfoT.getAccident(), carInfoS == null ? "" : carInfoS.getAccident(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getPurchaseDate()) && (null != carInfoS) && carInfoT.getPurchaseDate().equals(carInfoS.getPurchaseDate())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666704385"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666704385"), (String) examScoreMap.get("1151013343666704385"), (String) examScoreMap.get("1151013343666704385"),
                    carInfoT.getPurchaseDate(), carInfoS.getPurchaseDate(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666704385"), "0", (String) examScoreMap.get("1151013343666704385"),
                    carInfoT.getPurchaseDate(), carInfoS == null ? "" : carInfoS.getPurchaseDate(), "1");
        }
        if (StringUtils.isNotBlank(carInfoT.getOriginalPrice()) && (null != carInfoS) && carInfoT.getOriginalPrice().equals(carInfoS.getOriginalPrice())) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343663955969"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663955969"), (String) examScoreMap.get("1151013343663955969"), (String) examScoreMap.get("1151013343663955969"),
                    carInfoT.getOriginalPrice(), carInfoS.getOriginalPrice(), "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343663955969"), "0", (String) examScoreMap.get("1151013343663955969"),
                    carInfoT.getOriginalPrice(), carInfoS == null ? "" : carInfoS.getOriginalPrice(), "1");
        }

        //盖章
        PictureUser pictureUserS = new PictureUser();
        pictureUserS.setExamUserId(user.getId());
        pictureUserS.setPictureTypeId("1143436249238634496");  //手动签字
        pictureUserS = pictureUserService.getByEntity(pictureUserS);

        PictureUser pictureUserT = new PictureUser();
        pictureUserT.setPaperId(paperId);
        pictureUserT.setPictureTypeId("1143436249238634496");  //手动签字
        pictureUserT = pictureUserService.getByEntity(pictureUserT);
        if (null != pictureUserT && null != pictureUserS) {
            delegateCount = delegateCount.add(BigDecimal.valueOf(Integer.valueOf((String) examScoreMap.get("1151013343666032621"))));
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666032621"), (String) examScoreMap.get("1151013343666032621"), (String) examScoreMap.get("1151013343666032621"),
                    "已签", "已签", "0");
        } else {
            saveExamDetail(user.getId(), user.getExamId(), "1151028180617777153",
                    (String) examNameMap.get("1151013343666032621"), "0", (String) examScoreMap.get("1151013343666032621"),
                    null == pictureUserT ? "未签" : "已签", pictureUserS == null ? "未签" : "已签", "1");
        }

        return delegateCount;
    }


    //调用字典
    public String getDic(String dicCode, String code) {
        switch (dicCode) {
            case "aa_usage_type":      //使用用途
                return DictUtils.getDictLabel(dicCode, code, "");
            case "aa_fuel_type":       //燃油种类
                return DictUtils.getDictLabel(dicCode, code, "");
            case "aa_vehicle_color":   //车身颜色
                return DictUtils.getDictLabel(dicCode, code, "");
            case "aa_vehicle_level":   //级别
                return DictUtils.getDictLabel(dicCode, code, "");
            case "aa_environmental_standard":  //环保标准
                return DictUtils.getDictLabel(dicCode, code, "");
            case "aa_entrust_file_type":  //环保标准
                return DictUtils.getDictLabel(dicCode, code, "");
            default:
        }
        return "";
    }


    /**
     * //数据判断 如果为 null 则替换成 ""
     *
     * @param obj 需要判断的字符串
     * @param str
     */
    public String setNullToEmpty(Object obj, String str) {
        //如果不为空
        if (null != obj && (!"".equals(obj))) {
            return obj + str;
        } else {
            return "";
        }
    }


    /**
     * 考试计时
     */
    @Transactional(readOnly = false)
    public CommonResult examTiming(ExamUser examUser) {
        CommonResult comRes = new CommonResult();
        TimingVO vo = new TimingVO();
        examUser = this.get(examUser);
        if (examUser == null) {
            comRes.setCode(CodeConstant.REQUEST_FAILED);
            comRes.setMsg("未查询到考生信息！");
            return comRes;
        }
        Exam exam = new Exam();
        exam.setId(examUser.getExamId());
        exam = examService.getByEntity(exam);
        if (exam == null) {
            comRes.setCode(CodeConstant.REQUEST_FAILED);
            comRes.setMsg("未查询到考试信息！");
            return comRes;
        }
        if (!"3".equals(exam.getState())) {
            return new CommonResult(CodeConstant.EXAM_END, "考试已结束！");
        }
        vo.setExamType(exam.getExamType());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        BigDecimal nowTime = new BigDecimal(new Date().getTime());      //当前时间
        BigDecimal startTime = new BigDecimal(examUser.getStartTime().getTime());       //考试开始时间
        BigDecimal spentTime = nowTime.subtract(startTime);     //考生考试已用时长（nowTime-startTime）
        if ("1".equals(exam.getExamType())) {
            //倒计时
            BigDecimal duration = new BigDecimal(exam.getDuration());       //考试总时长
            duration = duration.multiply(new BigDecimal("60000"));      //换算毫秒值
            BigDecimal surplusTime = duration.subtract(spentTime);      //本场考试剩余时长(duration-spentTime)

            //对剩余时长进行校验，如果剩余时长小于零，则考试结束，返回状态码。
            if (surplusTime.compareTo(new BigDecimal("0")) >= 0) {
                vo.setTimer(df.format(new Date(surplusTime.longValue())));
                comRes.setData(vo);
            } else {
                comRes.setCode(CodeConstant.EXAM_END);
                comRes.setMsg("考试时间到，考试结束！");
                return comRes;
            }
        } else {
            //正计时
            vo.setTimer(df.format(new Date(spentTime.longValue())));
            comRes.setData(vo);
        }
        return comRes;

    }

    /**
     * 学生登录 验证该学生是否存在考试中状态的考试
     *
     * @param examUser
     * @return
     */
    public ExamUser getAllowLogin(ExamUser examUser) {
        return dao.getAllowLogin(examUser);
    }

    /**
     * 根据学生id 调用大平台获取学生详细信息
     *
     * @return
     */
    public CommonResult getLoadStuListByIds(String userIds) {
        Map<String, String> map = new HashMap();
        map.put("ids", userIds);
        CommonResult comRes = httpClientService.post(ServiceConstant.DERIVE_STUDENT_ACHIEVEMENT, map);
        return comRes;

    }

    /**
     * 根据大平台返回学生详细信息，转成对应的实体对象，批量导出 (并完成排序功能)
     *
     * @param array
     * @param examId
     * @return
     */
    public List<ExamUser> getExamUserListByPlatfrom(JSONArray array, String[] examId) {
        List<ExamUser> examUserList = dao.getExamUserByExamId(examId);
        for (ExamUser examUser : examUserList) {
            for (Object platformExamUser : array) {
                JSONObject platformUser = (JSONObject) platformExamUser;
                if (examUser.getUserId().equals(platformUser.getString("id"))) {
                    examUser.setUserNum(platformUser.getString("userName"));
                    examUser.setTrueName(platformUser.getString("trueName"));
                    examUser.setSchoolName(platformUser.getString("schoolName"));
                    examUser.setMajorName(platformUser.getString("majorName"));
                    examUser.setClassName(platformUser.getString("className"));
                    examUser.setGender(platformUser.getString("gender"));
                    Exam exam = new Exam();
                    exam.setId(examUser.getExamId());
                    String examName = examService.getByEntity(exam).getName();
                    examUser.setExamName(examName);
                }
            }
        }
        return examUserList;
    }

    /**
     * 根据学生userid 考试id 查询这些学生是否在其他考试内
     *
     * @param userIdList
     * @returnuploadScore
     */
    public CommonResult getExamStateByUserId(List<String> userIdList, Exam exam) {
        CommonResult comRes = new CommonResult();
        List<String> returnList = new ArrayList<>();
        StringBuilder studentUserIds = new StringBuilder();
        List<ExamUser> examUserList = dao.getExamStateByUserId(userIdList, exam.getId());
        if (CollectionUtils.isEmpty(examUserList)) {
            return comRes;
        }
        int len = examUserList.size();
        for (int i = 0; i < len; i++) {
            if (i == len - 1) {
                studentUserIds.append(examUserList.get(i).getUserId());
            } else {
                studentUserIds.append(examUserList.get(i).getUserId() + ",");
            }
        }
        Map<String, String> map = new HashMap();
        map.put("ids", studentUserIds.toString());


        comRes = httpClientService.post(ServiceConstant.DERIVE_STUDENT_ACHIEVEMENT, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(comRes.getCode())) {
            return comRes;
        }
        JSONArray array = JSONArray.parseArray(comRes.getData().toString());
        if (CollectionUtils.isNotEmpty(array)) {
            int i = 0;
            for (Object o : array) {
                JSONObject loanuser = (JSONObject) o;
                for (ExamUser user : examUserList) {
                    if (loanuser.getString("id").equals(user.getUserId())) {
                        returnList.add("用户名为" + loanuser.getString("trueName") + "的考生正在" + examUserList.get(i).getExamName() + "考试中");
                        i++;
                        break;
                    }
                }
                if (i > 2) {
                    returnList.add("....");
                    break;
                }
            }
        }
        comRes.setCode(CodeConstant.UNREASONABLE_CANDIDATE_STATUS);
        comRes.setMsg(returnList.toString());
        comRes.setData("");
        return comRes;
    }

    /**
     * 保存考生信息  先删在增
     *
     * @param examUserJson
     * @return
     */
    @Transactional
    public Object saveExamUser(String examUserJson, String examId) {
        CommonResult comRes = new CommonResult();
        examUserJson = examUserJson.replace("\n", "");
        examUserJson = examUserJson.replace(" ", "");
        //判断该考试、练习是否存在考试生
        if (StringUtils.isNotBlank(examId)) {
            ExamUser examUser = new ExamUser();
            examUser.setExamId(examId);
            super.dao.phyDeleteByEntity(examUser);
        } else {
            return "考试id不能为空!";
        }
        JSONArray jsonArray = JSONObject.parseArray(examUserJson);
        for (Object o : jsonArray) {
            JSONObject userJson = (JSONObject) o;
            ExamUser examUser = new ExamUser();
            examUser.setUserId(userJson.getString("userId"));
            examUser.setServerExamUserId(userJson.getString("serverExamUserId"));
            examUser.setExamId(examId);
            super.save(examUser);
        }
        return comRes;
    }

    /**
     * 真删除学生
     */
    public void deleteByExamUser(String examId) {
        if (StringUtils.isNotBlank(examId)) {
            ExamUser examUser = new ExamUser();
            examUser.setExamId(examId);
            dao.phyDeleteByEntity(examUser);
        }
    }

    public List<ExamUser> getExamUserByExamId(String[] examIds) {
        return dao.getExamUserByExamId(examIds);
    }
}