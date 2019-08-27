package com.jeesite.modules.aa.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.jwt.JwtUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.service.SignInService;
import com.jeesite.modules.aa.vo.BaseVO;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.common.utils.UserUtils;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * loginController
 *
 * @author lvchangwei
 * @version 2019-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/signIn")
public class SignInController {

    @Autowired
    private SignInService signInService;

    /**
     * 考生端登录
     */
    @RequestMapping(value = "stulogin")
    @ResponseBody
    public CommonResult stulogin(LoginVO vo) {
        return signInService.stuLogin(vo);
    }

    /**
     * 登录超时返回
     */
    @RequestMapping(value = "timeout")
    @ResponseBody
    public CommonResult timeout() {
        return new CommonResult(CodeConstant.LOGIN_TIMEOUT, "登录超时");
    }


    /**
     * 生成登陆二维码
     */
    @RequestMapping(value = "generateQrCode")
    @ResponseBody
    public CommonResult generateQrCode() {
        CommonResult comRes = new CommonResult();
        String uuid = UUID.randomUUID().toString();
        String url = "localhost:8980/appraisal/test/testData/getDate3" + "?uuid=" + uuid;
        int width = 300;
        int height = 300;
        String logoPath = "E:/ray.jpg";
        BaseVO baseVO = new BaseVO();
        baseVO.setBase(signInService.generateQrCode(url, width, height, logoPath).replaceAll("\r|\n", ""));
        baseVO.setUuid(uuid);
        comRes.setData(baseVO);
        return comRes;

    }

    /**
     * 扫码
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "sweepTheYard")
    @ResponseBody
    public CommonResult sweepTheYard(String uuid) {
        ExamUser examUser = UserUtils.getExamUser();
        CacheUtils.put("uuid",uuid, examUser);
        return new CommonResult();
    }

    /**
     * 二维码请求登陆
     *
     * @param uuid
     * @return
     */
    @RequestMapping(value = "sweepCodeLanding")
    @ResponseBody
    public CommonResult sweepCodeLanding(String uuid) {
        CommonResult comRes = new CommonResult();
        ExamUser examUser = CacheUtils.get("uuid",uuid);
        //判断是否为空，并判断是否存在
        if (null == examUser) {
            comRes.setCode(CodeConstant.REQUEST_FAILED);
            comRes.setMsg("请扫码登录!");
            return comRes;
        }
        if (StringUtils.isBlank(examUser.getUserId())) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
            comRes.setMsg("请求用户不存在!");
            return comRes;
        }

        CommonResult result = signInService.judgmentExist(examUser.getUserId());
        if(!CodeConstant.REQUEST_SUCCESSFUL.equals(result.getCode())){
            return result;
        }
        if(null==result.getData()){
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
            comRes.setMsg("请求用户不存在!");
            return comRes;
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("token", examUser.getToken());
        returnMap.put("roleType",((JSONObject)result.getData()).getString("isExamRight")); //教师登录才有
        returnMap.put("roleId",((JSONObject)result.getData()).getString("roleId")); //教师登录才有
        comRes.setData(returnMap);
        CacheUtils.remove("uuid",uuid);
        return comRes;

    }


    /**
     * 教师端登陆
     *
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "teacherlogin")
    @ResponseBody
    public CommonResult login(String userName, String password) {
        CommonResult comRes = new CommonResult();
        Map<String, String> map = new HashMap<>();
        map.put("userName", userName);
        map.put("password", password);
        CommonResult teacherSide = signInService.commonuserTeacherSideLogin(ServiceConstant.COMMONUSER_TEACHER_SIDE_LOGIN, map);
        if (!CodeConstant.REQUEST_SUCCESSFUL.equals(teacherSide.getCode())) {
            return teacherSide;
        }
        ExamUser examUser = new ExamUser();
        JSONObject json = JSONObject.parseObject(teacherSide.getData().toString());
        if (StringUtils.isBlank(json.toString())) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
            comRes.setMsg("用户不存在!");
            return comRes;
        }
        if ("3".equals(json.getString("roleId"))) {
            comRes.setCode(CodeConstant.WRONG_REQUEST_PARAMETER);
            comRes.setMsg("考生不允许登录!");
            return comRes;
        }
        examUser.setUserId(json.getString("id"));
        examUser.setRoleType(json.getString("roleId"));
        String token = JwtUtils.generateToken(examUser.getUserId());
        examUser.setToken(token);
        CacheUtils.put("examUser", examUser.getUserId(), examUser);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("roleType", json.getString("isExamRight"));
        returnMap.put("token", token);
        comRes.setData(returnMap);
        return comRes;
    }


    /**
     * 教师 注销登录
     */
    @RequestMapping(value = "cancellation")
    @ResponseBody
    public CommonResult cancellation(){
        ExamUser examUser = UserUtils.getExamUser();
        String userId = examUser.getUserId();
        CacheUtils.remove("examUser",userId);
        return new CommonResult();
    }


}
