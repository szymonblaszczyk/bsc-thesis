package pl.ife.tcs.commonlib.model

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, include = JsonTypeInfo.As.PROPERTY, visible = true)
sealed class FlexibleResponseModel
class CollectionResponse(val collection: List<EntityModel>): FlexibleResponseModel()
class ErrorResponse(val error: String): FlexibleResponseModel()