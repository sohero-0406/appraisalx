package com.jeesite.common.constant;

/**
 * 调用第三方服务名
 */
public class ServiceConstant {

    //登陆功能
    public static final String COMMONUSER_TEACHER_SIDE_LOGIN ="/common/commonUser/teacherSideLogin";

    //查询车辆配置属性
    public static final String VEHICLEINFO_GET_BY_ENTITY ="/common/vehicleInfo/getByEntity";

    //通过userid 查询学生信息
    public static final String DERIVE_STUDENT_ACHIEVEMENT = "/common/commonUser/loadStuListByIds";

    //通过userid 查询人员信息
    public static final String DERIVE_STUDENT_LOADCOMMONUSER = "/common/commonUser/loadCommonUser";

    //添加学生，查询学生列表
    public static final String COMMONUSER_LOAD_STU_LIST_IN_PLATFORM = "/common/commonUser/loadStuListInPlatform";

    //通过server_exam_user_id 查询考生信息
    public static final String COMMONUSER_LOAD_STU_LIST_BY_EXAM_USER_IDS = "/common/commonUser/loadStuListByExamUserIds";

    //通过品牌查询名称
    public static final String COMMON_VEHICLE_BRAND_GET_BY_ENTITY = "/common/vehicleBrand/getByEntity";

    //通过车系查询名称
    public static final String COMMON_VEHICLE_SERIES_GET_BY_ENTITY = "/common/vehicleSeries/getByEntity";

    //根据车型id查部分车辆配置属性
    public static final String VEHICLEINFO_GET_CAR_MODEL ="/common/vehicleInfo/getCarModel";

    //上传成绩
    public static final String COMMONASSESSMENT_UPLOAD_SCORES = "/common/commonAssessment/uploadScores";

    //加载车辆品牌数据
    public static final String VEHICLEBRAND_FIND_LIST = "/common/vehicleBrand/findList";

    //加载车辆品牌车系数据
    public static final String VEHICLESERIES_FIND_LIST = "/common/vehicleSeries/findList";

    //加载车辆年款型号数据
    public static final String VEHICLEINFO_FIND_LIST = "/common/vehicleInfo/findList";

    //加载考核名称
    public static final String COMMONASSESSMENT_LOAD_ASSESSMENT_NAME_LIST = "/common/commonAssessment/loadAssessmentNameList";

    //加载考核日期
    public static final String COMMONASSESSMENTSTU_LOAD_ASSESSMENT_DATE_LIST = "/common/commonAssessmentStu/loadAssessmentDateList";

    //加载考核时间
    public static final String COMMONASSESSMENTSTU_LOAD_ASSESSMENT_TIME_LIST = "/common/commonAssessmentStu/loadAssessmentTimeList";

    //加载专业
    public static final String COMMONUSER_LOAD_MAJOR_LIST = "/common/commonUser/loadMajorList";

    //加载班级
    public static final String COMMONUSER_LOAD_CLASS_LIST = "/common/commonUser/loadClassList";

    //根据考生id返回信息
    public static final String COMMONASSESSMENTSTU_LOAD_ONE_EXAM_STU = "/common/commonAssessmentStu/loadOneExamStu";
}
