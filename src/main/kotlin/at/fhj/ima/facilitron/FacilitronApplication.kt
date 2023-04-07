package at.fhj.ima.facilitron

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class FacilitronApplication{

	@Bean
	fun init() = CommandLineRunner{
		// TODO fill DB
	}

}

fun main(args: Array<String>) {
	runApplication<FacilitronApplication>(*args)
}
