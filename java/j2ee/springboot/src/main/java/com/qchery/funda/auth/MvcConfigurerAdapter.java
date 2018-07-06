package com.qchery.funda.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring Mvc Configurer Adapter扩展
 *
 * @author sdcuike
 *     <p>Created on 2017.02.13
 *     <p>处理自定义注解 {@link SpringHandlerInterceptor}，自动配置HandlerInterceptor
 */
public abstract class MvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    @Autowired private List<HandlerInterceptor> handlerInterceptors;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (handlerInterceptors == null || handlerInterceptors.isEmpty()) {
            return;
        }

        Collections.sort(
                handlerInterceptors,
                new Comparator<HandlerInterceptor>() {
                    @Override
                    public int compare(HandlerInterceptor a, HandlerInterceptor b) {
                        return getOrder(a) - getOrder(b);
                    }
                });

        for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
            SpringHandlerInterceptor annotation = AnnotationUtils.findAnnotation(
                            handlerInterceptor.getClass(), SpringHandlerInterceptor.class);
            if (annotation == null) {
                continue;
            }
            InterceptorRegistration registration = registry.addInterceptor(handlerInterceptor);

            String[] includePatterns = getIncludePatterns(annotation);
            if (includePatterns.length != 0) {
                registration.addPathPatterns(includePatterns);
            }

            String[] excludePatterns = getExcludePatterns(annotation);
            if (excludePatterns.length != 0) {
                registration.excludePathPatterns(excludePatterns);
            }
        }
    }

    private int getOrder(Object o) {
        SpringHandlerInterceptor annotation =
                AnnotationUtils.findAnnotation(o.getClass(), SpringHandlerInterceptor.class);
        if (annotation == null) {
            return 0;
        }
        return annotation.order();
    }

    private String[] getIncludePatterns(SpringHandlerInterceptor annotation) {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(annotation.value()));
        list.addAll(Arrays.asList(annotation.includePatterns()));
        return list.toArray(new String[] {});
    }

    private String[] getExcludePatterns(SpringHandlerInterceptor annotation) {
     /*   List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(annotation.excludePatterns()));
        return list.toArray(new String[list.size()]);*/
        return annotation.excludePatterns();
    }
}
