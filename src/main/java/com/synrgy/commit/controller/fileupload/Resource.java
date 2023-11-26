package com.synrgy.commit.controller.fileupload;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Resource implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods()
                .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
    }


    @Override // cara call engpoint : localhost:9090/api/showFile/namafile.png:
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
        registry.addResourceHandler("/showFile/**").addResourceLocations("file:cdn/profile/");
        registry.addResourceHandler("/showFile/report/**").addResourceLocations("file:cdn/report/");
        registry.addResourceHandler("/showFile/tmp/**").addResourceLocations("file:cdn/template/");
    }
}

