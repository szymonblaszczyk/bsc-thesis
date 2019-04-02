package pl.ife.tcs.basicclientservice.service

import com.netflix.discovery.EurekaClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class RepositoryService @Autowired constructor(
        private val restTemplate: RestTemplate,
        private val eurekaClient: EurekaClient
) {
    private val repositoryServiceId: String = "repository-service"
    private lateinit var baseUrl: String

    init {
        val application = eurekaClient.getApplication(repositoryServiceId)
        val instanceInfo = application.instances[0]
        baseUrl = "http://" + instanceInfo.ipAddr + ":" + instanceInfo.port
    }

    fun getGreeting(): String? = restTemplate.getForObject("$baseUrl/greetings", String::class.java)
}