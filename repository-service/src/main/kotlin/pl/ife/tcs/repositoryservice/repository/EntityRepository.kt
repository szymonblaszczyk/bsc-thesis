package pl.ife.tcs.repositoryservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pl.ife.tcs.commonlib.model.persistency.EntityModel
import java.time.LocalDateTime

interface EntityRepository : JpaRepository<EntityModel, Long> {

    @Query("SELECT m FROM EntityModel m WHERE m.dateUpdated >= :date")
    fun findNewerThan(@Param("date") date: LocalDateTime?): List<EntityModel>
}