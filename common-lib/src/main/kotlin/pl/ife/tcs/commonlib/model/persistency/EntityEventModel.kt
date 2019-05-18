package pl.ife.tcs.commonlib.model.persistency

import com.fasterxml.jackson.annotation.JsonTypeInfo
import javax.persistence.ElementCollection
import javax.persistence.Entity

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, visible = true)
@Entity
sealed class EntityEventModel: AbstractKotlinJpaPersistable<Long>()
@Entity class EntityCreatedEventModel(
        val entityId: Long,
        @ElementCollection val values: Map<String, Long?>
): EntityEventModel()
@Entity class EntityUpdatedEventModel(
        val entityId: Long,
        @ElementCollection val values: Map<String, EntityAttributeValueChangeModel<Long>>
): EntityEventModel()