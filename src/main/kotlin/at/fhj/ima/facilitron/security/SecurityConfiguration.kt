package at.fhj.ima.facilitron.security

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import java.lang.Exception

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
class SecurityConfiguration(
    @Autowired val jwtAuthFilter:JwtAuthenticationFilter,
    @Autowired val authenticationProvider: AuthenticationProvider
) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf()
            .ignoringRequestMatchers("/auth/**")
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/public/**").permitAll()
            .requestMatchers("/auth/**").permitAll()
            // when changing --> ALSO IN DefaultURL !!!
            .requestMatchers("/", "/public").permitAll()
            .requestMatchers(DefaultURL.USER_CREATE, DefaultURL.USER_EDIT).hasAnyAuthority("HR", "ADMIN")
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

}