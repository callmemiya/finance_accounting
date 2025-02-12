package ru.nirs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * Конфигурация с развилкой:
 * - если включен общий фильтр по работе с JWT-токеном - конфигурация для стенда
 * - если не включен фильтр - конфигурация для локального запуска
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;

    public SecurityConfig(@Autowired(required = false) JwtTokenFilter jwtTokenFilter) {
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        if (jwtTokenFilter != null) {
            // Рабочая конфигурация безоспасности
            // Определяется через включение в конфиге jwt фильтра
            http
                    .httpBasic().disable()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .antMatchers("/ui/**").authenticated()
                    .anyRequest().permitAll()
                    .and()
                    .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint((request, response, e) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"));
        } else {
            // Локальная конфигурация безопасности
            // Для работы консоли h2 добавлены 2 строки:
            http
                    .httpBasic().disable()
                    .csrf().disable()
                    // h2
                    .headers().frameOptions().sameOrigin().and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    // h2
                    .antMatchers("/h2-console/**").permitAll()
                    .anyRequest().permitAll();
        }
    }

}
