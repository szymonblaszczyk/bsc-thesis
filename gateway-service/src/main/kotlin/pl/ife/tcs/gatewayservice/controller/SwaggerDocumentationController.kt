package pl.ife.tcs.gatewayservice.controller

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import springfox.documentation.swagger.web.SwaggerResource
import springfox.documentation.swagger.web.SwaggerResourcesProvider

@Component
@Primary
@EnableAutoConfiguration
class SwaggerDocumentationController: SwaggerResourcesProvider {

    override fun get(): MutableList<SwaggerResource> = mutableListOf(
            getSwaggerResource("repository-service", "/api/repository-service/v2/api-docs", "2.0"),
            getSwaggerResource("basic-client-service", "/api/basic-client-service/v2/api-docs", "2.0"),
            getSwaggerResource("diff-client-service", "/api/diff-client-service/v2/api-docs", "2.0"),
            getSwaggerResource("event-client-service", "/api/event-client-service/v2/api-docs", "2.0")
    )

    private fun getSwaggerResource(name: String, location: String, version: String): SwaggerResource {
        val swaggerResource = SwaggerResource()
        swaggerResource.name = name
        swaggerResource.location = location
        swaggerResource.swaggerVersion = version
        return swaggerResource
    }

}