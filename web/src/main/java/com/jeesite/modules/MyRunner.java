package com.jeesite.modules;

import alvinJNI.HardwareTime;
import com.jeesite.common.cache.CacheUtils;
import com.jeesite.common.web.http.ServletUtils;
import com.jeesite.modules.aa.entity.SystemSetting;
import com.jeesite.modules.aa.service.SystemSettingService;
import com.jeesite.modules.common.entity.ExamUser;
import org.ini4j.Wini;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 项目启动执行类
 */
@Component
public class MyRunner implements CommandLineRunner {

    @Override
    public void run(String... strings) {
        String url = MyRunner.class.getResource("").toString().replace("file:/", "");
        if (url.contains("/webapps")) {
            url = url.substring(0, url.indexOf("/webapps")).split("/")[0];
        } else {
            url = "D:";
        }
        url+="/大平台地址.ini";

        Wini ini = null;
        try {
            ini = new Wini(new File(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ip = ini.get("Server", "Ip");
        String port = ini.get("Server", "Port");
        String contextPath = ini.get("Server", "ContextPath");
        CacheUtils.put("platformUrl", "http://" + ip + ":" + port + "/" + contextPath);
    }
}