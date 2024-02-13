package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Controller
class ExampleController(val userDao: UserDao) {

  @Autowired lateinit var thymeleaf: TemplateEngine

  @GetMapping("/examples/hello")
  fun hello2(req: HttpServletRequest, resp: HttpServletResponse) {
    val context = Context()
    context.setVariable("name", "Jacob")
    val result = thymeleaf.process("/templates/servlets/hello.html", context)
    resp.contentType = "text/html"
    resp.writer.println(result)
  }

  @GetMapping("/examples/users")
  fun users(req: HttpServletRequest, resp: HttpServletResponse) {
    val users = userDao.listUsers(Limit(count = 10, page = 1))
    val context = Context()
    context.setVariable("users", users)
    val result = thymeleaf.process("/templates/user/users.html", context)
    resp.contentType = "text/html"
    resp.writer.println(result)
  }
}
