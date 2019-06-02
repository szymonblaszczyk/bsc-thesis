package pl.ife.tcs.repositoryservice.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.stereotype.Service
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import pl.ife.tcs.commonlib.util.RandUtil

@Service
@RefreshScope
class EntityFactory {

    @Value("\${thesis.table.length:1}") val tableLength: Int = 1
    @Value("\${thesis.entity.width:1}") val entityWidth: Int = 1

    /**
     * Returns a list of {@link pl.ife.tcs.commonlib.model.persistency.EntityModel}
     * of length given by the project properties
     * */
    fun getEntities(): List<EntityModel> {
        return getEntities(tableLength)
    }

    /**
     * Returns a list of {@link pl.ife.tcs.commonlib.model.persistency.EntityModel}
     * of length given by
     *
     * @param n the desired number of new objects
     * */
    fun getEntities(n: Int): List<EntityModel> {
        return List(n) { getEntity() }
    }

    /**
     * Returns a single new {@link pl.ife.tcs.commonlib.model.persistency.EntityModel}
     * object with a number of attributes given by the project properties
     * all initialised with random values
     * */
    fun getEntity(): EntityModel {
        return getEntity(true, entityWidth)
    }

    /**
     * Returns a single new {@link pl.ife.tcs.commonlib.model.persistency.EntityModel}
     * object with a number of attributes given by the project properties
     *
     * @param initialised whether or not the attributes should be initalised
     * with random values (if true) or with null (if false)
     * @param n the desired number of attributes in the object
     * */
    fun getEntity(initialised: Boolean, n: Int): EntityModel {
        val attributes = mutableMapOf<String, Long?>()
        for (i in 0..n) { if (initialised) attributes["Val$i"] = RandUtil.getRandomLong() }
        return EntityModel( attributes )
    }
}