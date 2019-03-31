package pl.ife.tcs.repositoryservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Value("\${swagger.docket.api.package:}")
    val swaggerPackage: String = ""

    @Value("\${swagger.docket.path:}")
    val swaggerPath: String = ""

    @Bean
    fun swagger(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.basePackage(swaggerPackage))
            .paths(PathSelectors.ant("$swaggerPath/*"))
            .build()
}