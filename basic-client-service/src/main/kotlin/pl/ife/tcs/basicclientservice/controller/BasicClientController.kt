package pl.ife.tcs.basicclientservice.controller

import com.netflix.discovery.EurekaClient
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import pl.ife.tcs.basicclientservice.service.RepositoryService
import java.util.logging.Logger


@RestController
class BasicClientController @Autowired constructor(
        private val repositoryService: RepositoryService
){

    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(BasicClientController::class.simpleName)

    @ApiOperation(value = "Greet the user")
    @GetMapping("greetings")
    fun getGreetings(): ResponseEntity<String> = ResponseEntity.ok("Hello, my name is $applicationName!")

    @ApiOperation(value = "Greet another service")
    @GetMapping("/greetings/{serviceId}")
    fun greet(@PathVariable serviceId: String): ResponseEntity<String> {
        val response = repositoryService.getGreeting()
        return ResponseEntity.ok("- Hello, who are you? - $response")
    }
}