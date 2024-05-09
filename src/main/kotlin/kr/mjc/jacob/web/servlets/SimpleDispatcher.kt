package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.webContext
import org.springframework.beans.factory.annotation.Autowired
import org.thymeleaf.TemplateEngine

@WebServlet("/mvc/*")
class SimpleDispatcher : HttpServlet() {

  @Autowired lateinit var requestHandler: RequestHandler
  @Autowired lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    println("servletPath = ${req.servletPath}") // 이 서블릿의 path: /mvc
    println("pathInfo = ${req.pathInfo}") // /mvc 뒤의 uri

    val context = webContext(req, resp)
    when (req.pathInfo) {
      "/user/list" -> requestHandler.userList(req, context)
      "/user/detail" -> requestHandler.userDetail(req, context)
    }

    val template = req.pathInfo.substring(1)  // '/' 제거
    resp.contentType = "text/html"
    templateEngine.process(template, context, resp.writer)
  }
}