package com.jeesite.common.constant;

/**
 *  接口返回状态码
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
    //请求失败
    public static final String REQUEST_FAILED = "1010";

    //考试结束
    public static final String EXAM_END = "1011";

}
