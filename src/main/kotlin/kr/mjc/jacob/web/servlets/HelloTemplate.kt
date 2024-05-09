package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.webContext
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine

@WebServlet("/servlets/template")
class HelloTemplate : HttpServlet() {

  @Autowired lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    resp.contentType = "text/html"
    templateEngine.process("examples/hello", webContext(req, resp), resp.writer)
  }
}
