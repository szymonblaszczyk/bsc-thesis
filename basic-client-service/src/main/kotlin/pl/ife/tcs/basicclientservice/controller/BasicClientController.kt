package pl.ife.tcs.basicclientservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.ife.tcs.basicclientservice.repository.EntityRepository
import pl.ife.tcs.basicclientservice.service.RepositoryService
import pl.ife.tcs.commonlib.model.networking.DifferentialResponse
import pl.ife.tcs.commonlib.model.networking.ErrorResponse
import pl.ife.tcs.commonlib.model.networking.FlexibleResponseModel
import pl.ife.tcs.commonlib.model.networking.SnapshotResponse
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import java.time.LocalDateTime
import java.util.logging.Logger


@RestController
@RequestMapping
class BasicClientController @Autowired constructor(
        private val repositoryService: RepositoryService,
        private val entityRepository: EntityRepository
){
    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(BasicClientController::class.simpleName)

    @ApiOperation(value = "Greet the user")
    @GetMapping("greetings")
    fun getGreetings(): ResponseEntity<String> = ResponseEntity.ok("Hello, my name is $applicationName!")

    @ApiOperation(value = "Get all data rows from the repository")
    @GetMapping("entities/all")
    fun getAll(): ResponseEntity<List<EntityModel>> {
        return ResponseEntity.ok(entityRepository.findAll())
    }

    @ApiOperation(value = "Get size of the repository")
    @GetMapping("entities/size")
    fun getSize(): ResponseEntity<Long> {
        return ResponseEntity.ok(entityRepository.count())
    }

    @ApiOperation(value = "Get new data rows from the repository")
    @GetMapping("entities/sync")
    fun syncWithRepository(@RequestHeader missedCycles: Int): ResponseEntity<FlexibleResponseModel> {
        val isInit = entityRepository.findAll().isEmpty()
        val latestUpdateDate = entityRepository.findNewestUpdateDate()
        val response = repositoryService.getSnapshot(isInit, missedCycles, latestUpdateDate)
                ?: return ResponseEntity.notFound().build()
        return when (response) {
            is SnapshotResponse -> {
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

    @ApiOperation(value = "Flush client's repository")
    @DeleteMapping("entities/flush")
    fun flushRepository(): ResponseEntity<Void> {
        entityRepository.deleteAll()
        return ResponseEntity.ok().build()
    }
}