package com.example.securityskilltesting.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest {

    @Test
    void allowsFrontendOriginsWithoutTrailingSlash() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();

        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/product/upload");
        request.addHeader("Origin", "https://project-i-frontend-kappa.vercel.app");

        CorsConfiguration config = source.getCorsConfiguration(request);
        assertThat(config).isNotNull();
        assertThat(config.checkOrigin("https://project-i-frontend-kappa.vercel.app")).isEqualTo("https://project-i-frontend-kappa.vercel.app");
        assertThat(config.checkOrigin("https://project-i-frontend-6m7l.vercel.app")).isEqualTo("https://project-i-frontend-6m7l.vercel.app");
        assertThat(config.getAllowCredentials()).isTrue();
    }
}
