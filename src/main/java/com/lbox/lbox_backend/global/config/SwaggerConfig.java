package com.lbox.lbox_backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI swagger() {
        // Swagger 화면에 표시할 API 소개
        Info info = new Info()
                .title("Lbox 역기획 API")
                .description("Lbox 역기획 스웨거")
                .version("1.0.0");

        return new OpenAPI()
                // Swagger가 열린 서버를 기준으로 API 요청
                .addServersItem(new Server().url("/"))
                .info(info);
    }
}