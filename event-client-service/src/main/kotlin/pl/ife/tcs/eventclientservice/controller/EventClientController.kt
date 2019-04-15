package pl.ife.tcs.eventclientservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.ife.tcs.commonlib.model.networking.*
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.eventclientservice.repository.EntityRepository
import pl.ife.tcs.eventclientservice.service.RepositoryService
import java.util.logging.Logger


@RestController
class EventClientController @Autowired constructor(
        private val repositoryService: RepositoryService,
        private val entityRepository: EntityRepository
){
    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(EventClientController::class.simpleName)

    @ApiOperation(value = "Greet the user")
    @GetMapping("greetings")
    fun getGreetings(): ResponseEntity<String> = ResponseEntity.ok("Hello, my name is $applicationName!")

    @ApiOperation(value = "Get all data rows from the repository")
    @GetMapping("all")
    fun getAll(): ResponseEntity<List<EntityModel>> {
        return ResponseEntity.ok(entityRepository.findAll())
    }

    @ApiOperation(value = "Get size of the repository")
    @GetMapping("size")
    fun getSize(): ResponseEntity<Long> {
        return ResponseEntity.ok(entityRepository.count())
    }

    @ApiOperation(value = "Get new data rows from the repository")
    @GetMapping("repository/sync")
    fun syncWithRepository(): ResponseEntity<FlexibleResponseModel> {
        val latestUpdateDate = entityRepository.findNewestUpdateDate()
        val response = repositoryService.getSnapshot(latestUpdateDate)
                ?: return ResponseEntity.notFound().build()
        return when (response) {
            is EventDrivenResponse -> {
                logger.info("Obtained ${response.collection.size} data rows from repository, saving")
//                val updatedRows = entityRepository.findAllById(response.collection.map { it.entityId })
                ResponseEntity.ok(response)
            }
            is ErrorResponse -> {
                logger.warning("Obtained error: ${response.error}")
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
            }
            else -> {
                logger.warning { "Obtained response of incompatible type: ${response.javaClass.simpleName}" }
                ResponseEntity.unprocessableEntity().build()
            }
        }
    }
}