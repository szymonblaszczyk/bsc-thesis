package pl.ife.tcs.diffclientservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate



@Configuration
class RestConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate = builder.build()

}