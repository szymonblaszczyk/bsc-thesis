package pl.ife.tcs.basicclientservice.service

import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import pl.ife.tcs.commonlib.model.FlexibleResponseModel
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

    fun getFlexResponse(number: Int): FlexibleResponseModel? {
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/flex").queryParam("number", number).toUriString()
        logger.info("Calling $repositoryServiceId at URL: $url")
        return restTemplate.getForObject(url, FlexibleResponseModel::class.java)
    }
}