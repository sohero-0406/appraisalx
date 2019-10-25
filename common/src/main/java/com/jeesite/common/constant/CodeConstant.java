package com.jeesite.common.constant;

/**
 * 接口返回状态码
 */
public class CodeConstant {

    //请求成功
    public static final String REQUEST_SUCCESSFUL = "1000";
    //用户名或密码不正确
    public static final String INCORRECT_USER_NAME_OR_PASSWORD = "1001";
    //识别成功
    public static final String IDENTIFY_THE_SUCCESSFUL = "1002";
    //识别失败，图片未摆正
    public static final String FAILED_TO_IDENTIFY_PICTURE_NOT_ALIGNED = "1003";
    //识别失败，上传的图片中不包含指定信息
    public static final String FAILED_TO_IDENTIFY_UPLOADED_IMAGE_NOT_CONTAIN_INFORMATION = "1004";
    //识别失败，图片模糊
    public static final String FAILED_TO_IDENTIFY_PICTURE_FUZZY = "1005";
    //识别失败，图片上关键字段反光或过曝
    public static final String FAILED_TO_IDENTIFY_REFLECTIVE_OR_OVEREXPOSED_ON_IMAGE = "1006";
    //识别失败，未知状态
    public static final String FAILED_TO_IDENTIFY_UNKNOWN_STATE = "1007";
    //识别失败，原因未知
    public static final String FAILED_TO_IDENTIFY = "1008";
    //请求失败
    public static final String REQUEST_FAILED = "1010";
    //考试结束
    public static final String EXAM_END = "1011";
    //未完善被评估车辆及参照物数据表
    public static final String REFERENCES_EVALUATED_INCOMPLETE = "1012";
    //参照物不存在
    public static final String REFERENCE_NOT_EXIST = "1013";
    //不存在正在进行的考试
    public static final String EXAM_NO_ONGOING = "1014";
    //存在不合理的考生状态
    public static final String UNREASONABLE_CANDIDATE_STATUS = "1015";
    //所查数据不存在
    public static final String DATA_NOT_FOUND = "1016";
    //上传失败，图片不可多于三张
    public static final String UPLOAD_FAIL_THREE = "1017";
    //当前试卷已被启用
    public static final String PAPER_ENABLED = "1018";
    //请求参数有误
    public static final String WRONG_REQUEST_PARAMETER = "1020";
    //登录超时
    public static final String LOGIN_TIMEOUT = "2000";
    //您未注册或者系统没有检测到硬件信息，或者您破坏了注册信息
    public static final String REGISTE_INFO_ERROR = "3000";
}
