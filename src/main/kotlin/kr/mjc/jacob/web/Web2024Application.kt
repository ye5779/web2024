package kr.mjc.jacob.web

import kr.mjc.jacob.web.controller.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
@ServletComponentScan
class Web2024Application : WebMvcConfigurer {

  @Autowired lateinit var authInterceptor: AuthInterceptor

  /** 인터셉터 등록 */
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(authInterceptor)
      .addPathPatterns("/post/create", "/post/update", "/post/delete",
                       "/user/profile", "/user/logout", "/user/delete")
  }

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
  runApplication<Web2024Application>(*args)
}
