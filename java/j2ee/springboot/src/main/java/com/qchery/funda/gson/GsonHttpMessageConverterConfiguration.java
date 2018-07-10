package com.qchery.funda.gson;

import com.google.gson.Gson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Configuration
@ConditionalOnClass(Gson.class)
@ConditionalOnMissingClass({"com.fasterxml.jackson.core.JsonGenerator"})
@ConditionalOnBean(Gson.class)
public class GsonHttpMessageConverterConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GsonHttpMessageConverter gsonHttpMessageConverter(Gson gson) {
        GsonHttpMessageConverter converter = new GsonHttpMessageConverter();
        converter.setGson(gson);
        return converter;
    }

    //使用HttpMessageConverters来定制转换器
    @Configuration
    public static class CustomConfiguration {
        @Bean
        public HttpMessageConverters customConverters() {
            Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
            messageConverters.add(gsonHttpMessageConverter);
            return new HttpMessageConverters(true, messageConverters);
        }
    }
    //使用java配置
    @Configuration
    @EnableWebMvc
    public static class Application extends WebMvcConfigurerAdapter {
        @Override
        public void configureMessageConverters(List<HttpMessageConverter < ? >> converters) {
            GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
            converters.add(gsonHttpMessageConverter);
        }
    }
}