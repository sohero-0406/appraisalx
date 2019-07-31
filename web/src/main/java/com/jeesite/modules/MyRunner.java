package com.jeesite.modules;

import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.entity.SystemSetting;
import com.jeesite.modules.aa.service.SystemSettingService;
import com.jeesite.modules.common.entity.ExamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 项目启动执行类
 */
@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    private SystemSettingService systemSettingService;

    @Override
    public void run(String... strings) {
        SystemSetting systemSetting = systemSettingService.getByEntity(new SystemSetting());
        if (null != systemSetting) {
            String ip = systemSetting.getIp();
            String port = systemSetting.getPort();
            String contextPath = systemSetting.getContextPath();
            CacheUtils.put("platformUrl", "http://" + ip + ":" + port + "/" + contextPath);
        }
    }
}