package com.jeesite.modules.aa.web;

import com.jeesite.modules.aa.service.HomePageService;
import com.jeesite.modules.aa.vo.HomePageVO;
import com.jeesite.modules.common.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 加载首页界面
 */

@Controller
@RequestMapping(value = "${adminPath}/aa/homePage")
public class HomePageController {

    @Autowired
    private HomePageService homePageService;

    /**
     * 加载首页界面(学生)
     */
    @PostMapping(value = "loadHomePageStu")
    @ResponseBody
    public CommonResult loadHomePageStu(HomePageVO homePageVO){
        CommonResult comRes = new CommonResult();
        comRes.setData(homePageService.loadHomePageStu(homePageVO));
        return comRes;
    }

    /**
     * 加载首页界面(教师)
     */
    @PostMapping(value = "loadHomePageTea")
    @ResponseBody
    public CommonResult loadHomePageTea(HomePageVO homePageVO){
        CommonResult comRes = new CommonResult();
        comRes.setData(homePageService.loadHomePageTea(homePageVO));
        return comRes;
    }

    /**
     * 新建试卷
     */
    @PostMapping(value = "newPaper")
    @ResponseBody
    public CommonResult newPaper(){
        CommonResult comRes = new CommonResult();
        homePageService.newPaper();
        return comRes;
    }
}
