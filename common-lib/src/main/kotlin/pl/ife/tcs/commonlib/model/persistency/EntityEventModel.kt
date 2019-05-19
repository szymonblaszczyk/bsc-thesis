package pl.ife.tcs.commonlib.model.persistency

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Table

@Entity
data class EntityEventModel(
        val entityId: Long,
        val eventType: EventType,
        @ElementCollection
        val changes: Map<String, Long?>
): AbstractKotlinJpaPersistable<Long>()