package org.upnext.productservice.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Map URL /images/** to the local folder "uploads/images/"
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:uploads/images/");
    }
}