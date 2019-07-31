package com.jeesite.modules.aa.web;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.modules.aa.service.SignInService;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
