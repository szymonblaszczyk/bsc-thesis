package pl.ife.tcs.commonlib.model.persistency

import javax.persistence.Entity

@Entity
class EntityModel(
        var booleanValue: Boolean,
        var intValue: Int,
        var longValue: Long,
        var doubleValue: Double
) : AbstractKotlinJpaPersistable<Long>()