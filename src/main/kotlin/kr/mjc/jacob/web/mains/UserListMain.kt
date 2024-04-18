package kr.mjc.jacob.web.mains

import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun thymeleaf(): TemplateEngine {
  val templateResolver = ClassLoaderTemplateResolver().apply {
    prefix = "/templates/"
    suffix = ".html"
  }
  return TemplateEngine().apply {
    setTemplateResolver(templateResolver)
  }
}

fun main() {

}