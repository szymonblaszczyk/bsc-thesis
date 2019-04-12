package pl.ife.tcs.repositoryservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.commonlib.util.RandUtil

@Service
class EntityFactory {

    @Value("\${thesis.table.length:1}") val tableLength: Int = 1
    @Value("\${thesis.entity.length:1}") val entityLength: Int = 1

    val sessionEntityFields: List<String> by lazy { List(entityLength) { RandUtil.getRandomString() } }

    fun generateEntities(n: Int): List<EntityModel> {
        return List(n) { generateEntity() }
    }

    fun generateEntitiesConfigured(): List<EntityModel> {
        return List(tableLength) { generateEntity() }
    }

    fun generateEntity(): EntityModel {
        val attributes = mutableMapOf<String, Long>()
        sessionEntityFields.forEach { attributes[it] = RandUtil.getRandomLong() }
        return EntityModel( attributes )
    }
}