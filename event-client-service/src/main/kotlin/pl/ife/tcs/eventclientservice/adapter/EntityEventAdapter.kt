package pl.ife.tcs.eventclientservice.adapter

import org.springframework.stereotype.Service
import pl.ife.tcs.commonlib.model.persistency.EntityEventModel
import pl.ife.tcs.commonlib.model.persistency.EntityModel

@Service
class EntityEventAdapter{

    fun apply(entities: List<EntityModel>, events: List<EntityEventModel>): List<EntityModel> {
        val mapping = mutableMapOf<Long, Pair<EntityModel?, EntityEventModel?>>()
        entities.forEach { mapping[it.id!!] = Pair(it, null) }
        events.forEach { mapping[it.entityId] = Pair(mapping[it.entityId]?.first, it) }

        val updatedEntities = mapping.map {  }
    }
}
