package be.zwoop.config.security;

import be.zwoop.security.AccessTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private final AccessTokenFilter accessTokenFilter;

    private static final String[] AUTH_WHITELIST = {
        "/v2/api-docs", "/v3/api-docs", "/swagger-resources/**", "/swagger-ui/**", "/webjars/**", "/actuator/**",
        "/api/v1/public/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http
            .cors().and().csrf().disable()

            // Set session management to stateless
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            // Set unauthorized requests exception handler
            .exceptionHandling()
            .authenticationEntryPoint(
                    (request, response, ex) -> {
                        response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        );
                    }
            )
            .and()

            // Set permissions on endpoints
            .authorizeRequests()
            // Our public endpoints
            .antMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .and()

            // Add JWT token filter
            .addFilterBefore(accessTokenFilter, BasicAuthenticationFilter.class);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


}
