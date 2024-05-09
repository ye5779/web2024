package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.webContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine

@WebServlet("/servlets/lifecycle")
class LifeCycleServlet : HttpServlet() {

  @Autowired lateinit var templateEngine: TemplateEngine

  private val log = LoggerFactory.getLogger(this.javaClass)

  override fun init() {
    log.info("init LifeCycleServlet.")
  }

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    log.debug("process request.")
    resp.contentType = "text/html"
    templateEngine.process("examples/hello", webContext(req, resp), resp.writer)
  }

  override fun destroy() {
    log.info("destroy LifeCycleServlet.")
  }
}
