package pl.ife.tcs.eventclientservice.adapter

import org.springframework.stereotype.Service
import pl.ife.tcs.commonlib.model.persistency.EntityEventModel
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.commonlib.model.persistency.EventType

@Service
class EntityEventAdapter{

    fun apply(entities: List<EntityModel>, events: List<EntityEventModel>) : List<EntityModel> {
        val (creationEvents, updateEvents) = events.partition { it.eventType == EventType.CREATED }
        val newEntities = creationEvents.map { EntityModel(it.changes.toMutableMap()).apply { id = it.entityId } }
        val sumOfEntities = entities.plus(newEntities)
        return sumOfEntities.map { entity -> apply(entity, updateEvents.find { it.entityId == entity.id }) }
    }

    fun apply(entity: EntityModel, event: EntityEventModel?): EntityModel {
        event?.changes?.forEach {
            (attributeName, newAttributeValue) -> entity.attributeMap[attributeName] = newAttributeValue
        }
        return entity
    }
}
