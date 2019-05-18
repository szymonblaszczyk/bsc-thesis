package pl.ife.tcs.repositoryservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.ife.tcs.commonlib.model.persistency.*
import pl.ife.tcs.commonlib.util.RandUtil

@Service
class EntityManipulator {

    @Value("\${thesis.entity.update.operation.batch:1}") val entityUpdateBatch: Int = 1
    @Value("\${thesis.entity.update.collection.batch:1}") val collectionUpdateBatch: Int = 1

    fun randomiseCollection(collection: MutableList<EntityModel>): Pair<List<EntityModel>, List<EntityEventModel>> {
        return randomiseCollection(collection, collectionUpdateBatch)
    }

    fun randomiseCollection(collection: MutableList<EntityModel>, n: Int): Pair<List<EntityModel>, List<EntityEventModel>> {
        val randomEntities = RandUtil.pickRandomElements(collection, n)
        val changes = randomEntities.map { randomise(it) }
        return Pair(collection, changes.map { it.second })
    }

    fun randomise(entity: EntityModel): Pair<EntityModel, EntityEventModel> {
        return randomise(entity, entityUpdateBatch)
    }

    fun randomise(entity: EntityModel, n: Int): Pair<EntityModel, EntityEventModel>  {
        val changes = mutableMapOf<String, EntityAttributeValueChangeModel<Long>>()
        val attributes = entity.attributes.toList()
        val randomAttributes = RandUtil.pickRandomElements(attributes, n)
        randomAttributes.forEach {
            val updatedValue = entity.attributeMap.toMutableMap()[it]
            val randomValue = RandUtil.getRandomLong()
            changes[it] = EntityAttributeValueChangeModel(updatedValue, randomValue)
            entity.attributeMap.toMutableMap()[it] = randomValue
        }
        val event = EntityUpdatedEventModel(entity.id!!, changes)
        return Pair(entity, event)
    }
}