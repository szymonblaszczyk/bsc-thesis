package pl.ife.tcs.commonlib.model

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.PrePersist
import javax.persistence.PreUpdate

@Entity
class EntityModel(
        val value: Int,
        var dateCreated: LocalDateTime?,
        var dateUpdated: LocalDateTime?
) : AbstractKotlinJpaPersistable<Long>() {

    @PrePersist
    @PreUpdate
    public fun updateTimestamps() {
        dateUpdated = LocalDateTime.now()
        dateCreated = dateCreated ?: LocalDateTime.now()
    }
}