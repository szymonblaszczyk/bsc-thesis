package pl.ife.tcs.eventclientservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.BufferingClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import pl.ife.tcs.commonlib.model.networking.LoggingRequestInterceptor


@Configuration
@RefreshScope
class RestConfig {

    @Value("\${thesis.table.length:1}")
    val tableLength: Int = 1
    @Value("\${thesis.entity.width:1}")
    val entityWidth: Int = 1
    @Value("\${thesis.entity.update.operation.batch:1}")
    val entityUpdateBatch: Int = 1
    @Value("\${thesis.entity.update.collection.batch:1}")
    val collectionUpdateBatch: Int = 1

    @Bean
    @RefreshScope
    fun restTemplate(): RestTemplate {
        val restTemplate = RestTemplate(BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory()))
        val interceptors = listOf(LoggingRequestInterceptor(tableLength, entityWidth, entityUpdateBatch, collectionUpdateBatch))
        restTemplate.interceptors = interceptors
        return restTemplate
    }

}