package kr.mjc.jacob.web.servlets

import jakarta.servlet.http.HttpServletRequest
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.thymeleaf.context.WebContext

@Component
class RequestHandler(private val userRepository: UserRepository) {

  val sort = Sort.by("id").descending()

  fun userList(req: HttpServletRequest, context: WebContext) {
    val page = req.getParameter("page")?.toInt() ?: 0
    val users = userRepository.findAll(PageRequest.of(page, 20, sort))
    context.setVariable("users", users)
  }

  fun userDetail(req: HttpServletRequest, context: WebContext) {
    val id = req.getParameter("id").toLong()
    val user = userRepository.findById(id).orElseThrow()
    context.setVariable("user", user)
  }
}