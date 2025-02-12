package ru.nirs.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Автоконфигурация для автоматического добавления фильтра JwtTokenFilter во все модули, которые
 * будут использовать данную библиотеку
 */
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnExpression("${jwtfilter.auth.starter.tokenFilter.enabled:true}")
public class JwtFilterAutoConfiguration {

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public BasicAuthFilter basicAuthFilter() {
        return new BasicAuthFilter();
    }

}
