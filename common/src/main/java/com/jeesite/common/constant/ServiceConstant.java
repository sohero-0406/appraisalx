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
}
