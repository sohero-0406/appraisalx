package com.jeesite.modules.aa.web;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.aa.service.AppraislJobTableService;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.ExamUser;
import com.jeesite.modules.common.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 二手车鉴定评估作业表Controller
 * @author lvchangwei
 * @version 2019-07-23
 */
@Controller
@RequestMapping(value = "${adminPath}/aa/appraisljobtable")
public class AppraislJobTableController extends BaseController {

    @Autowired
    private AppraislJobTableService appraislJobTableService;

    /**
     * 生成鉴定评估作业本
     */
    @RequestMapping(value = "findAppraisalJobTable")
    @ResponseBody
    public CommonResult findAppraisalJobTable(){
        CommonResult comRes = new CommonResult();
        ExamUser examUser = UserUtils.getExamUser();
        comRes.setData(appraislJobTableService.findAppraisalJobTable(examUser));
        return comRes;
    }

}
