package kr.mjc.jacob.web

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@SpringBootApplication
@ServletComponentScan(basePackages = ["kr.mjc.jacob.web.servlets"])
class Web2024Application

fun main(args: Array<String>) {
  runApplication<Web2024Application>(*args)
}
