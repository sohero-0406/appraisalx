package com.jeesite.modules;

import com.jeesite.modules.swagger.config.SwaggerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
 
@Configuration
@ConditionalOnProperty(
        name = {"web.swagger.enabled"},
        havingValue = "true",
        matchIfMissing = false
)
public class Swagger2 {
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                //为当前包路径
//                .apis(RequestHandlerSelectors.basePackage("com.jeesite.modules.test.web"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                //页面标题
//                .title("Spring Boot 测试使用 Swagger2 构建RESTful API")
//                //创建人
//                .contact(new Contact("MarryFeng", "http://www.baidu.com", ""))
//                //版本号
//                .version("1.0")
//                //描述
//                .description("API 描述")
//                .build();
//    }

    @Bean
    public Docket testApi() {
        String moduleCode = "test";
        String moduleName = "测试模块";
        String basePackage = "com.jeesite.modules.test.web";
        return SwaggerConfig.docket(moduleCode, moduleName, basePackage).select().apis(RequestHandlerSelectors.basePackage(basePackage)).build();
    }

}