package com.jeesite.modules.aa.web;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.service.SignInService;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
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
        return new CommonResult(CodeConstant.LOGIN_TIMEOUT,"登录超时");
    }


    /**
     * 生成登陆二维码
     */
    @RequestMapping(value = "generateQrCode")
    @ResponseBody
    public String  generateQrCode(){
        String uuid = UUID.randomUUID().toString();
        String url = "localhost:8980/appraisal/test/testData/getDate3"+"?uuid="+uuid;
        int width = 300;
        int height = 300;
        String logoPath = "E:/ray.jpg";
        return signInService.generateQrCode(url,width,height,logoPath);

    }

    /**
     * 扫码
     * @param uuid
     * @return
     */
    @RequestMapping(value = "sweepTheYard")
    @ResponseBody
    public CommonResult  sweepTheYard(String uuid) {
        ExamUser examUser = UserUtils.getExamUser();
        CacheUtils.put("sysCache",uuid,examUser);
        return new CommonResult();
    }

    /**
     * 二维码请求登陆
     * @param uuid
     * @return
     */
    @RequestMapping(value = "sweepCodeLanding")
    @ResponseBody
    public CommonResult  getUUid(String uuid) {
        CommonResult comRes = new CommonResult();
        ExamUser examUser = CacheUtils.get(uuid);
        comRes.setData(examUser);
        ServletUtils.getRequest().setAttribute("examUser",examUser);
        return comRes;
    }


}
