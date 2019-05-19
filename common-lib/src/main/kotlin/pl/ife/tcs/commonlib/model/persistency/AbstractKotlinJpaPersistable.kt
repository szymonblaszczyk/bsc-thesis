package pl.ife.tcs.commonlib.model.persistency

import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/** See <a href="https://kotlinexpertise.com/hibernate-with-kotlin-spring-boot/">Kotlin Expertise</a> */

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AbstractKotlinJpaPersistable<T : Serializable> {

    @Id
    @GenericGenerator(name = "ImportIdGenerator", strategy = "pl.ife.tcs.commonlib.model.persistency.ImportIdGenerator")
    @GeneratedValue(generator = "ImportIdGenerator")
    @Column(unique = true, nullable = false)
    var id: T? = null

    @Column(name = "dateCreated")
    var dateCreated: LocalDateTime? = null
    @Column(name = "dateUpdated")
    var dateUpdated: LocalDateTime? = null


    @PrePersist
    /** @PreUpdate <- Not working for reasons undetermined. @see AuditableListener for a by-pass solution */
    fun updateTimestamps() {
        val currentDate = LocalDateTime.now()
        dateUpdated = currentDate
        dateCreated = dateCreated ?: currentDate
    }

    override fun equals(other: Any?): Boolean {
        other ?: return false

        if (this === other) return true

        if (javaClass != ProxyUtils.getUserClass(other)) return false

        other as AbstractKotlinJpaPersistable<*>

        return if (null == this.id) false else this.id == other.id
    }

    override fun hashCode(): Int = 31

    override fun toString(): String = "Entity of type ${this.javaClass.name} with id: $id"
}