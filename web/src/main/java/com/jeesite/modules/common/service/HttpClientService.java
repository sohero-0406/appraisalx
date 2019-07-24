/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jeesite.modules.common.service;

import com.alibaba.fastjson.JSONObject;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.constant.CodeConstant;
import com.jeesite.common.entity.Page;
import com.jeesite.common.service.CrudService;
import com.jeesite.common.web.http.HttpClientUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.vo.LoginVO;
import com.jeesite.modules.common.dao.CommonUserDao;
import com.jeesite.modules.common.entity.CommonResult;
import com.jeesite.modules.common.entity.CommonUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 调用第三方服务 service
 *
 * @author lvchangwei
 * @version 2019-07-24
 */
@Service
@Transactional(readOnly = true)
public class HttpClientService {

    /**
     * 向第三方服务发送post请求
     *
     * @param serviceName 服务名 例如 /aa/checkBodySkeleton/save
     * @param map         请求入参
     * @return
     */
    public String post(String serviceName, Map<String, String> map) {
        String platformUrl = CacheUtils.get("platformUrl");
        String url = platformUrl + serviceName;
        String result = HttpClientUtils.post(url,map);
        JSONObject object = JSONObject.parseObject(result);
        return object.getString("data");
    }
}