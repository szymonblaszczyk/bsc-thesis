package pl.ife.tcs.commonlib.model

import javax.persistence.Entity

@Entity
class EntityModel(
        val value: Int
) : AbstractKotlinJpaPersistable<Long>()