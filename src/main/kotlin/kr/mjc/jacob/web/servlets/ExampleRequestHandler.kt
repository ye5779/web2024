package kr.mjc.jacob.web.servlets

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.UserDao
import org.springframework.stereotype.Controller
import org.thymeleaf.context.Context

@Controller
class ExampleRequestHandler(val userDao: UserDao) {

  fun hello(req: HttpServletRequest, resp: HttpServletResponse,
            context: Context) {
    context.setVariable("name", "Jacob")
  }

  fun users(req: HttpServletRequest, resp: HttpServletResponse,
            context: Context) {
    val users = userDao.listUsers(Limit(count = 10, page = 1))
    context.setVariable("users", users)
  }
}