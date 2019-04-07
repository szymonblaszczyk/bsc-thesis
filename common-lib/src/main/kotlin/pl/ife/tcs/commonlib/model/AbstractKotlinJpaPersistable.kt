package pl.ife.tcs.commonlib.model

import org.springframework.data.util.ProxyUtils
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.Id

/** See <a href="https://kotlinexpertise.com/hibernate-with-kotlin-spring-boot/">Kotlin Expertise</a> */
abstract class AbstractKotlinJpaPersistable<T : Serializable> {

    @Id
    @GeneratedValue
    var id: T? = null

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