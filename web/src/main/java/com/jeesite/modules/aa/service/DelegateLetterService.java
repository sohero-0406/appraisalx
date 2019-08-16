/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.MoneyUtils;
import com.jeesite.common.utils.download.DownloadWordUtils;
import com.jeesite.common.utils.word.WordExport;
import com.jeesite.modules.aa.dao.DelegateLetterDao;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.vo.AppraisalReportVO;
import com.jeesite.modules.aa.vo.DelegateLetterVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 委托书信息Service
 *
 * @author lvchangwei
 * @version 2019-07-18
 */
@Service
@Transactional(readOnly = true)
public class DelegateLetterService extends CrudService<DelegateLetterDao, DelegateLetter> {

    @Autowired
    private DelegateUserService delegateUserService;
    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private PictureUserService pictureUserService;
    @Autowired
    private VehicleDocumentInfoService vehicleDocumentInfoService;
    @Autowired
    private CheckTradableVehiclesService checkTradableVehiclesService;
    @Autowired
    private IdentifyTecService identifyTecService;
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private DelegateLetterService delegateLetterService;
    @Autowired
    private ExamService examService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 获取单条数据
     *
     * @param delegateLetter
     * @return
     */
    @Override
    public DelegateLetter get(DelegateLetter delegateLetter) {
        return super.get(delegateLetter);
    }

    /**
     * 查询分页数据
     *
     * @param delegateLetter 查询条件
     * @return
     */
    @Override
    public Page<DelegateLetter> findPage(DelegateLetter delegateLetter) {
        return super.findPage(delegateLetter);
    }

    /**
     * 保存数据（插入或更新）
     *
     * @param delegateLetter
     */
    @Override
    @Transactional(readOnly = false)
    public void save(DelegateLetter delegateLetter) {
        super.save(delegateLetter);
    }

    /**
     * 更新状态
     *
     * @param delegateLetter
     */
    @Override
    @Transactional(readOnly = false)
    public void updateStatus(DelegateLetter delegateLetter) {
        super.updateStatus(delegateLetter);
    }

    /**
     * 删除数据
     *
     * @param delegateLetter
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(DelegateLetter delegateLetter) {
        super.delete(delegateLetter);
    }

    public DelegateLetter getByEntity(DelegateLetter delegateLetter) {
        return dao.getByEntity(delegateLetter);
    }

    /**
     * 查询单个委托书信息
     */
    public DelegateLetterVO findDelegateLetter(ExamUser examUser) {

        DelegateLetterVO delegateLetterVO = new DelegateLetterVO();

        DelegateLetter delegateLetter = new DelegateLetter();
        delegateLetter.setPaperId(examUser.getPaperId());
        delegateLetter = this.getByEntity(delegateLetter);
        delegateLetterVO.setDelegateLetter(delegateLetter);

        DelegateUser delegateUser = new DelegateUser();
        delegateUser.setExamUserId(examUser.getExamId());
        delegateUser.setPaperId(examUser.getPaperId());
        delegateUser = delegateUserService.getByEntity(delegateUser);
        if (StringUtils.isNotBlank(delegateUser.getCompleteDate())) {
            String[] completeDateArr = delegateUser.getCompleteDate().split("-");
            delegateUser.setCompleteDate(completeDateArr[0] + "年" + completeDateArr[1] + "月" + completeDateArr[2] + "日");
        }
        delegateLetterVO.setDelegateUser(delegateUser);

        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getExamId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        carInfo.setColor(DictUtils.getDictLabel("aa_vehicle_color", carInfo.getColor(), ""));
        carInfo.setFuelType(DictUtils.getDictLabel("aa_fuel_type", carInfo.getFuelType(), ""));
        if (StringUtils.isNotBlank(carInfo.getRegisterDate())) {
            String[] registerDate = carInfo.getRegisterDate().split("-");
            carInfo.setRegisterDate(registerDate[0] + "年" + registerDate[1] + "月" + registerDate[2] + "日");
        }
        if (StringUtils.isNotBlank(carInfo.getPurchaseDate())) {
            String[] purchaseDate = carInfo.getPurchaseDate().split("-");
            carInfo.setPurchaseDate(purchaseDate[0] + "年" + purchaseDate[1] + "月" + purchaseDate[2] + "日");
        }
        delegateLetterVO.setCarInfo(carInfo);

        PictureUser pictureUser = new PictureUser();
        pictureUser.setExamUserId(examUser.getExamId());
        pictureUser.setPaperId(examUser.getPaperId());
        pictureUser.setPictureTypeId("1143436249238634496");
        pictureUser = pictureUserService.getByEntity(pictureUser);
        delegateLetterVO.setPictureUser(pictureUser);

        return delegateLetterVO;
    }

