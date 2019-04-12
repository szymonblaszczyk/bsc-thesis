package pl.ife.tcs.commonlib.model.persistency

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Transient

@Entity
class EntityModel(
        @ElementCollection
        val attributeMap: Map<String, Long?>
) : AbstractKotlinJpaPersistable<Long>() {

    val attributes: Set<String>
        @JsonIgnore @Transient get() = attributeMap.keys

}