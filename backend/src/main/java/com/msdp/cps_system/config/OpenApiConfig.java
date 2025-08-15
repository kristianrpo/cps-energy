package com.msdp.cps_system.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "CPS System API",
        version = "1.0",
        description = "API for managing CPS System",
        contact = @Contact(
            name = "Sebastian Restrepo",
            email = "srestrep74@eafit.edu.co"),
        license = @License(
            name = "Apache 2.0", 
            url = "http://www.apache.org/licenses/LICENSE-2.0"),
        summary = "Comprehensive API for CPS System"))
public class OpenApiConfig {
    
}