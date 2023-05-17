package at.fhj.ima.facilitron

import at.fhj.ima.facilitron.service.SecurityRoleService
import dev.robinohs.totpkt.otp.HashAlgorithm
import dev.robinohs.totpkt.otp.totp.TotpGenerator
import dev.robinohs.totpkt.random.RandomGenerator
import dev.robinohs.totpkt.recovery.RecoveryCodeGenerator
import dev.robinohs.totpkt.secret.SecretGenerator
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.time.Duration

@SpringBootApplication
class FacilitronApplication{

	@Bean
	fun init(securityRoleService: SecurityRoleService) = CommandLineRunner{

		// ----- ROLES -----
		securityRoleService.saveRole("EMPLOYEE")
		securityRoleService.saveRole("HR")
		securityRoleService.saveRole("SUPPORT")
		securityRoleService.saveRole("SECTIONMANAGER")
		securityRoleService.saveRole("ADMIN")
		// TODO initialise all available roles (DEV ONLY)

		// ----- EMPLOYEES -----

	}

	@Bean
	fun totpGenerator(): TotpGenerator {
		val generator = TotpGenerator()
		generator.codeLength = 6
		generator.algorithm = HashAlgorithm.SHA1
		generator.timePeriod = Duration.ofSeconds(20)
		return generator
	}

	@Bean
	fun recoveryCodeGenerator(): RecoveryCodeGenerator {
		val generator = RecoveryCodeGenerator()
		generator.blockLength = 5
		return generator
	}

	@Bean
	fun secretGenerator(): SecretGenerator {
		val generator = SecretGenerator()
		generator.randomGenerator = RandomGenerator(charPool = listOf('B', 'C', 'D'))
		return generator
	}

}

fun main(args: Array<String>) {
	runApplication<FacilitronApplication>(*args)
}
