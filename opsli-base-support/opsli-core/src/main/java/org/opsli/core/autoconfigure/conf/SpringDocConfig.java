package org.opsli.core.autoconfigure.conf;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    private static final String SECURITY_SCHEME_NAME = "OPSLI-Authorization";
    @Bean
    public OpenAPI OpenApi() {
        OpenAPI openApi = new OpenAPI();
        openApi.info(new Info()
                .title("OPSLI 快速开发平台-API")
                .summary("OPSLI 快速开发平台基于springboot、vue、element-ui ，项目采用前后端分离架构，热插拔式业务模块与插件扩展性高 ,代码简洁，功能丰富，开箱即用")
                .description("用于可在线调试OPSLI后端接口")
                .contact(new Contact()
                        .name("OPSLI 快速开发平台")
                        .url("http://www.opsli.com")
                        .email("meet.pace@foxmail.com")
                )
                .version("3.0.1")
                .license(new License().name("Apache 2.0").url("http://www.opsli.com")));
        openApi.externalDocs(new ExternalDocumentation()
                .description("OPSLi 快速开发平台官方文档")
                .url("https://wiki.opsli.bedebug.com/docs/opsliopsli-1e29udgrb4hog/opsli-1c83h9o28e1cm"));
        openApi.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
        openApi.components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME
                        , new SecurityScheme()
                                .name("T-Authorization")
                                .type(SecurityScheme.Type.APIKEY)
                                //.scheme("Bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)));
        return openApi;
    }

}
