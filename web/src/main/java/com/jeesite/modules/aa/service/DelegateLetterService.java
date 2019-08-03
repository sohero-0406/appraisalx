/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.aa.service;

import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.utils.MoneyUtils;
import com.jeesite.common.utils.word.WordExport;
import com.jeesite.modules.aa.dao.DelegateLetterDao;
import com.jeesite.modules.aa.entity.*;
import com.jeesite.modules.aa.vo.AppraisalReportVO;
import com.jeesite.modules.aa.vo.DelegateLetterVO;
import com.jeesite.modules.common.entity.Exam;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.entity.VehicleInfo;
import com.jeesite.modules.common.service.ExamService;
import com.jeesite.modules.common.service.VehicleInfoService;
import com.jeesite.modules.sys.utils.DictUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 委托书信息Service
 * @author lvchangwei
 * @version 2019-07-18
 */
@Service
@Transactional(readOnly=true)
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
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private ExamService examService;
	
	/**
	 * 获取单条数据
	 * @param delegateLetter
	 * @return
	 */
	@Override
	public DelegateLetter get(DelegateLetter delegateLetter) {
		return super.get(delegateLetter);
	}
	
	/**
	 * 查询分页数据
	 * @param delegateLetter 查询条件
	 * @return
	 */
	@Override
	public Page<DelegateLetter> findPage(DelegateLetter delegateLetter) {
		return super.findPage(delegateLetter);
	}
	
	/**
	 * 保存数据（插入或更新）
	 * @param delegateLetter
	 */
	@Override
	@Transactional(readOnly=false)
	public void save(DelegateLetter delegateLetter) {
		super.save(delegateLetter);
	}
	
	/**
	 * 更新状态
	 * @param delegateLetter
	 */
	@Override
	@Transactional(readOnly=false)
	public void updateStatus(DelegateLetter delegateLetter) {
		super.updateStatus(delegateLetter);
	}
	
	/**
	 * 删除数据
	 * @param delegateLetter
	 */
	@Override
	@Transactional(readOnly=false)
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
		if(StringUtils.isNotBlank(delegateUser.getCompleteDate())){
			String[] completeDateArr = delegateUser.getCompleteDate().split("-");
			delegateUser.setCompleteDate(completeDateArr[0] + "年" + completeDateArr[1] + "月" + completeDateArr[2] + "日");
		}
		delegateLetterVO.setDelegateUser(delegateUser);

		CarInfo carInfo = new CarInfo();
		carInfo.setExamUserId(examUser.getExamId());
		carInfo.setPaperId(examUser.getPaperId());
		carInfo = carInfoService.getByEntity(carInfo);
		carInfo.setColor(DictUtils.getDictLabel("aa_vehicle_color",carInfo.getLevel(),""));
		if(StringUtils.isNotBlank(carInfo.getRegisterDate())){
			String[] registerDate = carInfo.getRegisterDate().split("-");
			carInfo.setRegisterDate(registerDate[0] + "年" + registerDate[1] + "月" + registerDate[2] + "日");
		}
		if(StringUtils.isNotBlank(carInfo.getYearCheckDue())){
			String[] yearCheckDue = carInfo.getYearCheckDue().split("-");
			carInfo.setYearCheckDue(yearCheckDue[0] + "年" + yearCheckDue[1] + "月" + yearCheckDue[2] + "日");
		}
		if(StringUtils.isNotBlank(carInfo.getPurchaseDate())){
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
     *  生成鉴定评估报告编号
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
        if(StringUtils.isBlank(delegateUser.getAppraisalDate())){
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            delegateUser.setAppraisalDate(df.format(new Date()));
            Integer appraisalNum;
            Integer max = findAppraisalNumMAX(delegateUser.getAppraisalDate());
            if(null!=max){
                appraisalNum = max + 1;
            }else{
                appraisalNum = 1;
            }
            delegateUser.setAppraisalNum(String.format("%8d", appraisalNum).replace(" ", "0"));
            delegateUserService.save(delegateUser);
        }
    }


    /**
     * 查询最大鉴定评估报告编号
     */
    public Integer findAppraisalNumMAX(String year){
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

        //鉴定报告
//		AppraisalReport appraisalReport = new AppraisalReport();
//		appraisalReport.setExamUserId(examUser.getId());
//		appraisalReport.setPaperId(examUser.getPaperId());
//		appraisalReport = dao.getByEntity(appraisalReport);
//		if (StringUtils.isNotBlank(appraisalReport.getBaseDateEnd())) {
//			String[] baseDateEndArr = appraisalReport.getBaseDateEnd().substring(0, 10).split("-");
//			appraisalReport.setBaseDateEnd(baseDateEndArr[0] + "年" + baseDateEndArr[1] + "月" + baseDateEndArr[2] + "日");
//		}
//		appraisalReportVO.setAppraisalReport(appraisalReport);

        //委托人
        DelegateUser delegateUser = new DelegateUser();
        delegateUser.setExamUserId(examUser.getId());
        delegateUser.setPaperId(examUser.getPaperId());
        delegateUser = delegateUserService.getByEntity(delegateUser);
        appraisalReportVO.setDelegateUser(delegateUser);
        appraisalReportVO.setCarOwner(delegateUser.getName());

        //车辆信息
        CarInfo carInfo = new CarInfo();
        carInfo.setExamUserId(examUser.getId());
        carInfo.setPaperId(examUser.getPaperId());
        carInfo = carInfoService.getByEntity(carInfo);
        carInfo.setColor(DictUtils.getDictLabel("aa_vehicle_color",carInfo.getLevel(),""));
        if (StringUtils.isNotBlank(carInfo.getRegisterDate())) {
            String[] registerDateArr = carInfo.getRegisterDate().substring(0, 10).split("-");
            carInfo.setRegisterDate(registerDateArr[0] + "年" + registerDateArr[1] + "月" + registerDateArr[2] + "日");
        }
        if (StringUtils.isNotBlank(carInfo.getYearCheckDue())) {
            String[] yearCheckDueArr = carInfo.getYearCheckDue().substring(0, 10).split("-");
            carInfo.setYearCheckDue(yearCheckDueArr[0] + "年" + yearCheckDueArr[1] + "月");
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
        for (IdentifyTec temp : identifyTecList) {
            defectDescription.append(temp.getDescription());
        }

        //车辆等级评定
        VehicleGradeAssess vehicleGradeAssess = new VehicleGradeAssess();
        vehicleGradeAssess.setExamUserId(examUser.getId());
        vehicleGradeAssess.setPaperId(examUser.getPaperId());
        vehicleGradeAssess = vehicleGradeAssessService.getByEntity(vehicleGradeAssess);
        //设置技术状况
        vehicleGradeAssess.setTechnicalStatus(vehicleGradeAssessService.getTechnicalStatus(vehicleGradeAssess));
//        if (StringUtils.isNotBlank(vehicleGradeAssess.getIdentifyDate())) {
//            String[] identifyDateArr = vehicleGradeAssess.getIdentifyDate().substring(0, 10).split("-");
//            vehicleGradeAssess.setIdentifyDate(identifyDateArr[0] + "年" + identifyDateArr[1] + "月" + identifyDateArr[2] + "日");
//        }
        appraisalReportVO.setVehicleGradeAssess(vehicleGradeAssess);
        defectDescription.append(vehicleGradeAssess.getDescription());
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

        //委托书信息
        DelegateLetter delegateLetter = new DelegateLetter();
        if(StringUtils.isNotBlank(examUser.getPaperId())){
            delegateLetter.setPaperId(examUser.getPaperId());
        }else {
            Exam exam = new Exam();
            exam.setId(examUser.getExamId());
            exam = examService.getByEntity(exam);
            delegateLetter.setPaperId(exam.getPaperId());
        }
        delegateLetter = delegateLetterService.getByEntity(delegateLetter);
        appraisalReportVO.setDelegateLetter(delegateLetter);

        //车辆配置全表
        VehicleInfo vehicleInfo = vehicleInfoService.getCarModel(carInfo.getModel());
        appraisalReportVO.setVehicleInfo(vehicleInfo);

        return appraisalReportVO;
    }


    /**
     * 整理报告内容
     */
    public Map appraisalReportInfo(ExamUser examUser){
        Map<String,Object> returnMap = new HashMap<>();
        AppraisalReportVO appraisalReportVO = this.findAppraisalReport(examUser);
        //一、绪言
        returnMap.put("organizationName",appraisalReportVO.getDelegateLetter().getOrganizationName());//机构
        returnMap.put("name",appraisalReportVO.getDelegateUser().getName());                  //接受委托\委托方
        returnMap.put("licensePlateNum",appraisalReportVO.getCarInfo().getLicensePlateNum());//牌号
        //returnMap.put(""); //（来自作业表）所表现的市场价值 ？？？？？？
        //二、委托方
        //委托方 同上name
        returnMap.put("contact",appraisalReportVO.getDelegateUser().getContact());//委托方联系人
        returnMap.put("phone",appraisalReportVO.getDelegateUser().getPhone());//联系电话
        //车主姓名 同上name
        //三、评定基准日
        //returnMap.put(""); //鉴定评估基准日 ?????
        //四、鉴定评估车辆信息
        CarInfo carInfo = appraisalReportVO.getCarInfo();
        returnMap.put("labelType",carInfo.getLevel());//厂牌型号
        //拍照号码同上
        returnMap.put("engineNum",carInfo.getEngineNum());//发动机号码
        returnMap.put("vinCode",carInfo.getVinCode()); //车辆识别代号／车架号
        returnMap.put("color",carInfo.getColor());
        returnMap.put("mileage",carInfo.getMileage()); //表征里程 (行驶里程)
        returnMap.put("registerDate",carInfo.getRegisterDate());  //注册登记日期(初次登记日期)
        returnMap.put("yearCheckDue",carInfo.getYearCheckDue());//年审检验合格有效期
        //returnMap.put(); //交强险截止日期
        returnMap.put("check3","1".equals(appraisalReportVO.getCheckTradableVehicles().getCheck3())?"是":"否");//是否查封、抵押车辆
        //returnMap.put("");

        //车船税截止日期
        returnMap.put("carTaxEndDateYear",""); //年
        returnMap.put("carTaxEndDateMonth","");//月
        // 车辆购置税（费）证
        returnMap.put("vehiclePurchaseTax","");
        // 机动车登记证书
        returnMap.put("vehicleRegistration","");
        // 机动车行驶证
        returnMap.put("vehicleLicense","");
        SimpleDateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat dfM = new SimpleDateFormat("MM");
        List<VehicleDocumentInfo> vehicleDocumentInfoList = appraisalReportVO.getVehicleDocumentInfolist();
        for(VehicleDocumentInfo vehicleDocumentInfo:vehicleDocumentInfoList){
            //车船税截止日期
            if("3".equals(vehicleDocumentInfo.getProject())
                    && StringUtils.isNotBlank(vehicleDocumentInfo.getValidity()) ){
                returnMap.replace("carTaxEndDateYear",df.format(vehicleDocumentInfo.getValidity())); //年
                returnMap.replace("carTaxEndDateMonth",dfM.format(vehicleDocumentInfo.getValidity()));//月
                break;
            }
            if("8".equals(vehicleDocumentInfo.getProject()) && StringUtils.isNotBlank(vehicleDocumentInfo.getState())){
                // 车辆购置税（费）证
                returnMap.replace("vehiclePurchaseTax",("1".equals(vehicleDocumentInfo.getState())?"是":"否"));
                break;
            }
            //机动车登记证书
            if("2".equals(vehicleDocumentInfo.getProject()) && StringUtils.isNotBlank(vehicleDocumentInfo.getState())){
                returnMap.replace("vehicleRegistration",("1".equals(vehicleDocumentInfo.getState())?"是":"否"));
                break;
            }
            // 机动车行驶证
            if("1".equals(vehicleDocumentInfo.getProject()) && StringUtils.isNotBlank(vehicleDocumentInfo.getState())){
                returnMap.put("vehicleLicense",("1".equals(vehicleDocumentInfo.getState())?"是":"否"));
                break;
            }
        }
        returnMap.put("trafficIllegalRecord","1".equals(appraisalReportVO.getCheckTradableVehicles().getTrafficIllegalRecord())?"有":"无");//未接受处理的交通违法记录
        //使用性质 ？？？？？？？？？？
        //五、技术鉴定结果
        returnMap.put("defectDescription",appraisalReportVO.getDefectDescription());//技术状况缺陷描述
        returnMap.put("","");//重要配置及参数信息
        returnMap.put("","");//技术状况鉴定等级
        returnMap.put("","");//等级描述
        // 六、价值评估
        returnMap.put("type",appraisalReportVO.getCalculate().getType());//价值估算方法
        String price = calculateService.getEstimateByType(examUser.getUserId(),examUser.getPaperId()).get("price");
        returnMap.put("price",price);
        returnMap.put("bigPrice", MoneyUtils.change(Double.valueOf(price)));
        //计算过程
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        //八、鉴定评估报告法律效力
        Date identifyDate = null;
        try{
            identifyDate  = formatter.parse(appraisalReportVO.getVehicleGradeAssess().getIdentifyDate());
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(identifyDate);
            calendar.add(calendar.DATE,90);
            identifyDate = calendar.getTime();
        }catch (Exception e){
        }
        returnMap.put("identifyYear",String.format("%tY", identifyDate));
        returnMap.put("identifyMonth",String.format("%tm", identifyDate));
        returnMap.put("identifyDay",String.format("%td", identifyDate));
        return returnMap;
    }



    /***
     * 生成鉴定报告
     * 生成鉴定评估报告
     */
    public void generateLetter(ExamUser examUser) {

        Map<String,String> reportMap = this.appraisalReportInfo(examUser);

        try{
            long startTime = System.currentTimeMillis();
            WordExport changer = new WordExport();
            String fileName = "E:/word.docx";
            System.out.println(fileName);
            changer.setTemplate(fileName);
            Map<String, String> content = new HashMap<String, String>();
            content.put("organizationName",reportMap.get("organizationName"));
            content.put("name",reportMap.get("name"));
            content.put("licensePlateNum",reportMap.get("licensePlateNum"));
//
//
//            content.put("Principles", "格式规范、标准统一、利于阅览");
//            content.put("Purpose", "规范会议操作、提高会议质量");
//            content.put("Scope", "公司会议、部门之间业务协调会议");
//
//            content.put("customerName", "**有限公司");
//            content.put("address", "机场路2号");
//
//            content.put("tradeName", "水泥制造");
//            content.put("price1", "1.085");
//		Map<String,Object> picture1 = new HashMap<String, Object>();
//		picture1.put("width", 100);
//		picture1.put("height", 150);
//		picture1.put("type", "jpg");
//		picture1.put("content", inputStream2ByteArray(new FileInputStream("E:\\picture\\21.jpg"), true));
//		content.put("price1", picture1);

//            content.put("price2", "0.906");
//            content.put("price3", "0.433");
//            content.put("numPrice", "0.675");
//
//            content.put("company_name", "**有限公司");
//            content.put("company_address", "机场路2号");
//
//            content.put("pictureimg", "E:/picture/21.jpg");
            changer.replaceBookMark(content);

            //替换表格标签
            List<Map<String, String>> content2 = new ArrayList<Map<String, String>>();
            Map<String, String> table1 = new HashMap<String, String>();

            table1.put("MONTH", "*月份");
            table1.put("SALE_DEP", "75分");
            table1.put("TECH_CENTER", "80分");
            table1.put("CUSTOMER_SERVICE", "85分");
            table1.put("HUMAN_RESOURCES", "90分");
            table1.put("FINANCIAL", "95分");
            table1.put("WORKSHOP", "80分");
            table1.put("TOTAL", "85分");

            for (int i = 0; i < 3; i++) {
                content2.add(table1);
            }
//		changer.fillTableAtBookMark("Table", content2);
//		changer.fillTableAtBookMark("month", content2);
            //表格中文本的替换
            Map<String, String> table = new HashMap<>();
            table.put("CUSTOMER_NAME", "**有限公司");
            table.put("ADDRESS", "机场路2号");
            table.put("USER_NO", "3021170207");
            table.put("tradeName", "水泥制造");
            table.put("PRICE_1", "1.085");
            table.put("PRICE_2", "0.906");
            table.put("PRICE_3", "0.433");
            table.put("NUM_PRICE", "0.675");
//            changer.replaceText(table, "Table2");
            //保存替换后的WORD
            changer.saveAs(fileName + "_out.docx");
            System.out.println("time==" + (System.currentTimeMillis() - startTime));
        }catch (Exception e){

        }


    }


}