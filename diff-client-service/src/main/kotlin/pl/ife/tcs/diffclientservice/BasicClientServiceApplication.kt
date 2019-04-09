package pl.ife.tcs.diffclientservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableDiscoveryClient
@EnableSwagger2
@EntityScan("pl.ife.tcs.*")
class DiffClientServiceApplication

fun main(args: Array<String>) {
	runApplication<DiffClientServiceApplication>(*args)
}
