package org.upnext.productservice.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI apiInfo(){
        return new OpenAPI().info(
            new Info()
                    .title("Product Service")
                    .version("1.0")
                    .description("Product Service API For UpNext Project")
        );
    }
}
