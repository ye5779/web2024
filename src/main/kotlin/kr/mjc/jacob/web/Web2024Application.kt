package kr.mjc.jacob.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
@ServletComponentScan
class Web2024Application {
  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
  runApplication<Web2024Application>(*args)
}
