package pl.ife.tcs.basicclientservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import pl.ife.tcs.commonlib.model.networking.LoggingRequestInterceptor


@Configuration
class RestConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate(BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()))
        val interceptors
                = (if (restTemplate.interceptors.isNotEmpty()) restTemplate.interceptors else listOf()).toMutableList()
        interceptors.add(LoggingRequestInterceptor())
        restTemplate.interceptors = interceptors
        return restTemplate
    }

}