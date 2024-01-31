package kr.mjc.jacob.web

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Bean
  fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
    return http.authorizeHttpRequests { requests ->
      requests.requestMatchers("/", "/css/*").permitAll().anyRequest()
        .authenticated()
    }.formLogin { form -> form.loginPage("/login").permitAll() }
      .logout { logout -> logout.permitAll() }.build()
  }

  @Bean
  fun userDetailsService(): UserDetailsService {
    val user1 = User("user1", passwordEncoder().encode("password1"),
        authorities("ROLE_USER"))
    val user2 = User("user2", passwordEncoder().encode("password2"),
        authorities("ROLE_USER"))
    return InMemoryUserDetailsManager(user1, user2)
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

  private fun authorities(
      vararg authorities: String): List<SimpleGrantedAuthority> {
    return authorities.map { authority -> SimpleGrantedAuthority(authority) }
  }
}
