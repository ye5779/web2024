package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserController(val userRepository: UserRepository) {

  companion object {
    private const val PAGE_SIZE: Int = 20
  }

  @GetMapping("/user/list")
  fun userList(req: HttpServletRequest, model: Model) {
    val page = req.getParameter("page")?.toInt() ?: 0
    val users = userRepository.findAll(
        PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
    model.addAttribute("users", users)
  }

  @GetMapping("/user/detail")
  fun userDetail(req: HttpServletRequest, model: Model) {
    val id = req.getParameter("id").toLong()
    val user = userRepository.findById(id).orElseThrow()
    model.addAttribute("user", user)
  }
}