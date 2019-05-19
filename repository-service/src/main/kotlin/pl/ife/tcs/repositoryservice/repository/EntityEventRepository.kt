package pl.ife.tcs.repositoryservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import pl.ife.tcs.commonlib.model.persistency.EntityEventModel
import java.time.LocalDateTime

@Repository
interface EntityEventRepository : JpaRepository<EntityEventModel, Long> {

    @Query("SELECT m FROM EntityEventModel m WHERE m.dateUpdated >= :date ORDER BY dateUpdated DESC")
    fun findNewerThan(@Param("date") date: LocalDateTime?): List<EntityEventModel>
}