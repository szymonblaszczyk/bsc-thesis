package pl.ife.tcs.diffclientservice.service

import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import pl.ife.tcs.commonlib.model.networking.SyncPolicy
import pl.ife.tcs.commonlib.model.networking.FlexibleResponseModel
import java.time.LocalDateTime
import java.util.logging.Logger

@Service
class RepositoryService @Autowired constructor(
        private val restTemplate: RestTemplate,
        private val eurekaClient: EurekaClient
) {
    private val repositoryServiceId: String = "repository-service"
    private val applicationInstance by lazy { eurekaClient.getApplication(repositoryServiceId).instances[0] }
    private val baseUrl: String by lazy { "http://" + applicationInstance.ipAddr + ":" + applicationInstance.port }
    private val logger: Logger = Logger.getLogger(RepositoryService::class.simpleName)

    fun getGreeting(): String? {
        val url = "$baseUrl/greetings"
        logger.info("Calling $repositoryServiceId at URL: $url")
        return restTemplate.getForObject(url, String::class.java)
    }

    fun getSnapshot(init: Boolean, missedCycles: Int, date: LocalDateTime?): FlexibleResponseModel? {
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/entities/policy")
                .queryParam("date", date)
                .queryParam("policy", SyncPolicy.DIFF)
                .toUriString()
        logger.info("Calling $repositoryServiceId at URL: $url")
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                HttpEntity("body", HttpHeaders().apply {
                    this.set("init", init.toString())
                    this.set("missedCycles", missedCycles.toString())
                }),
                FlexibleResponseModel::class.java
        ).body
    }
}