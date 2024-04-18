package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@WebServlet("/dispatcher/*")
class SimpleDispatcherServlet : HttpServlet() {

  @Autowired
  lateinit var exampleHandler: ExampleRequestHandler
  @Autowired
  lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    println("servletPath = ${req.servletPath}") // 이 서블릿의 path: /dispatcher
    println("pathInfo = ${req.pathInfo}") // /dispatcher 후의 uri
    val context = Context()
    when (req.pathInfo) {
      "/examples/hello" -> exampleHandler.hello(req, context)
      "/examples/users" -> exampleHandler.users(req, context)
    }

    resp.contentType = "text/html"
    templateEngine.process(req.pathInfo, context, resp.writer)
  }
}
