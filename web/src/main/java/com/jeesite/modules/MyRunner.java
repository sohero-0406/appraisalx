package com.jeesite.modules;

import com.jeesite.common.cache.CacheUtils;
import org.ini4j.Wini;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            url = url.substring(0, url.indexOf("/webapps"));
            url = url.substring(0, url.lastIndexOf("/"));
        } else {
            url = "D:";
        }
        //图片上传路径
        String picUrl = url + "/soHero/appraisalPic/";
        CacheUtils.put("picUrl", picUrl);

        url += "/server/config.ini";
        Wini ini = null;
        try {
            ini = new Wini(new File(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String ip = ini.get("Server", "ServerName");
        String port = ini.get("Server", "ComID");
        String contextPath = ini.get("Server", "ContextPath");
        CacheUtils.put("platformUrl", "http://" + ip + ":" + port + "/" + contextPath);
    }
}