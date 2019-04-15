package pl.ife.tcs.commonlib.model.networking

import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.ife.tcs.commonlib.model.persistency.EntityEventModel
import pl.ife.tcs.commonlib.model.persistency.EntityModel

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, visible = true)
sealed class FlexibleResponseModel
class SnapshotResponse(val collection: List<EntityModel>): FlexibleResponseModel()
class DifferentialResponse(val collection: List<EntityModel>): FlexibleResponseModel()
class EventDrivenResponse(val collection: List<EntityEventModel>): FlexibleResponseModel()
class ErrorResponse(val error: String): FlexibleResponseModel()