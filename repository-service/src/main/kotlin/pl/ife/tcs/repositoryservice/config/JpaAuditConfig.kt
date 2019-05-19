package pl.ife.tcs.repositoryservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*


@Configuration
@EnableJpaAuditing
class JpaAuditConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware { Optional.of("dummy") }
    }
}