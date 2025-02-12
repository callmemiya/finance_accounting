package ru.nirs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.configuration.ObjectPostProcessorConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import ru.nirs.security.JwtFilterAutoConfiguration;
import ru.nirs.security.SecurityConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест проверяет работу SecurityConfig с включенным или выключенным jwtTokenFilter
 */
@SpringBootTest
class ApplicationContextTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(ObjectPostProcessorConfiguration.class))
            .withConfiguration(AutoConfigurations.of(AuthenticationConfiguration.class))
            .withConfiguration(AutoConfigurations.of(WebSecurityConfiguration.class))
            .withConfiguration(AutoConfigurations.of(SecurityConfig.class));

    @Test
    void checkLocalSecurityConfig() {

        this.contextRunner
                .withPropertyValues("jwtfilter.auth.starter.tokenFilter.enabled=false")
                .withUserConfiguration(JwtFilterAutoConfiguration.class)
                .run(context -> {
                    assertThat(context).doesNotHaveBean(JwtFilterAutoConfiguration.class);
                });

        this.contextRunner
                .withPropertyValues("jwtfilter.auth.starter.tokenFilter.enabled=true")
                .withUserConfiguration(JwtFilterAutoConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(JwtFilterAutoConfiguration.class);
                });

    }

}
