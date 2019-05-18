package pl.ife.tcs.commonlib.model.persistency

import javax.persistence.Entity

class EntityAttributeValueChangeModel<T>(
        val oldAttributeValue: T?,
        val newAttributeValue: T?
)