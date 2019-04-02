package pl.ife.tcs.basicclientservice.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.logging.Logger

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Value("\${swagger.docket.api.package:}")
    val swaggerPackage: String = ""

    @Value("\${swagger.docket.path:}")
    val swaggerPath: String = ""

    private val logger = Logger.getLogger(SwaggerConfig::class.simpleName)

    @Bean
    fun swagger(): Docket {
        logger.info("Initialising Docket Bean with base package $swaggerPackage and path $swaggerPath")
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerPackage))
                .paths(PathSelectors.any())
                .build()
    }
}