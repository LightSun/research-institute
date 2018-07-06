package com.qchery.funda.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// web 配置。
@Configuration
@EnableWebMvc
@PropertySource({"classpath:test.properties"})
public class MyWebAppConfiguer extends WebMvcConfigurerAdapter {

    @Value("${author}")
    String author;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HeaderTokenInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
        System.out.println("author = " + author);
    }

    // 允许跨域的接口
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/sys/*")
                .allowedOrigins("*")
                .allowCredentials(false)
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .allowedHeaders(
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Headers",
                        "Access-Control-Allow-Methods",
                        "Access-Control-Max-Age")
                .exposedHeaders("Access-Control-Allow-Origin")
                .maxAge(3600); //1 hour
    }
}
