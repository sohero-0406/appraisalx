/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.jeesite.common.constant.ServiceConstant;
import com.jeesite.modules.common.entity.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 车辆配置全表Service
 *
 * @author chenlitao
 * @version 2019-07-04
 */
@Service
@Transactional(readOnly = true)
public class VehicleInfoService {

    @Autowired
    private HttpClientService httpClientService;

    public CommonResult findList(String chexiId) {
        Map<String, String> map = new HashMap<>();
        map.put("chexiId", chexiId);
        return httpClientService.post(ServiceConstant.VEHICLEINFO_FIND_LIST, map);
    }
}