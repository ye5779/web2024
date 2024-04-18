package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

/**
 * 서블릿은 컨스트럭터를 사용하지 않는다.
 * setter를 사용해서 injection을 한다.
 */
@WebServlet("/servlets/users")
class UserListServlet : HttpServlet() {

  @Autowired
  lateinit var userRepository: UserRepository

  @Autowired
  lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val users =
      userRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "id"))

    val context = Context()
    context.setVariable("users", users)

    resp.contentType = "text/html"
    templateEngine.process("examples/users", context, resp.writer)
  }
}