    /**
     * 参数check_name  复核人姓名
     *
     * @param delegateLetter
     */
    @Transactional(readOnly = false)
    public CommonResult saveAppraisalReport(DelegateLetter delegateLetter, ExamUser examUser) {
        CommonResult comRes = new CommonResult();
        DelegateLetter letter = new DelegateLetter();
        //查找对应的 教师手输数据
        letter.setPaperId(examUser.getPaperId());
        letter = dao.getByEntity(letter);
        if (null == letter) {  //判断是否存在数据
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
            comRes.setMsg("数据存在异常!");
            return comRes;
        }
        letter.setCheckName(delegateLetter.getCheckName());
        letter.setAppraiser(delegateLetter.getAppraiser());  //二手车鉴定评估师
        letter.setAppraiserDate(delegateLetter.getAppraiserDate()); //二手车鉴定评估机构盖章日期
        super.save(letter);
        return comRes;
    }


    /**
     * 生成鉴定评估报告编号
     */
    @Transactional(readOnly = false)
    public void createAppraisalReportNum(ExamUser examUser) {
        DelegateUser delegateUser = new DelegateUser();
        //判断 学生或者老师
        //老师
        delegateUser.setPaperId(examUser.getPaperId());
        //学生
        delegateUser.setExamUserId(examUser.getId());
        delegateUser = delegateUserService.getByEntity(delegateUser);
        //判断 是否记录 时间标号  如果为空 修改数据
        if (StringUtils.isBlank(delegateUser.getAppraisalDate())) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            delegateUser.setAppraisalDate(df.format(new Date()));
            Integer appraisalNum;
            Integer max = findAppraisalNumMAX(delegateUser.getAppraisalDate());
            if (null != max) {
                appraisalNum = max + 1;
            } else {
                appraisalNum = 1;
            }
            delegateUser.setAppraisalNum(String.format("%8d", appraisalNum).replace(" ", "0"));
            delegateUserService.save(delegateUser);
        }
    }


    /**
     * 查询最大鉴定评估报告编号
     */
    public Integer findAppraisalNumMAX(String year) {
        return dao.findAppraisalNumMAX(year);
    }


    /**
     * 查询一份鉴定评估报告
     */
    public AppraisalReportVO findAppraisalReport(ExamUser examUser) {

        AppraisalReportVO appraisalReportVO = new AppraisalReportVO();
        //缺陷描述
        StringBuilder defectDescription = new StringBuilder();
        //大写金额
        String priceCapital = null;
        //委托人
        DelegateUser delegateUser = new DelegateUser();
        delegateUser.setExamUserId(examUser.getId());
        delegateUser.setPaperId(examUser.getPaperId());
        delegateUser = delegateUserService.getByEntity(delegateUser);
        appraisalReportVO.setDelegateUser(delegateUser);

        //车辆信息
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        if (StringUtils.isNotBlank(carInfo.getLevel())) {
            carInfo.setColor(DictUtils.getDictLabel("aa_vehicle_color", carInfo.getColor(), ""));
        }
        if (StringUtils.isNotBlank(carInfo.getUsage())) {
            carInfo.setUsage(DictUtils.getDictLabel("aa_usage_type", carInfo.getUsage(), ""));
        }
        appraisalReportVO.setCarInfo(carInfo);

        //车辆单证信息
        List<VehicleDocumentInfo> vehicleDocumentInfoList = new ArrayList<VehicleDocumentInfo>();
        VehicleDocumentInfo vehicleDocumentInfo = new VehicleDocumentInfo();
        vehicleDocumentInfo.setExamUserId(examUser.getId());
        vehicleDocumentInfo.setPaperId(examUser.getPaperId());
        vehicleDocumentInfoList = vehicleDocumentInfoService.findList(vehicleDocumentInfo);
        appraisalReportVO.setVehicleDocumentInfolist(vehicleDocumentInfoList);

        //检查可交易车辆
        CheckTradableVehicles checkTradableVehicles = new CheckTradableVehicles();
        checkTradableVehicles.setExamUserId(examUser.getId());
        checkTradableVehicles.setPaperId(examUser.getPaperId());
        checkTradableVehicles = checkTradableVehiclesService.getByEntity(checkTradableVehicles);
        appraisalReportVO.setCheckTradableVehicles(checkTradableVehicles);

        //鉴定技术状况
        List<IdentifyTec> identifyTecList = new ArrayList<IdentifyTec>();
        IdentifyTec identifyTec = new IdentifyTec();
        identifyTec.setExamUserId(examUser.getId());
        identifyTec.setPaperId(examUser.getPaperId());
        identifyTecList = identifyTecService.findList(identifyTec);
        List<String> descrip = new ArrayList<>();
        for (IdentifyTec tec : identifyTecList) {
            if (StringUtils.isNotBlank(tec.getDescription())) {
                descrip.add(tec.getDescription());
            }
        }
        for (int i = 0; i < descrip.size(); i++) {
            if (i == descrip.size() - 1) {
                defectDescription.append(descrip.get(i));
            } else {
                defectDescription.append(descrip.get(i) + ",");
            }
        }
        //车辆等级评定
        VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
        vehicleGradeAssess.setExamUserId(examUser.getId());
        vehicleGradeAssess.setPaperId(examUser.getPaperId());
        vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
        //设置技术状况
        if(StringUtils.isNotBlank(vehicleGradeAssess.getTechnicalStatus())){
            vehicleGradeAssess.setTechnicalStatus(DictUtils.getDictLabel("aa_technical_status", vehicleGradeAssess.getTechnicalStatus(), ""));
        }

        appraisalReportVO.setVehicleGradeAssess(vehicleGradeAssess);
        defectDescription.append(StringUtils.isNotBlank(vehicleGradeAssess.getDescription()) ? "," + vehicleGradeAssess.getDescription() : "");
        //填入缺陷描述
        appraisalReportVO.setDefectDescription(defectDescription.toString());

        //计算车辆价值
        Calculate calculate = new Calculate();
        calculate.setExamUserId(examUser.getId());
        calculate.setPaperId(examUser.getPaperId());
        calculate = calculateService.getByEntity(calculate);
        //设置算法类型
        calculate.setType(calculateService.getType(calculate));


        appraisalReportVO.setCalculate(calculate);
        appraisalReportVO.setPriceCapital(priceCapital);

        //教师手动输入数据
        DelegateLetter delegateLetter = new DelegateLetter();
        if (StringUtils.isNotBlank(examUser.getPaperId())) {
            delegateLetter.setPaperId(examUser.getPaperId());
        } else {
            Exam exam = new Exam();
            exam.setId(examUser.getExamId());
            exam = examService.getByEntity(exam);
            delegateLetter.setPaperId(exam.getPaperId());
        }
        delegateLetter = delegateLetterService.getByEntity(delegateLetter);
        appraisalReportVO.setDelegateLetter(delegateLetter);

        if (StringUtils.isNotBlank(carInfo.getModel())) {
            //车辆配置全表
            Map<String, String> map = new HashMap<>();
            map.put("chexingId", carInfo.getModel());
            CommonResult result = httpClientService.post(ServiceConstant.VEHICLEINFO_GET_CAR_MODEL, map);
            if (CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())) {
                JSONObject vehicleInfo = JSONObject.parseObject(result.getData().toString());
                appraisalReportVO.setVehicleInfo(vehicleInfo);
            }
        }
        return appraisalReportVO;
    }


    /**
     * 整理报告内容
     */
    public Map appraisalReportInfo(ExamUser examUser) throws ParseException {

        Map<String, Object> returnMap = new HashMap<>();
        AppraisalReportVO appraisalReportVO = this.findAppraisalReport(examUser);
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy年MM月dd日");

        returnMap.put("appraisalNum", appraisalReportVO.getDelegateUser().getAppraisalNum()); //评报字-数字（8位
        returnMap.put("appraisalDate", appraisalReportVO.getDelegateUser().getAppraisalDate());//评报字-时间

        //一、绪言
        returnMap.put("organizationName", appraisalReportVO.getDelegateLetter().getOrganizationName());//机构
        returnMap.put("name", appraisalReportVO.getDelegateUser().getName());                  //接受委托\委托方
        returnMap.put("licensePlateNum", appraisalReportVO.getCarInfo().getLicensePlateNum());//牌号
        appraisalReportVO.getVehicleGradeAssess().getIdentifyDate();
        //二、委托方
        //委托方 同上name
        returnMap.put("contact", appraisalReportVO.getDelegateUser().getContact());//委托方联系人
        returnMap.put("phone", appraisalReportVO.getDelegateUser().getPhone());//联系电话
        //车主姓名 同上name
        //三、评定基准日
        // //鉴定评估基准日 见下
        //四、鉴定评估车辆信息
        CarInfo carInfo = appraisalReportVO.getCarInfo();
        returnMap.put("labelType", carInfo.getLevel());//厂牌型号
        //拍照号码同上
        returnMap.put("engineNum", carInfo.getEngineNum());//发动机号码
        returnMap.put("vinCode", carInfo.getVinCode()); //车辆识别代号／车架号
        returnMap.put("color", carInfo.getColor());
        returnMap.put("mileage", carInfo.getMileage()); //表征里程 (行驶里程)
        returnMap.put("registerDateYear", "");  //注册登记日期(初次登记日期) 见下
        returnMap.put("registerDateMonth", "");
        returnMap.put("registerDateDay", "");
        if (StringUtils.isNotBlank(carInfo.getRegisterDate())) {
            String[] registerDate = carInfo.getRegisterDate().substring(0, 10).split("-");
            returnMap.replace("registerDateYear", registerDate[0]);
            returnMap.replace("registerDateMonth", registerDate[1]);
            returnMap.replace("registerDateDay", registerDate[2]);
        }
        returnMap.put("check3", appraisalReportVO.getCheckTradableVehicles().getCheck3());//是否查封、抵押车辆


        //车船税截止日期
        returnMap.put("carTaxEndDateYear", ""); //年
        returnMap.put("carTaxEndDateMonth", "");//月
        //交强险截止日期
        returnMap.put("compulsoryInsuranceYear", "");
        returnMap.put("compulsoryInsuranceMonth", "");
        returnMap.put("compulsoryInsuranceDay", "");

        // 车辆购置税（费）证
        returnMap.put("vehiclePurchaseTax", "");
        // 机动车登记证书
        returnMap.put("vehicleRegistration", "");
        // 机动车行驶证
        returnMap.put("vehicleLicense", "");
        List<VehicleDocumentInfo> vehicleDocumentInfoList = appraisalReportVO.getVehicleDocumentInfolist();
        for (VehicleDocumentInfo vehicleDocumentInfo : vehicleDocumentInfoList) {
            //车船税截止日期
            if ("3".equals(vehicleDocumentInfo.getProject()) && "1".equals(vehicleDocumentInfo.getState())
                    && StringUtils.isNotBlank(vehicleDocumentInfo.getValidity())) {
                String[] dateArray = vehicleDocumentInfo.getValidity().substring(0, 10).split("-");
                returnMap.replace("carTaxEndDateYear", dateArray[0]); //年
                returnMap.replace("carTaxEndDateMonth", dateArray[1]);//月
                continue;
            }
            //交强险截止日期
            if ("4".equals(vehicleDocumentInfo.getProject()) && "1".equals(vehicleDocumentInfo.getState())
                    && StringUtils.isNotBlank(vehicleDocumentInfo.getValidity())) {
                String[] dateArray = vehicleDocumentInfo.getValidity().substring(0, 10).split("-");
                returnMap.replace("compulsoryInsuranceYear", dateArray[0]); //年
                returnMap.replace("compulsoryInsuranceMonth", dateArray[1]);//月
                returnMap.replace("compulsoryInsuranceDay", dateArray[2]);//日
                continue;
            }
            if ("8".equals(vehicleDocumentInfo.getProject()) && StringUtils.isNotBlank(vehicleDocumentInfo.getState())) {
                // 车辆购置税（费）证
                returnMap.replace("vehiclePurchaseTax", vehicleDocumentInfo.getState());
                continue;
            }
            //机动车登记证书
            if ("2".equals(vehicleDocumentInfo.getProject()) && StringUtils.isNotBlank(vehicleDocumentInfo.getState())) {
                returnMap.replace("vehicleRegistration", vehicleDocumentInfo.getState());
                continue;
            }
            // 机动车行驶证
            if ("1".equals(vehicleDocumentInfo.getProject()) && StringUtils.isNotBlank(vehicleDocumentInfo.getState())) {
                returnMap.replace("vehicleLicense", vehicleDocumentInfo.getState());
                continue;
            }
        }
        returnMap.put("trafficIllegalRecord", appraisalReportVO.getCheckTradableVehicles().getTrafficIllegalRecord());//未接受处理的交通违法记录
        //使用性质
        returnMap.put("usage", appraisalReportVO.getCarInfo().getUsage());
        //五、技术鉴定结果
        returnMap.put("defectDescription", appraisalReportVO.getDefectDescription());//技术状况缺陷描述
        if (null != appraisalReportVO.getVehicleInfo()) {
            returnMap.put("chexingmingcheng", appraisalReportVO.getVehicleInfo().getString("chexingmingcheng"));//重要配置及参数信息
        }
        returnMap.put("technicalStatus", appraisalReportVO.getVehicleGradeAssess().getTechnicalStatus());//技术状况鉴定等级
        returnMap.put("score", appraisalReportVO.getVehicleGradeAssess().getScore());//等级描述
        // 六、价值评估
        returnMap.put("type", appraisalReportVO.getCalculate().getType());//价值估算方法
        Map<String, String> calculateMap = calculateService.getEstimateByType(examUser.getUserId(), examUser.getPaperId());
        returnMap.put("price", calculateMap.get("price"));  //价格
        returnMap.put("process", calculateMap.get("process"));//计算过程
        returnMap.put("bigPrice", MoneyUtils.change(Double.valueOf(calculateMap.get("price"))));
        //计算过程
        returnMap.put("yearCheckDueYear", "");
        returnMap.put("yearCheckDueMonth", "");
        //年检
        if (StringUtils.isNotBlank(carInfo.getYearCheckDue())) {
            String[] dateArray = carInfo.getYearCheckDue().substring(0, 10).split("-");
            returnMap.replace("yearCheckDueYear", dateArray[0]);
            returnMap.replace("yearCheckDueMonth", dateArray[1]);
        }

        //二手车鉴定评估机构盖章日期
        returnMap.put("appraiserDateYear", "");
        returnMap.put("appraiserDateMonth", "");
        returnMap.put("appraiserDateDay", "");

        if (StringUtils.isNotBlank(appraisalReportVO.getDelegateLetter().getAppraiserDate())) {
            String[] dateArray = appraisalReportVO.getDelegateLetter().getAppraiserDate().substring(0, 10).split("-");
            returnMap.replace("appraiserDateYear", dateArray[0]);
            returnMap.replace("appraiserDateMonth", dateArray[1]);
            returnMap.replace("appraiserDateDay", dateArray[2]);
        }
        //鉴定评估基准日
        returnMap.put("identifyYear", "");
        returnMap.put("identifyMonth", "");
        returnMap.put("identifyDay", "");
        //鉴定评估基准日  加90天
        returnMap.put("identifyAfterYear", "");
        returnMap.put("identifyAfterMonth", "");
        returnMap.put("identifyAfterDay", "");


        //八、鉴定评估报告法律效力
        Date identifyDate;
        Date identifyAfterDate;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.isNotBlank(appraisalReportVO.getVehicleGradeAssess().getIdentifyDate())) {
                identifyDate = formatter.parse(appraisalReportVO.getVehicleGradeAssess().getIdentifyDate());
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(identifyDate);
                calendar.add(calendar.DATE, 90);
                identifyAfterDate = calendar.getTime();
                returnMap.replace("identifyYear", String.format("%tY", identifyDate));
                returnMap.replace("identifyMonth", String.format("%tm", identifyDate));
                returnMap.replace("identifyDay", String.format("%td", identifyDate));
                returnMap.replace("identifyAfterYear", String.format("%tY", identifyAfterDate));
                returnMap.replace("identifyAfterMonth", String.format("%tm", identifyAfterDate));
                returnMap.replace("identifyAfterDay", String.format("%td", identifyAfterDate));
            }

        } catch (Exception e) {
            logger.warn("时间转换异常!--用户id为" + examUser.getUserId() + "--，生成鉴定报告异常");
        }

        //九 复核人
        returnMap.put("appraiser", appraisalReportVO.getDelegateLetter().getAppraiser());
        returnMap.put("checkName", appraisalReportVO.getDelegateLetter().getCheckName());

        return returnMap;
    }


    /***
     * 生成鉴定报告
     * 生成鉴定评估报告
     */
    @Transactional
    public void generateLetter(ExamUser examUser) throws ParseException {
        Map<String, String> reportMap = this.appraisalReportInfo(examUser);
        DelegateUser delegateUser = new DelegateUser();
        delegateUser.setExamUserId(examUser.getId());
        delegateUser.setPaperId(examUser.getPaperId());
        delegateUser = delegateUserService.getByEntity(delegateUser);

        try {
            WordExport changer = new WordExport();
            String fileName = "E:/word.docx";
            changer.setTemplate(fileName);
            Map<String, String> content = new HashMap<String, String>();

            content.put("appraisalNum", reportMap.get("appraisalNum"));// //评报字-数字（8位
            content.put("appraisalDate", reportMap.get("appraisalDate")); //评报字-时间

            //一、绪言
            content.put("organizationName", reportMap.get("organizationName"));
            content.put("name", reportMap.get("name"));
            content.put("licensePlateNum", reportMap.get("licensePlateNum"));
            content.put("identifyYear", reportMap.get("identifyYear"));
            content.put("identifyMonth", reportMap.get("identifyMonth"));
            content.put("identifyDay", reportMap.get("identifyDay"));
            //二、委托方
            content.put("name1", reportMap.get("name"));
            content.put("contact", reportMap.get("contact"));
            content.put("phone", reportMap.get("phone"));
            content.put("name2", reportMap.get("name"));
            //三、基准日同上
            content.put("identifyYear1", reportMap.get("identifyYear"));
            content.put("identifyMonth1", reportMap.get("identifyMonth"));
            content.put("identifyDay1", reportMap.get("identifyDay"));

            //四、鉴定评估车辆信息
            content.put("labelType", reportMap.get("labelType"));
            content.put("licensePlateNum1", reportMap.get("licensePlateNum"));
            content.put("engineNum", reportMap.get("engineNum"));
            content.put("vinCode", reportMap.get("vinCode"));
            content.put("color", reportMap.get("color"));
            content.put("mileage", reportMap.get("mileage"));


            //注册登记日期
            content.put("registerDateYear", reportMap.get("registerDateYear"));
            content.put("registerDateMonth", reportMap.get("registerDateMonth"));
            content.put("registerDateDay", reportMap.get("registerDateDay"));

            //年审检验合格
            content.put("yearCheckDueYear", reportMap.get("yearCheckDueYear"));
            content.put("yearCheckDueMonth", reportMap.get("yearCheckDueMonth"));
            //车船税截止
            content.put("carTaxEndDateYear", reportMap.get("carTaxEndDateYear"));
            content.put("carTaxEndDateMonth", reportMap.get("carTaxEndDateMonth"));
            //交强险截止日期
            content.put("compulsoryInsuranceYear", reportMap.get("compulsoryInsuranceYear"));
            content.put("compulsoryInsuranceMonth", reportMap.get("compulsoryInsuranceMonth"));
            content.put("compulsoryInsuranceDay", reportMap.get("compulsoryInsuranceDay"));

            content.put("check3", StringUtils.isBlank(reportMap.get("check3")) ? "" : ("1".equals(reportMap.get("check3")) ? "是" : "否"));
            content.put("vehiclePurchaseTax",
                    StringUtils.isBlank(reportMap.get("vehiclePurchaseTax")) ? "" : ("1".equals(reportMap.get("vehiclePurchaseTax")) ? "有" : "无"));
            content.put("vehicleRegistration",
                    StringUtils.isBlank(reportMap.get("vehicleRegistration")) ? "" : ("1".equals(reportMap.get("vehicleRegistration")) ? "有" : "无"));
            content.put("vehicleLicense",
                    StringUtils.isBlank(reportMap.get("vehicleLicense")) ? "" : ("1".equals(reportMap.get("vehicleLicense")) ? "有" : "无"));
            content.put("trafficIllegalRecord",
                    StringUtils.isBlank(reportMap.get("trafficIllegalRecord")) ? "" : ("1".equals(reportMap.get("trafficIllegalRecord")) ? "有" : "无"));
            content.put("usage", reportMap.get("usage"));

            //五、技术鉴定结果process
            content.put("defectDescription", reportMap.get("defectDescription"));
            content.put("chexingmingcheng", reportMap.get("chexingmingcheng"));
            content.put("technicalStatus", reportMap.get("technicalStatus"));
            content.put("score", StringUtils.isNotBlank(reportMap.get("score")) ? reportMap.get("score") + "分" : "");

            // 六、价值评估
            content.put("type", reportMap.get("type"));
            content.put("process", reportMap.get("process"));//计算过程
            content.put("price", reportMap.get("price"));
            content.put("bigPrice", reportMap.get("bigPrice"));

            //八、鉴定评估报告法律效力
            content.put("identifyAfterYear", reportMap.get("identifyAfterYear"));
            content.put("identifyAfterMonth", reportMap.get("identifyAfterMonth"));
            content.put("identifyAfterDay", reportMap.get("identifyAfterDay"));
            // 九、声明
            content.put("checkName", reportMap.get("checkName"));
            content.put("appraiser", reportMap.get("appraiser"));
            content.put("appraiserDateYear", reportMap.get("appraiserDateYear"));
            content.put("appraiserDateMonth", reportMap.get("appraiserDateMonth"));
            content.put("appraiserDateDay", reportMap.get("appraiserDateDay"));
            content.put("identifyYear2", reportMap.get("identifyYear"));
            content.put("identifyMonth2", reportMap.get("identifyMonth"));
            content.put("identifyDay2", reportMap.get("identifyDay"));


            changer.replaceBookMark(content);
            //报告生成位置
            String examUserId = examUser.getId();
            String paperId = examUser.getPaperId();
            String url = examUserId;
            if (com.jeesite.common.lang.StringUtils.isBlank(examUserId)) {
                url = paperId;
            }
            //图片默认存储路径，读取picture.properties
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config/picture");
            String prefix = bundle.getString("url");
            String filePath = prefix + "exam/" + url + "/";
            String generateUrl = filePath + "二手车鉴定评估报告.docx";
            File destFile = new File(generateUrl);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            //保存文件路径
            PictureUser picture;
            PictureUser pictureUser = new PictureUser();
            pictureUser.setExamUserId(examUser.getId());
            pictureUser.setPaperId(examUser.getPaperId());
            pictureUser.setPictureTypeId("1152467158926442496"); //二手车鉴定评估报告
            picture = pictureUserService.getByEntity(pictureUser);
            if (null != picture) {
                pictureUser = picture;
            }
            pictureUser.setUrl("exam/" + url + "/二手车鉴定评估报告.docx");
            pictureUserService.save(pictureUser);
            //保存替换后的WORD
            changer.saveAs(generateUrl);
        } catch (Exception e) {

        }

    }

    /**
     * 报告下载功能
     *
     * @param request
     * @param response
     * @param examUser
     * @return
     */
    @Transactional
    public void getWord(HttpServletRequest request, HttpServletResponse response, ExamUser examUser) {
        PictureUser pictureUser = new PictureUser();
        pictureUser.setExamUserId(examUser.getId());
        pictureUser.setPaperId(examUser.getPaperId());
        pictureUser.setPictureTypeId("1152467158926442496"); //二手车鉴定评估报告
        pictureUser = pictureUserService.getByEntity(pictureUser);
//        DelegateUser delegateUser = new DelegateUser();
//        delegateUser.setExamUserId(examUser.getId());
//        delegateUser.setPaperId(examUser.getPaperId());
//        delegateUser = delegateUserService.getByEntity(delegateUser);
        String name = "二手车鉴定评估报告";
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config/picture");
        String prefix = bundle.getString("url");
        String url = prefix + pictureUser.getUrl();
        DownloadWordUtils.downloadWord(request, response, url, name, "docx");

    }


}


