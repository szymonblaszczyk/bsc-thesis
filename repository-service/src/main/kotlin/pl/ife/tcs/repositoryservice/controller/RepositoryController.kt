package pl.ife.tcs.repositoryservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.ife.tcs.commonlib.model.SyncPolicy
import pl.ife.tcs.commonlib.model.networking.DifferentialResponse
import pl.ife.tcs.commonlib.model.networking.FlexibleResponseModel
import pl.ife.tcs.commonlib.model.networking.SnapshotResponse
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.repositoryservice.repository.EntityRepository
import pl.ife.tcs.repositoryservice.service.EntityFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.logging.Logger

@RestController
class RepositoryController @Autowired constructor(
        private val entityRepository: EntityRepository,
        private val entityFactory: EntityFactory
) {

    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(RepositoryController::class.simpleName)

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

    @ApiOperation(value = "Generate new data rows")
    @PostMapping("generate")
    fun addNewRows(@RequestParam number: Int): ResponseEntity<Void> {
        val list: List<EntityModel> = entityFactory.generateEntities(number)
        val save = entityRepository.saveAll(list)
        logger.info("Added ${save.size} new data rows to the repository")
        return ResponseEntity.ok().build()
    }

    @ApiOperation(value = "Initialised repository accordingly to project configuration")
    @PostMapping("init")
    fun initRepo(): ResponseEntity<Void> {
        entityRepository.deleteAll()
        val list: List<EntityModel> = entityFactory.generateEntitiesConfigured()
        val save = entityRepository.saveAll(list)
        logger.info("Added ${save.size} new data rows to the repository")
        return ResponseEntity.ok().build()
    }

    @ApiOperation(value = "Get data rows according to sync policy")
    @GetMapping("policy")
    fun getByPolicy(
            @RequestParam(required = true) policy: SyncPolicy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) date: LocalDateTime?
    ): ResponseEntity<FlexibleResponseModel> {
        val response = when (policy) {
            SyncPolicy.SNAPSHOT -> {
                val rows = entityRepository.findAll()
                logger.info("Fetched whole repository with ${rows.size} data rows")
                SnapshotResponse(rows)
            }
            SyncPolicy.DIFF -> {
                val rows = if (date != null) entityRepository.findNewerThan(date) else entityRepository.findAll()
                logger.info("Fetched ${rows.size} data rows newer than $date from the repository")
                DifferentialResponse(rows)
            }
            SyncPolicy.EVENT -> TODO()
        }
        logger.info("Responding with ${response.javaClass.name} to request with sync policy $policy")
        return ResponseEntity.ok(response)
    }
}