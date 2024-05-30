package kr.mjc.jacob.web

import kr.mjc.jacob.web.interceptor.AuthInterceptor
import kr.mjc.jacob.web.interceptor.CsrfInterceptor
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
  @Autowired lateinit var csrfInterceptor: CsrfInterceptor

  /** 인터셉터 등록 */
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(authInterceptor)
      .addPathPatterns("/post/create", "/post/update", "/post/delete",
                       "/user/profile", "/user/logout", "/user/delete")

    registry.addInterceptor(csrfInterceptor)
      .addPathPatterns("/user/login", "/user/logout", "/user/signup",
                       "/user/profile", "/post/create", "/post/update",
                       "/post/detail", "/post/delete")
  }

  @Bean
  fun passwordEncoder() = BCryptPasswordEncoder()
}

fun main(args: Array<String>) {
  runApplication<Web2024Application>(*args)
}
