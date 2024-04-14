package org.example.marketserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "OpenApi specification - Alibou",
                version = "1.0",
                description = "API documentation for Spring Security integration at Alibou",
                termsOfService = "https://aliboucoding.com/course/terms",
                contact = @Contact(
                        name = "Support Team",
                        url = "https://aliboucoding.com/support",
                        email = "support@aliboucoding.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local development server"
                ),
                @Server(
                        url = "https://api.aliboucoding.com",
                        description = "Production server"
                )
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT Authorization header using the Bearer scheme. Example: \"Authorization: Bearer {token}\""
)
public class OpenApiConfig {
    // Class remains empty as configuration is done through annotations
}
