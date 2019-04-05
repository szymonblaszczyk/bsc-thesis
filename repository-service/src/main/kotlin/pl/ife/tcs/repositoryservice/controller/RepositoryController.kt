package pl.ife.tcs.repositoryservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.ife.tcs.commonlib.model.FlexibleResponseModel
import java.util.logging.Logger

@RestController
class RepositoryController {

    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(RepositoryController::class.simpleName)

    @ApiOperation(value = "Greet the user")
    @GetMapping("greetings")
    fun getGreetings(): ResponseEntity<String> = ResponseEntity.ok("Hello, my name is $applicationName!")
}