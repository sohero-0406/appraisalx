package com.jeesite.modules.aa.service;

import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.service.ExamUserService;
import com.jeesite.modules.common.service.HttpClientService;
import com.jeesite.modules.common.service.VehicleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SignInService {

    @Autowired
    private CarInfoService carInfoService;
    @Autowired
    private PictureUserService pictureUserService;
    @Autowired
    private VehicleGradeAssessService vehicleGradeAssessService;
    @Autowired
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private PaperService paperService;
    @Autowired
    private ExamUserService examUserService;
    @Autowired
    private HttpClientService httpClientService;

    /**
     * 考生端登录
     *
     * @param vo
     * @return
     */
    public CommonResult stuLogin(LoginVO vo) {
        CommonResult result = new CommonResult();
        String userName = vo.getUserName();
        String password = vo.getPassword();
        ExamUser examUser = new ExamUser();
        examUser.setUserNum(userName);
        examUser = examUserService.getByEntity(examUser);
        if (null == examUser || !password.equals(examUser.getPassword())) {
            result.setCode(CodeConstant.INCORRECT_USER_NAME_OR_PASSWORD);
            result.setMsg("用户名或密码不正确");
            return result;
        }
        ExamUser sessionUser = new ExamUser();
        sessionUser.setId(examUser.getId());
        sessionUser.setUserId(examUser.getUserId());
        sessionUser.setExamId(examUser.getExamId());
        sessionUser.setStartTime(examUser.getStartTime());
        ServletUtils.getRequest().getSession().setAttribute("examUser", examUser);
        return result;
    }
}
