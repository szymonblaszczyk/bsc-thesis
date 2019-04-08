package pl.ife.tcs.basicclientservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import java.time.LocalDateTime

interface EntityRepository : JpaRepository<EntityModel, Long> {

    @Query("SELECT MAX(m.dateUpdated) FROM EntityModel m")
    fun findNewestUpdateDate(): LocalDateTime?
}