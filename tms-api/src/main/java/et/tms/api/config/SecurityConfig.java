
package et.tms.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain api(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                new HttpStatusEntryPoint(
                                        HttpStatus.UNAUTHORIZED
                                )
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Public Scalar API documentation
                        .requestMatchers(
                                "/docs",
                                "/docs/**",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/v3/api-docs.yaml"
                        ).permitAll()

                        // Protected assessment endpoints
                        .requestMatchers(
                                "/api/assessments/**"
                        ).authenticated()

                        // Other API endpoints
                        .requestMatchers(
                                "/api/**"
                        ).permitAll()

                        // Deny all unspecified requests
                        .anyRequest().denyAll()
                )

                .addFilterBefore(
                        new TrainingAuthFilter(),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
