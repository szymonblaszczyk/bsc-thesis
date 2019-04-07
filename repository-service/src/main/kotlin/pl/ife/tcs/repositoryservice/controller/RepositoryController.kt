package pl.ife.tcs.repositoryservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ife.tcs.commonlib.model.CollectionResponse
import pl.ife.tcs.commonlib.model.EntityModel
import pl.ife.tcs.commonlib.model.ErrorResponse
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

    @ApiOperation(value = "Test error response")
    @GetMapping("error")
    fun genError(@RequestParam message: String): ResponseEntity<FlexibleResponseModel> {
        return ResponseEntity.ok().body(ErrorResponse(message))
    }

    @ApiOperation(value = "Test flexible response")
    @GetMapping("flex")
    fun getFlexible(@RequestParam number: Int): ResponseEntity<FlexibleResponseModel> {
        val response = if (number < 6) {
            val list: List<EntityModel> = List(number) { EntityModel(it) }
            CollectionResponse(list)
        } else {
            ErrorResponse("Sorry, that number is too big")
        }
        return ResponseEntity.ok(response)
    }
}