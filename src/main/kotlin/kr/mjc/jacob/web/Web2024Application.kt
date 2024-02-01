package kr.mjc.jacob.web

import kr.mjc.jacob.web.controller.AuthInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
@ServletComponentScan(basePackages = ["kr.mjc.jacob.web.examples"])
class Web2024Application : SpringBootServletInitializer(), WebMvcConfigurer {

  @Autowired lateinit var authInterceptor: AuthInterceptor

  /**
   * 인터셉터 등록
   */
  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(authInterceptor)
      .addPathPatterns("/post/postAdd", "/post/postUpdate", "/user/me",
          "/user/password")
  }
  /**
   * war 파일을 위한 구성
   */
  override fun configure(
      application: SpringApplicationBuilder): SpringApplicationBuilder {
    return application.sources(Web2024Application::class.java)
  }


}

fun main(args: Array<String>) {
  runApplication<Web2024Application>(*args)
}
