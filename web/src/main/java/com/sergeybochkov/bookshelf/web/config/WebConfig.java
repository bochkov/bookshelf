package com.sergeybochkov.bookshelf.web.config;

import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.spring.boot.config.JtwigViewResolverConfigurer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter implements JtwigViewResolverConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("favicon.ico").addResourceLocations("classpath:/static/img/favicon.ico");
    }

    @Override
    public void configure(JtwigViewResolver viewResolver) {
        viewResolver.setPrefix("classpath:/templates/");
        viewResolver.setSuffix(".html");
    }
}
