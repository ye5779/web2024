package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Controller
class ExampleController(val userRepository: UserRepository) {

  @GetMapping("/handler-methods/")
  fun index(): String {
    return "handler-methods/index" // /templates/handler-methods/index.html
  }

  /** request.getParameter() */
  @PostMapping("/handler-methods/request")
  fun request(request: HttpServletRequest): String {
    val username = request.getParameter("username")
    val password = request.getParameter("password")
    val firstName = request.getParameter("firstName")
    println(
        "request: username: $username, password: $password, firstName: $firstName")
    return "handler-methods/user" // /templates/handler-methods/user.html
  }

  /** @RequestParam */
  @PostMapping("/handler-methods/requestParam")
  fun requestParam(@RequestParam("username") username: String,
                   @RequestParam("password") password: String,
                   @RequestParam("firstName") firstName: String): String {
    println(
        "requestParam: username: $username, password: $password, firstName: $firstName")
    return "handler-methods/user"
  }

  /** @ModelAttribute */
  @PostMapping("/handler-methods/modelAttribute")
  fun modelAttribute(@ModelAttribute("user") user: User): String {
    user.apply { dateJoined = LocalDateTime.now(); lastLogin = dateJoined }
    println("@ModelAttribute: $user")
    return "handler-methods/user"
  }

  @GetMapping("/handler-methods/profile")
  fun profile() {
    // /templates/handler-methods/profile.html
  }

  /** HttpSession */
  @PostMapping("/handler-methods/login")
  fun login(session: HttpSession, username: String): String {
    val user = userRepository.findByUsername(username)
    session.setAttribute("user", user)
    return "redirect:/handler-methods/profile"
  }

  /** session.invalidate() */
  @PostMapping("/handler-methods/logout")
  fun logout(session: HttpSession): String {
    session.invalidate()
    return "redirect:/handler-methods/"
  }

  /** @SessionAttribute */
  @GetMapping("/handler-methods/sessionAttribute")
  fun sessionAttribute(@SessionAttribute("user") user: User?): String {
    println("@SessionAttribute: $user")
    return "handler-methods/profile" // /templates/handler-methods/profile.html
  }
}