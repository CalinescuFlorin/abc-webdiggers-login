package web_diggers.abc_backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "ABC Support",
                        email = "abc.backend.t1@gmail.com",
                        url = ""
                ),
                description = "OpenApi documentation for ABC Backend",
                title = "OpenApi specification - ABC",
                version = "0.1",
                license = @License(
                        name = "TDB",
                        url = "TBD"
                ),
                termsOfService = ""
        ),
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
