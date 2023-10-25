package org.mrying.chat.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author 邓和颖
 * @since   2023/9/21 20:05
 */
@Configuration
public class SwaggerConfiguration {

    // 1、创建 Docket 对象
    @Bean
    public Docket docket() {
        // 文档类型 2.0版本
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        // 2、创建 Api 信息，接口文档的总体描述
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("AI 对话")
                .version("1.0")
                .description("ChatGPT 对话实现")
                .contact(new Contact("Mrying","https://github.com/Mrying0823","1553947217@qq.com"))
                .license("Apache 2.0")
                .build();

        // 3、设置使用 ApiInfo
        docket.apiInfo(apiInfo);

        // 指定注解
        // 4、可以指定参与文档生成的包
        docket = docket.select().apis(RequestHandlerSelectors.basePackage("org.mrying.chat.controller")).build();

        return docket;
    }
}

