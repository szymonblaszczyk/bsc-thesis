package pl.ife.tcs.diffclientservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pl.ife.tcs.commonlib.model.networking.DifferentialResponse
import pl.ife.tcs.commonlib.model.networking.ErrorResponse
import pl.ife.tcs.commonlib.model.networking.FlexibleResponseModel
import pl.ife.tcs.commonlib.model.networking.SnapshotResponse
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.diffclientservice.repository.EntityRepository
import pl.ife.tcs.diffclientservice.service.RepositoryService
import java.util.logging.Logger


@RestController
class DiffClientController @Autowired constructor(
        private val repositoryService: RepositoryService,
        private val entityRepository: EntityRepository
){
    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(DiffClientController::class.simpleName)

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
            is DifferentialResponse -> {
                logger.info("Obtained ${response.collection.size} data rows from repository, saving")
                entityRepository.saveAll(response.collection)
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