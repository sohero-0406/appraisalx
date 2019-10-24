package com.jeesite.modules.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileLocalConfig implements WebMvcConfigurer {

    static final String ORIGINS[] = new String[]{"GET", "POST", "PUT", "DELETE"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String url = FileLocalConfig.class.getResource("").toString().replace("jar:file:/", "");
        if (url.contains("/webapps")) {
            url = url.substring(0, url.indexOf("/webapps"));
            url = url.substring(0, url.lastIndexOf("/"));
        } else {
            url = "D:";
        }
        //图片上传路径
        String picUrl = url + "/soHero/appraisalPic/";
        registry.addResourceHandler("/**").addResourceLocations("file:" + picUrl);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods(ORIGINS)
                .maxAge(3600);
    }
}
