/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.web;

import com.jeesite.common.web.BaseController;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.service.VehicleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 车辆配置全表Controller
 *
 * @author chenlitao
 * @version 2019-07-04
 */
@Controller
@RequestMapping(value = "${adminPath}/common/vehicleInfo")
public class VehicleInfoController extends BaseController {

    @Autowired
    private VehicleInfoService vehicleInfoService;

    @RequestMapping(value = "findList")
    @ResponseBody
    public CommonResult findList(String chexiId) {
        return vehicleInfoService.findList(chexiId);
    }
}