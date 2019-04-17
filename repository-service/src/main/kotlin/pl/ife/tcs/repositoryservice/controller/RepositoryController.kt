package pl.ife.tcs.repositoryservice.controller

import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.ife.tcs.commonlib.model.networking.*
import pl.ife.tcs.commonlib.model.persistency.EntityEventModel
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.commonlib.model.persistency.EventType
import pl.ife.tcs.repositoryservice.repository.EntityEventRepository
import pl.ife.tcs.repositoryservice.repository.EntityRepository
import pl.ife.tcs.repositoryservice.service.EntityFactory
import pl.ife.tcs.repositoryservice.service.EntityManipulator
import java.time.LocalDateTime
import java.util.logging.Logger

@RestController
class RepositoryController @Autowired constructor(
        private val entityRepository: EntityRepository,
        private val entityFactory: EntityFactory,
        private val entityManipulator: EntityManipulator,
        private val entityEventRepository: EntityEventRepository
) {

    @Value("\${spring.application.name:}")
    val applicationName: String = ""

    private val logger: Logger = Logger.getLogger(RepositoryController::class.simpleName)

    @ApiOperation(value = "Greet the user")
    @GetMapping("greetings")
    fun getGreetings(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, my name is $applicationName!")
    }

    @ApiOperation(value = "Get all data rows from the repository")
    @GetMapping("entities/all")
    fun getAllEntities(): ResponseEntity<List<EntityModel>> {
        return ResponseEntity.ok(entityRepository.findAll())
    }

    @ApiOperation(value = "Get size of the entities repository")
    @GetMapping("entities/size")
    fun getAllEntitiesSize(): ResponseEntity<Long> {
        return ResponseEntity.ok(entityRepository.count())
    }

    @ApiOperation(value = "Get all event changes from the repository")
    @GetMapping("events/all")
    fun getAllEvents(): ResponseEntity<List<EntityEventModel>> {
        return ResponseEntity.ok(entityEventRepository.findAll())
    }

    @ApiOperation(value = "Get size of the event repository")
    @GetMapping("events/size")
    fun getAllEventsSize(): ResponseEntity<Long> {
        return ResponseEntity.ok(entityEventRepository.count())
    }

    @ApiOperation(value = "Initialise repository accordingly to project configuration")
    @PostMapping("entities/init")
    fun initRepo(): ResponseEntity<Void> {
        entityRepository.deleteAll()
        entityEventRepository.deleteAll()
        val entities = entityFactory.getEntities()
        val entitiesSave = entityRepository.saveAll(entities)
        val events = entitiesSave.map {
            EntityEventModel(
                    it.id!!,
                    EventType.CREATED,
                    mapOf( *it.attributeMap.map { Pair(it.key, it.value) }.toTypedArray())
            )
        }
        val eventsSave = entityEventRepository.saveAll(events)
        logger.info("Added ${entitiesSave.size} new data rows and ${eventsSave.size} to the repository")
        return ResponseEntity.ok().build()
    }

    @ApiOperation(value = "Generate new data rows")
    @PostMapping("entities/generate")
    fun addToRepo(@RequestParam number: Int): ResponseEntity<Void> {
        val entities = entityFactory.getEntities(number)
        val entitiesSave = entityRepository.saveAll(entities)
        val events = entitiesSave.map { EntityEventModel(it.id!!, EventType.CREATED, it.attributeMap) }
        val eventsSave = entityEventRepository.saveAll(events)
        logger.info("Added ${entitiesSave.size} new data rows and ${eventsSave.size} to the repository")
        return ResponseEntity.ok().build()
    }

    @ApiOperation(value = "Mutates the content of the repository accordingly to project configuration")
    @PutMapping("entities/update")
    fun updateRepo(): ResponseEntity<Void> {
        val entities = entityRepository.findAll()
        val (updatedEntities, events) = entityManipulator.randomiseCollection(entities)
        val entitiesSave = entityRepository.saveAll(updatedEntities)
        val eventsSave = entityEventRepository.saveAll(events)
        logger.info("Updated collection of ${entitiesSave.size} applying ${eventsSave.size} events")
        return ResponseEntity.ok().build()
    }

    @ApiOperation(value = "Get data rows according to sync policy")
    @GetMapping("entities/policy")
    fun getEntitiesByPolicy(
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
            SyncPolicy.EVENT -> {
                val events = if (date != null) entityEventRepository.findNewerThan(date) else entityEventRepository.findAll()
                logger.info("Fetched ${events.size} entity events newer than $date from the repository")
                EventDrivenResponse(events)
            }
        }
        logger.info("Responding with ${response.javaClass.name} to request with sync policy $policy")
        return ResponseEntity.ok(response)
    }
}
