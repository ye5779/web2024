package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ExampleController(private val userRepository: UserRepository) {

  @GetMapping("/examples/hello")
  fun hello(req: HttpServletRequest, model: Model) {
    model.addAttribute("name", "Jacob")
  }

  @GetMapping("/examples/users")
  fun users(req: HttpServletRequest, model: Model) {
    val pageable: Pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "id")
    val users = userRepository.findAll(pageable)
    model.addAttribute("users", users)
  }
}
