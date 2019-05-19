package pl.ife.tcs.repositoryservice.config

import org.hibernate.event.service.spi.EventListenerRegistry
import org.hibernate.event.spi.EventType
import org.hibernate.event.spi.MergeEvent
import org.hibernate.event.spi.MergeEventListener
import org.hibernate.internal.SessionFactoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import pl.ife.tcs.commonlib.model.persistency.AbstractKotlinJpaPersistable
import java.time.LocalDateTime
import java.util.logging.Logger
import javax.annotation.PostConstruct
import javax.persistence.EntityManagerFactory

/** So far the only operational solution to the problem of not working @see javax.persistance.PreUpdate listener call
 * in @see pl.ife.tcs.commonlib.model.persistency.AbstractKotlinJpaPersistable */

@Component
class AuditableListener @Autowired constructor(
        private val entityManagerFactory: EntityManagerFactory
): MergeEventListener {

    private val logger: Logger = Logger.getLogger(javaClass.simpleName)

    @PostConstruct
    fun init() {
        val sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl::class.java)
        val registry = sessionFactory.serviceRegistry.getService(EventListenerRegistry::class.java)
        registry.getEventListenerGroup(EventType.MERGE).appendListener(this)
    }

    override fun onMerge(event: MergeEvent?) {
        if (event?.entity is AbstractKotlinJpaPersistable<*>) {
            val entity = event.entity as AbstractKotlinJpaPersistable<*>
            val currentDate = LocalDateTime.now()
            entity.dateUpdated = currentDate
            logger.info("Marking update of entity: ${entity.id} at $currentDate")
        }
    }

    override fun onMerge(event: MergeEvent?, copiedAlready: MutableMap<Any?, Any?>?) {
        //do Nothing
    }
}