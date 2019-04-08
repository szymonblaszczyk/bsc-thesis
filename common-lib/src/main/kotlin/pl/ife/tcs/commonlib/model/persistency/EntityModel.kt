package pl.ife.tcs.commonlib.model.persistency

import javax.persistence.Entity

@Entity
class EntityModel(
        val value: Int,
        var positiveLong: Long = Long.MAX_VALUE,
        var negativeLong: Long = Long.MIN_VALUE,
        var positiveDouble: Double = Double.MAX_VALUE,
        var negativeDouble: Double = Double.MIN_VALUE
) : AbstractKotlinJpaPersistable<Long>()