package com.jeesite.common.utils;

import com.baidu.aip.ocr.AipOcr;
import com.jeesite.common.constant.CodeConstant;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 百度AI之文字识别util
 * <p>
 * 可识别的图片类型包括：身份证正面、车牌照、车辆识别代号（VIN）、
 * 机动车行驶证正页、机动车登记证1、机动车销售统一发票
 * </p>
 * <p>https://cloud.baidu.com/product/ocr/cards</p>
 *
 * @author Administrator
 */
public class BaiDuAiUtil {
    /**
     * 身份证人像面类型id
     */
    private static final String PICTURE_TYPE_ID_ID_FRONT = "1143431700863209472";
    /**
     * 车牌照类型id
     */
    private static final String PICTURE_TYPE_ID_PLATE_LICENSE = "1143431962274000896";
    /**
     * vin类型id
     */
    private static final String PICTURE_TYPE_ID_VIN = "1143432423932891136";
    /**
     * 机动车行驶证正页类型id
     */
    private static final String PICTURE_TYPE_ID_VEHICLE_LICENSE = "1143432856340893696";
    /**
     * 机动车登记证1类型id
     */
    private static final String PICTURE_TYPE_ID_VEHICLE_REGISTRATION = "1143435514869673984";
    /**
     * 机动车销售统一发票类型id
     */
    private static final String PICTURE_TYPE_ID_NEW_CAR_INVOICE = "1143435831549947904";
    private static final String BAIDU_AI_APP_ID = "16970638";
    private static final String BAIDU_AI_API_KEY = "LgyBU72NmZwgLWGjyQ1Gv64k";
    private static final String BAIDU_AI_SECRET_KEY = "oIzu2MduVjte6T3DyOe8ynHAgSe5I8vm";
    /**基础地址*/
//	private static final String BAIDU_AI_BASE_HOST = "https://aip.baidubce.com/rest/2.0/ocr/v1/";
    /**
     * 自定义模版id之车辆铭牌
     */
    private static final String VEHICLE_NAMEPLATE = "a54066f45faae926ba07d4d780b750ac";
    /**
     * 自定义模版id之机动车销售统一发票
     */
    private static final String NEW_CAR_INVOICE = "c26279131caf92aba9fac3a3c8ffd10d";
    /**
     * 自定义模版id之机动车登记证
     */
    private static final String VEHICLE_REGISTRATION = "97bcb88f366a5125e0bc943511dc4607";
    private AipOcr client;

    public BaiDuAiUtil() {
        client = new AipOcr(BAIDU_AI_APP_ID, BAIDU_AI_API_KEY, BAIDU_AI_SECRET_KEY);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
    }

    /**
     * 根据图片类型id识别图片信息
     *
     * @param pictureTypeId 图片类型id
     * @param imagePath     图片路径
     * @return
     */
    public String discern(String pictureTypeId, String imagePath) {
        switch (pictureTypeId) {
            case PICTURE_TYPE_ID_ID_FRONT://身份证人像面识别
                return idCard(imagePath, true).toString();
            case PICTURE_TYPE_ID_VIN://vin识别
                return vinCode(imagePath).toString();
            case PICTURE_TYPE_ID_PLATE_LICENSE://车牌照识别
                return plateLicense(imagePath).toString();
            case PICTURE_TYPE_ID_VEHICLE_LICENSE://机动车行驶证识别
                return vehicleLicense(imagePath).toString();
            case PICTURE_TYPE_ID_VEHICLE_REGISTRATION://机动车登记证1识别
                return vehicleRegstration(imagePath).toString();
            case PICTURE_TYPE_ID_NEW_CAR_INVOICE://机动车统一销售发票识别
                return newCarInvoice(imagePath).toString();
        }
        JSONObject result = new JSONObject();
        result.put("code", CodeConstant.FAILED_TO_IDENTIFY);
        result.put("msg", "识别失败，该图片不在识别范围内！");
        return result.toString();
    }

