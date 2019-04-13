package pl.ife.tcs.commonlib.model.persistency

import javax.persistence.ElementCollection
import javax.persistence.Entity

@Entity
data class EntityEventModel(
        val entityId: Long,
        @ElementCollection
        val changes: Map<String, Long>
): AbstractKotlinJpaPersistable<Long>()