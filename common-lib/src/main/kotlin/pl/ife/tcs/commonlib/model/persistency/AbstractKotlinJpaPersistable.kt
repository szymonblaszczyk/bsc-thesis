package pl.ife.tcs.commonlib.model.persistency

import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

/** See <a href="https://kotlinexpertise.com/hibernate-with-kotlin-spring-boot/">Kotlin Expertise</a> */

@MappedSuperclass
abstract class AbstractKotlinJpaPersistable<T : Serializable> {

    @Id
    @GeneratedValue
    var id: T? = null

//    @Version
//    private var version: Long = 0

    var dateCreated: LocalDateTime? = null
    var dateUpdated: LocalDateTime? = null


    @PrePersist
    @PreUpdate
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