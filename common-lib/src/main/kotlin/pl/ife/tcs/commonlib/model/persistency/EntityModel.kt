package pl.ife.tcs.commonlib.model.persistency

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import javax.persistence.*

@Entity
class EntityModel(
        @ElementCollection
        val attributeMap: MutableMap<String, Long?>
) : AbstractKotlinJpaPersistable<Long>() {

    val attributes: Set<String>
        @JsonIgnore @Transient
        get() = attributeMap.keys
}