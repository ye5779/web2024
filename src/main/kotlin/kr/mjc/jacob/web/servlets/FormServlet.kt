package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.webContext
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine

@WebServlet("/servlets/form")
class FormServlet : HttpServlet() {

  @Autowired lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    println("get processing...")
    process(req, resp)
  }

  override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    println("post processing...")
    process(req, resp)
  }

  private fun process(req: HttpServletRequest, resp: HttpServletResponse) {
    resp.contentType = "text/html"
    templateEngine.process("examples/form", webContext(req, resp), resp.writer)
  }
}
