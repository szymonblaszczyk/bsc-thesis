package pl.ife.tcs.repositoryservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class RepositoryServiceApplication

fun main(args: Array<String>) {
	runApplication<RepositoryServiceApplication>(*args)
	println("Hello World!")
}