    /**
     * VIN码识别
     */
    public JSONObject vinCode(String imagePath) {
        JSONObject jsonObj = client.vinCode(imagePath, new HashMap<String, String>());
        JSONObject result = new JSONObject();
        if (jsonObj.optJSONArray("words_result").length() > 0) {
            result.put("code", CodeConstant.IDENTIFY_THE_SUCCESSFUL);
            result.put("msg", "识别成功");
            result.put("vinCode", jsonObj.optJSONArray("words_result").optJSONObject(0).optString("words"));
        } else {
            result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UNKNOWN_STATE);
            result.put("msg", "未知状态");
        }
        return result;
    }

    /**
     * 身份证识别
     *
     * @param imagePath
     * @param isFront
     * @return
     */
    private JSONObject idCard(String imagePath, boolean isFront) {
        String idCardSide = isFront ? "front" : "back";
        JSONObject jsonObj = client.idcard(imagePath, idCardSide, new HashMap<String, String>());
        JSONObject result = new JSONObject();
        switch (jsonObj.optString("image_status")) {
            case "normal"://识别正常
                result.put("code", CodeConstant.IDENTIFY_THE_SUCCESSFUL);
                result.put("msg", "识别成功");
                result.put("name", jsonObj.optJSONObject("words_result").optJSONObject("姓名").optString("words"));
                result.put("national", jsonObj.optJSONObject("words_result").optJSONObject("民族").optString("words"));
                result.put("address", jsonObj.optJSONObject("words_result").optJSONObject("住址").optString("words"));
                result.put("idNum", jsonObj.optJSONObject("words_result").optJSONObject("公民身份号码").optString("words"));
                result.put("birthday", jsonObj.optJSONObject("words_result").optJSONObject("出生").optString("words"));
                result.put("gender", jsonObj.optJSONObject("words_result").optJSONObject("性别").optString("words"));
                break;
            case "reversed_side":
                result.put("code", CodeConstant.FAILED_TO_IDENTIFY_PICTURE_NOT_ALIGNED);
                result.put("msg", "未摆正身份证");
                break;
            case "non_idcard":
                result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UPLOADED_IMAGE_NOT_CONTAIN_INFORMATION);
                result.put("msg", "上传的图片中不包含身份证");
                break;
            case "blurred":
                result.put("code", CodeConstant.FAILED_TO_IDENTIFY_PICTURE_FUZZY);
                result.put("msg", "身份证模糊");
                break;
            case "over_exposure":
                result.put("code", CodeConstant.FAILED_TO_IDENTIFY_REFLECTIVE_OR_OVEREXPOSED_ON_IMAGE);
                result.put("msg", "身份证关键字段反光或过曝");
                break;
            case "unknown":
                result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UNKNOWN_STATE);
                result.put("msg", "未知状态");
                break;
        }
        return result;
    }

    /**
     * 行驶证识别
     *
     * @param imagePath
     * @return
     */
    private JSONObject vehicleLicense(String imagePath) {
        JSONObject jsonObj = client.vehicleLicense(imagePath, new HashMap<String, String>());
        JSONObject result = new JSONObject();
        if (null != jsonObj.optJSONObject("words_result") && jsonObj.optJSONObject("words_result").length()>0 ) {
            result.put("code", CodeConstant.IDENTIFY_THE_SUCCESSFUL);
            result.put("msg", "识别成功");
            result.put("engineNum", jsonObj.optJSONObject("words_result").optJSONObject("发动机号码").optString("words"));
        } else {
            result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UNKNOWN_STATE);
            result.put("msg", "未知状态");
        }
        return result;
    }

    /**
     * 驾驶证识别
     *
     * @param imagePath
     * @return
     */
    private JSONObject drivingLicense(String imagePath) {
        return client.drivingLicense(imagePath, new HashMap<String, String>());
    }

    /**
     * 车牌识别
     *
     * @param imagePath
     * @return
     */
    private JSONObject plateLicense(String imagePath) {
        JSONObject jsonObj = client.plateLicense(imagePath, new HashMap<String, String>());
        JSONObject result = new JSONObject();
        if (null != jsonObj.optJSONObject("words_result")) {
            result.put("code", CodeConstant.IDENTIFY_THE_SUCCESSFUL);
            result.put("msg", "识别成功");
            result.put("color", jsonObj.optJSONObject("words_result").optString("color"));
            result.put("number", jsonObj.optJSONObject("words_result").optString("number"));
        } else {
            result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UNKNOWN_STATE);
            result.put("msg", "未知状态");
        }
        return result;
    }

    /**
     * 车辆铭牌识别
     *
     * @param imagePath
     * @return
     */
    private JSONObject vehicleNameplate(String imagePath) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("templateSign", VEHICLE_NAMEPLATE);
        return client.custom(imagePath, options);
    }

    /**
     * 机动车销售统一发票识别
     *
     * @param imagePath
     * @return 样例："{\"error_msg\":\"\",\"data\":{\"ret\":[{\"probability\":{\"average\":0.975991,\"min\":0.631704,\"variance\":0.005813},\"location\":{\"top\":197,\"left\":469,\"width\":22,\"height\":13},\"word_name\":\"产地\",\"word\":\"中国\"},{\"probability\":{\"average\":0.945559,\"min\":0.815,\"variance\":0.023173},\"location\":{\"top\":362,\"left\":225,\"width\":65,\"height\":13},\"word_name\":\"税额\",\"word\":\"15158.62\"},{\"probability\":{\"average\":0.9552,\"min\":0.77926,\"variance\":0.003931},\"location\":{\"top\":61,\"left\":92,\"width\":55,\"height\":18},\"word_name\":\"开票日期\",\"word\":\"019-04-22\"},{\"probability\":{\"average\":0.997525,\"min\":0.977425,\"variance\":2.5E-5},\"location\":{\"top\":220,\"left\":120,\"width\":95,\"height\":12},\"word_name\":\"合格证号\",\"word\":\"WAJ200003670980\"},{\"probability\":{\"average\":0.972823,\"min\":0.778278,\"variance\":0.002579},\"location\":{\"top\":243,\"left\":392,\"width\":110,\"height\":13},\"word_name\":\"车辆识别代号\",\"word\":\"LSJA36U63JZ922386\"},{\"probability\":{\"average\":0.99902,\"min\":1.297004,\"variance\":1.0E-6},\"location\":{\"top\":268,\"left\":443,\"width\":70,\"height\":13},\"word_name\":\"小写\",\"word\":\"109900.00\"},{\"probability\":{\"average\":0.999541,\"min\":0.997831,\"variance\":1.0E-6},\"location\":{\"top\":220,\"left\":287,\"width\":39,\"height\":12},\"word_name\":\"进口证明书号\",\"word\":\"证明书号\"},{\"probability\":{\"average\":0.995482,\"min\":0.969633,\"variance\":9.6E-5},\"location\":{\"top\":195,\"left\":125,\"width\":45,\"height\":14},\"word_name\":\"车辆类型\",\"word\":\"小型轿车\"},{\"probability\":{\"average\":0.985472,\"min\":0.889939,\"variance\":9.15E-4},\"location\":{\"top\":288,\"left\":123,\"width\":98,\"height\":16},\"word_name\":\"销售单位名称\",\"word\":\"北京天祥荣威4S店\"},{\"probability\":{\"average\":0.997433,\"min\":0.977959,\"variance\":3.5E-5},\"location\":{\"top\":291,\"left\":408,\"width\":75,\"height\":10},\"word_name\":\"销售单位电话\",\"word\":\"010-62882269\"},{\"word_name\":\"商检单号\",\"word\":\"\"},{\"probability\":{\"average\":0.998656,\"min\":0.987646,\"variance\":1.1E-5},\"location\":{\"top\":62,\"left\":468,\"width\":56,\"height\":10},\"word_name\":\"发票号码\",\"word\":\"00296365\"},{\"probability\":{\"average\":0.994245,\"min\":0.941512,\"variance\":1.93E-4},\"location\":{\"top\":243,\"left\":122,\"width\":69,\"height\":12},\"word_name\":\"发动机号码\",\"word\":\"16E5E6B0392\"},{\"probability\":{\"average\":0.995444,\"min\":0.887895,\"variance\":4.01E-4},\"location\":{\"top\":169,\"left\":405,\"width\":112,\"height\":13},\"word_name\":\"购买方纳税人识别号\",\"word\":\"110108198710201436\"},{\"probability\":{\"average\":0.998472,\"min\":0.990333,\"variance\":5.0E-6},\"location\":{\"top\":313,\"left\":407,\"width\":120,\"height\":12},\"word_name\":\"销售单位账号\",\"word\":\"6222020001116356293\"},{\"probability\":{\"average\":0.999482,\"min\":0.996523,\"variance\":1.0E-6},\"location\":{\"top\":335,\"left\":360,\"width\":120,\"height\":14},\"word_name\":\"销售单位开户银行\",\"word\":\"工行北京市海淀区支行\"},{\"probability\":{\"average\":0.999397,\"min\":0.99693,\"variance\":1.0E-6},\"location\":{\"top\":335,\"left\":123,\"width\":118,\"height\":12},\"word_name\":\"销售单位地址\",\"word\":\"北京市海淀区创业路99号\"},{\"probability\":{\"average\":0.999298,\"min\":0.998253,\"variance\":1.0E-6},\"location\":{\"top\":364,\"left\":127,\"width\":17,\"height\":10},\"word_name\":\"税率\",\"word\":\"16%\"},{\"probability\":{\"average\":0.975991,\"min\":0.631704,\"variance\":0.005813},\"location\":{\"top\":197,\"left\":269,\"width\":167,\"height\":13},\"word_name\":\"厂牌型号\",\"word\":\"厂牌型号荣威牌CSA7262GDAN产\"},{\"probability\":{\"average\":0.976916,\"min\":0.817902,\"variance\":0.001979},\"location\":{\"top\":312,\"left\":117,\"width\":118,\"height\":17},\"word_name\":\"销售单位纳税人识别号\",\"word\":\"9111010773161975XH\"},{\"probability\":{\"average\":0.979048,\"min\":0.835618,\"variance\":0.002584},\"location\":{\"top\":167,\"left\":123,\"width\":22,\"height\":13},\"word_name\":\"购买方名称/证件号码\",\"word\":\"马辰\"},{\"probability\":{\"average\":0.998635,\"min\":0.986593,\"variance\":1.1E-5},\"location\":{\"top\":47,\"left\":465,\"width\":85,\"height\":12},\"word_name\":\"发票代码\",\"word\":\"111000633220\"},{\"probability\":{\"average\":0.978254,\"min\":0.851138,\"variance\":0.002267},\"location\":{\"top\":355,\"left\":385,\"width\":134,\"height\":27},\"word_name\":\"主管税务机关及代码\",\"word\":\"北京市海淀区国家税务局111010800\"}],\"templateSign\":\"67a038627f86a36821032597c2929cb6\",\"templateName\":\"机动车销售统一发票\",\"scores\":1,\"isStructured\":true,\"logId\":\"156197187930699\",\"clockwiseAngle\":355.68},\"error_code\":0}"
     */
    private JSONObject newCarInvoice(String imagePath) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("templateSign", NEW_CAR_INVOICE);
        JSONObject jsonObj = client.custom(imagePath, options);
        JSONObject result = new JSONObject();

        JSONArray jsonArr = jsonObj.optJSONObject("data").optJSONArray("ret");
        if (null == jsonArr) {
            result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UNKNOWN_STATE);
            result.put("msg", "未知状态");
        } else {
            JSONObject childJsonObj = null;
            result.put("code", CodeConstant.IDENTIFY_THE_SUCCESSFUL);
            result.put("msg", "识别成功");
            for (int i = 0; i < jsonArr.length(); i++) {
                childJsonObj = jsonArr.optJSONObject(i);
                result.put(childJsonObj.optString("word_name"), childJsonObj.optString("word"));
            }
        }
        return result;
    }

    /**
     * 机动车登记证识别（返回数据误差较大）
     * <p>误差原因：</p>
     * <p>1、证件中的数据0带中横线，容易误读（如：总质量、日期）</p>
     * <p>2、数据如果与表格的边框重叠容易导致读取出错（如：制造厂名称、车辆识别代号、发动机型号、轮胎规格等）</p>
     * <p>3、水印会影响数据的读取（如：轴数数据未读出来）</p>
     * <p>4、如果数据以数字1开头，有较大几率读不到1（如：发动机号、外轮廓尺寸高）</p>
     * <p>5、如果数据以数字0结尾，有可能读不到0（如：总质量）</p>
     *
     * @param imagePath
     * @return 样例："{\"error_msg\":\"\",\"data\":{\"ret\":[{\"probability\":{\"average\":0.986349,\"min\":0.946553,\"variance\":4.15E-4},\"location\":{\"top\":2752,\"left\":1145,\"width\":97,\"height\":79},\"word_name\":\"外廓尺寸宽\",\"word\":\"1847\"},{\"probability\":{\"average\":0.999862,\"min\":0.999862,\"variance\":0},\"location\":{\"top\":2021,\"left\":1942,\"width\":35,\"height\":47},\"word_name\":\"车身颜色\",\"word\":\"灰\"},{\"probability\":{\"average\":0.85,\"min\":1.279556,\"variance\":0.046},\"location\":{\"top\":2266,\"left\":2190,\"width\":60,\"height\":72},\"word_name\":\"功率\",\"word\":\"14\"},{\"probability\":{\"average\":0.998805,\"min\":0.996498,\"variance\":3.0E-6},\"location\":{\"top\":2840,\"left\":1996,\"width\":276,\"height\":278},\"word_name\":\"发证机关章\",\"word\":\"天津市公安交通管理局\"},{\"probability\":{\"average\":0.994806,\"min\":0.973188,\"variance\":6.5E-5},\"location\":{\"top\":2744,\"left\":780,\"width\":111,\"height\":75},\"word_name\":\"外廓尺寸长\",\"word\":\"5213\"},{\"probability\":{\"average\":0.999746,\"min\":0.999532,\"variance\":0},\"location\":{\"top\":2304,\"left\":746,\"width\":80,\"height\":47},\"word_name\":\"燃料种类\",\"word\":\"汽油\"},{\"probability\":{\"average\":0.999555,\"min\":0.998044,\"variance\":0},\"location\":{\"top\":1924,\"left\":741,\"width\":267,\"height\":49},\"word_name\":\"车辆类型\",\"word\":\"小型普通客车\"},{\"probability\":{\"average\":0.986891,\"min\":0.962923,\"variance\":2.88E-4},\"location\":{\"top\":3141,\"left\":1568,\"width\":142,\"height\":54},\"word_name\":\"使用性质\",\"word\":\"非营运\"},{\"probability\":{\"average\":0.961337,\"min\":0.461964,\"variance\":0.011751},\"location\":{\"top\":203,\"left\":1053,\"width\":1179,\"height\":71},\"word_name\":\"机动车所有人/身份证明名称/号码\",\"word\":\"天津市硕恒科技展有限公司/组织机构代码证书/57231292\"},{\"probability\":{\"average\":0.995446,\"min\":0.950577,\"variance\":1.47E-4},\"location\":{\"top\":2393,\"left\":780,\"width\":632,\"height\":51},\"word_name\":\"制造厂名称\",\"word\":\"海通用(沈阳)北盛汽车有限公司\"},{\"probability\":{\"average\":0.901017,\"min\":0.901017,\"variance\":0},\"location\":{\"top\":2112,\"left\":762,\"width\":355,\"height\":37},\"word_name\":\"车辆识别代号/车架号\",\"word\":\"SGUD84X2EE077947\"},{\"probability\":{\"average\":0.856277,\"min\":0.433359,\"variance\":0.05962},\"location\":{\"top\":2961,\"left\":741,\"width\":66,\"height\":46},\"word_name\":\"总质量\",\"word\":\"238\"},{\"probability\":{\"average\":0.985129,\"min\":0.772113,\"variance\":0.00172},\"location\":{\"top\":3221,\"left\":1553,\"width\":284,\"height\":65},\"word_name\":\"车辆出厂日期\",\"word\":\"2014-12-11\"},{\"probability\":{\"average\":0.985129,\"min\":0.772113,\"variance\":0.00172},\"location\":{\"top\":3221,\"left\":2152,\"width\":241,\"height\":65},\"word_name\":\"发证日期\",\"word\":\"2015-01-15\"},{\"word_name\":\"登记证编号\",\"word\":\"\"},{\"probability\":{\"average\":0.963452,\"min\":0.722693,\"variance\":0.004468},\"location\":{\"top\":297,\"left\":1580,\"width\":251,\"height\":63},\"word_name\":\"登记日期\",\"word\":\"2015-01-15\"},{\"probability\":{\"average\":0.999828,\"min\":0.999599,\"variance\":0},\"location\":{\"top\":1925,\"left\":1928,\"width\":131,\"height\":51},\"word_name\":\"车辆品牌\",\"word\":\"别克牌\"},{\"probability\":{\"average\":0.963368,\"min\":0.863624,\"variance\":0.003327},\"location\":{\"top\":2754,\"left\":1555,\"width\":91,\"height\":75},\"word_name\":\"外廓尺寸高\",\"word\":\"750\"},{\"probability\":{\"average\":0.999928,\"min\":0.999907,\"variance\":0},\"location\":{\"top\":2114,\"left\":1940,\"width\":82,\"height\":47},\"word_name\":\"国产/进口\",\"word\":\"国产\"},{\"probability\":{\"average\":0.917559,\"min\":0.399845,\"variance\":0.038315},\"location\":{\"top\":2206,\"left\":767,\"width\":176,\"height\":42},\"word_name\":\"发动机号\",\"word\":\"4331337\"},{\"probability\":{\"average\":0.963452,\"min\":0.722693,\"variance\":0.004468},\"location\":{\"top\":297,\"left\":2253,\"width\":172,\"height\":63},\"word_name\":\"机动车登记编号\",\"word\":\"津DSH179\"},{\"probability\":{\"average\":0.999041,\"min\":0.998281,\"variance\":0},\"location\":{\"top\":2393,\"left\":1930,\"width\":131,\"height\":51},\"word_name\":\"转向形式\",\"word\":\"方向盘\"},{\"word_name\":\"轴数\",\"word\":\"\"},{\"word_name\":\"发动机型号\",\"word\":\"\"},{\"probability\":{\"average\":0.85,\"min\":1.279556,\"variance\":0.046},\"location\":{\"top\":3023,\"left\":719,\"width\":60,\"height\":48},\"word_name\":\"核定载客\",\"word\":\"791\"},{\"word_name\":\"后轮距\",\"word\":\"\"},{\"probability\":{\"average\":0.848343,\"min\":0.581859,\"variance\":0.020823},\"location\":{\"top\":2283,\"left\":1918,\"width\":121,\"height\":85},\"word_name\":\"排量\",\"word\":\"2384\"},{\"probability\":{\"average\":0.999997,\"min\":0.999995,\"variance\":0},\"location\":{\"top\":3238,\"left\":732,\"width\":94,\"height\":51},\"word_name\":\"车辆获得方式\",\"word\":\"购买\"},{\"probability\":{\"average\":0.883445,\"min\":0.562881,\"variance\":0.034321},\"location\":{\"top\":2680,\"left\":743,\"width\":81,\"height\":41},\"word_name\":\"轴距\",\"word\":\"3088\"},{\"probability\":{\"average\":0.864317,\"min\":0.864317,\"variance\":0},\"location\":{\"top\":2020,\"left\":748,\"width\":232,\"height\":35},\"word_name\":\"车辆型号\",\"word\":\"SGM6520UAAA\"},{\"probability\":{\"average\":0.993259,\"min\":0.914255,\"variance\":4.81E-4},\"location\":{\"top\":296,\"left\":649,\"width\":437,\"height\":54},\"word_name\":\"登记机关\",\"word\":\"天津市公安交通管理局\"},{\"word_name\":\"前轮距\",\"word\":\"\"},{\"probability\":{\"average\":0.85,\"min\":1.279556,\"variance\":0.046},\"location\":{\"top\":2462,\"left\":1887,\"width\":160,\"height\":48},\"word_name\":\"轮胎数\",\"word\":\"14529619\"},{\"word_name\":\"轮胎规格\",\"word\":\"\"}],\"templateSign\":\"467ffbcb69d52a61085a22226d57cade\",\"templateName\":\"机动车登记证1\",\"scores\":1,\"isStructured\":true,\"logId\":\"156197104119063\",\"clockwiseAngle\":0},\"error_code\":0
     */
    private JSONObject vehicleRegstration(String imagePath) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("templateSign", VEHICLE_REGISTRATION);
        JSONObject jsonObj = client.custom(imagePath, options);
        JSONObject result = new JSONObject();

        JSONArray jsonArr = jsonObj.optJSONObject("data").optJSONArray("ret");
        if (null == jsonArr) {
            result.put("code", CodeConstant.FAILED_TO_IDENTIFY_UNKNOWN_STATE);
            result.put("msg", "未知状态");
        } else {
            result.put("code", CodeConstant.IDENTIFY_THE_SUCCESSFUL);
            result.put("msg", "识别成功");
            JSONObject childJsonObj = null;
            for (int i = 0; i < jsonArr.length(); i++) {
                childJsonObj = jsonArr.optJSONObject(i);
                result.put(childJsonObj.optString("word_name"), childJsonObj.optString("word"));
            }
        }
        return result;
    }
}
