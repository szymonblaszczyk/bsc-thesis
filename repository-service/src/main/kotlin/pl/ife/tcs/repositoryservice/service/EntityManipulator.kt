package pl.ife.tcs.repositoryservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Service
import pl.ife.tcs.commonlib.model.persistency.EntityEventModel
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.commonlib.model.persistency.EventType
import pl.ife.tcs.commonlib.util.RandUtil

@Service
@RefreshScope
class EntityManipulator {

    @Value("\${thesis.entity.update.operation.batch:1}") val entityUpdateBatch: Int = 1
    @Value("\${thesis.entity.update.collection.batch:1}") val collectionUpdateBatch: Int = 1

    fun randomiseCollection(collection: MutableList<EntityModel>): Pair<List<EntityModel>, List<EntityEventModel>> {
        return randomiseCollection(collection, collectionUpdateBatch)
    }

    fun randomiseCollection(collection: MutableList<EntityModel>, n: Int): Pair<List<EntityModel>, List<EntityEventModel>> {
        val randomEntities = RandUtil.pickRandomElements(collection, n)
        val changes = randomEntities.map { randomise(it) }
        return Pair(changes.map { it.first }, changes.flatMap { it.second })
    }

    fun randomise(entity: EntityModel): Pair<EntityModel, List<EntityEventModel>> {
        return randomise(entity, entityUpdateBatch)
    }

    fun randomise(entity: EntityModel, n: Int): Pair<EntityModel, List<EntityEventModel>>  {
        val changeEvents = mutableListOf<EntityEventModel>()
        val attributes = entity.attributes.toList()
        val randomAttributes = RandUtil.pickRandomElements(attributes, n)
        randomAttributes.forEach {
            val randomValue = RandUtil.getRandomLong()
            changeEvents.add(EntityEventModel(entity.id!!, EventType.EDITED, mapOf(Pair(it, randomValue))))
            entity.attributeMap[it] = randomValue
        }
        return Pair(entity, changeEvents)
    }
}