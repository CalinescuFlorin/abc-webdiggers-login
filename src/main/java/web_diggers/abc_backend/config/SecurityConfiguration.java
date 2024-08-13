package web_diggers.abc_backend.config;

import web_diggers.abc_backend.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    // Endpoints accessible by anyone
    private static final String[] WHITE_LIST_URL = {
            "/api/v1/auth/**",
            "/api/v1/visitor/**",
            "/api/v1/password_change/**"
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/api/v1/visitor/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authorizeHttpRequests) ->
                                authorizeHttpRequests
                                        .requestMatchers(WHITE_LIST_URL).permitAll()

                                        .requestMatchers(GET ,"api/v1/user/**").hasRole("ADMIN")
                                        .requestMatchers(POST ,"api/v1/user/**").hasRole("ADMIN")
                                        .requestMatchers(PUT ,"api/v1/user/**").hasRole("ADMIN")
                                        .requestMatchers(DELETE ,"api/v1/user/**").hasRole("ADMIN")

                                        .requestMatchers(GET ,"api/v1/arheo/**").hasRole("ARCHAEOLOGIST")
                                        .requestMatchers(POST ,"api/v1/arheo/**").hasRole("ARCHAEOLOGIST")
                                        .requestMatchers(PUT ,"api/v1/arheo/**").hasRole("ARCHAEOLOGIST")
                                        .requestMatchers(DELETE ,"api/v1/arheo/**").hasRole("ARCHAEOLOGIST")

                                        .requestMatchers(GET ,"api/v1/bio/**").hasRole("BIOLOGIST")
                                        .requestMatchers(POST ,"api/v1/bio/**").hasRole("BIOLOGIST")
                                        .requestMatchers(PUT ,"api/v1/bio/**").hasRole("BIOLOGIST")
                                        .requestMatchers(DELETE ,"api/v1/bio/**").hasRole("BIOLOGIST")

                                        .anyRequest().authenticated()

                )
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
