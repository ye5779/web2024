package kr.mjc.jacob.web.servlets

import jakarta.servlet.http.HttpServletRequest
import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.thymeleaf.context.Context

@Component
class ExampleRequestHandler(val userRepository: UserRepository) {

  fun hello(req: HttpServletRequest, context: Context) {
    context.setVariable("name", "Jacob")
  }

  fun users(req: HttpServletRequest, context: Context) {
    val users: Page<User> =
      userRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "id"))
    context.setVariable("users", users)
  }
}