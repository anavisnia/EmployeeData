package com.example.employeedata.config;

import java.util.Set;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
	public Docket productApi() {
		return new Docket(DocumentationType.OAS_30) //Open Api Spec 3.0
		.apiInfo(new ApiInfoBuilder()
            .title("Employee-Project API")
            .description("A CRUD API to manage employees and projects")
            .build())
		.protocols(Set.of("https", "http"))
		.select()
		.apis(RequestHandlerSelectors.basePackage("com.example.employeedata.controller"))              
		.paths(regex("/api/.*"))  
		.build();
	}
}
