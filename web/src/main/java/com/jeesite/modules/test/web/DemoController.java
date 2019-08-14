package com.jeesite.modules.test.web;

import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.web.BaseController;
import com.jeesite.modules.sys.entity.DictData;
import com.jeesite.modules.sys.utils.DictUtils;
import com.jeesite.modules.test.entity.TestData;
import com.jeesite.modules.test.service.TestDataService;
import io.swagger.annotations.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */

/**
 * 演示实例Controller
 *
 * @author ThinkGem
 * @version 2018-03-24
 */
@Controller
@RequestMapping(value = "${adminPath}/demo")
@Api(value = "aaaa", description = "aaaa")
public class DemoController extends BaseController {

    @Autowired
    private TestDataService testDataService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取数据
     */
    @ModelAttribute
    public TestData get(String id, boolean isNewRecord) {
        return testDataService.get(id, isNewRecord);
    }

    /**
     * DataGrid
     */
    @RequiresPermissions("test:testData:view")
    @RequestMapping(value = "dataGrid/{viewName}")
    public String dataGrid(@PathVariable String viewName, TestData testData, Model model) {
        return "modules/demo/demoDataGrid" + StringUtils.cap(viewName);
    }

    /**
     * Form
     */
//	@RequiresPermissions("test:testData:view")
    @RequestMapping(value = "form/{viewName}", method = RequestMethod.POST)
    @ApiOperation(value = "bbbb", notes = "bbbb")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户的唯一标识", required = true),
            @ApiImplicitParam(name = "name", value = "用户名", required = false)
    })
    public String form(@PathVariable String viewName, TestData testData, Model model) {
        return "modules/demo/demoForm" + StringUtils.cap(viewName);
    }

    @ApiOperation(value = "税务报表二次计算", notes = "税务报表二次计算")
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(@ApiParam("二次计算参数") TestData testData) {
//		redisTemplate.opsForHash().put("a","a","aaa");
//		redisTemplate.opsForHash().put("a","b","bbb");
//		String a = (String) redisTemplate.opsForHash().get("a","b");
        testDataService.save(new TestData());
        List<DictData> list = DictUtils.getDictList("sys_job_event");
        return "hello";
    }
}