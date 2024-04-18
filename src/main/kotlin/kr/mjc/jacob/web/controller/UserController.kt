package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import kr.mjc.jacob.web.urlEncoded
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

/** Servlet API를 사용하지 않는 컨트롤러 */
//@Controller
class UserController(private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder) {

  companion object {
    private const val LANDING_PAGE = "redirect:/user/user_list"
    private const val PAGE_SIZE = 10
    private val log = LoggerFactory.getLogger(UserController::class.java)
  }

  /** 회원목록 */
  @GetMapping("/user/user_list")
  fun userList(page: Int = 0, model: Model) {
    val userPage: Page<User> = userRepository.findAll(
        PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
    model.addAttribute("page", userPage)
  }

  /** 회원가입 */
  @PostMapping("/signup")
  fun signup(user: User, session: HttpSession): String {
    val exists = userRepository.existsByUsername(user.username)
    if (!exists) {   // 이메일이 없음. 등록 진행
      user.apply {
        password = passwordEncoder.encode(password)
        dateJoined = LocalDateTime.now()
        lastLogin = LocalDateTime.now()
      }
      userRepository.save(user) // 등록 성공
      session.setAttribute("user", user)
      return LANDING_PAGE
    } else {  // 이메일 존재. 등록 실패
      return "redirect:/signup?duplicate_email"
    }
  }

  /** 로그인 */
  @PostMapping("/login")
  fun login(username: String, password: String, redirectUrl: String,
      session: HttpSession): String {
    val user = userRepository.findByUsername(username)
    return if (user != null && passwordEncoder.matches(password,
                                                       user.password)
    ) { // 비밀번호 매치
      user.lastLogin = LocalDateTime.now()
      userRepository.updateLastLogin(user.id, user.lastLogin)
      session.setAttribute("user", user)
      if (redirectUrl.isBlank()) LANDING_PAGE else "redirect:${redirectUrl}"
    } else {  // 사용자가 없거나 비밀번호가 매치하지 않을 경우
      "redirect:/login?redirectUrl=${redirectUrl.urlEncoded}&error"
    }
  }

  /** 비밀번호변경 */
  @PostMapping("/user/password")
  fun password(@SessionAttribute user: User, oldPassword: String,
      newPassword: String): String {
    return if (passwordEncoder.matches(oldPassword, user.password)) { // 비밀번호 매치
      val newPasswordHashed = passwordEncoder.encode(newPassword)
      userRepository.changePassword(user.id, newPasswordHashed) // DB의 비밀번호 변경
      user.password = newPasswordHashed  // 세션의 비밀번호 변경
      "redirect:/user/profile?password_changed"
    } else {  // 비밀번호 매치 안함
      "redirect:/user/password?wrong_password"
    }
  }

  /** 회원정보 */
  @GetMapping("/user/user_detail")
  fun userDetail(id: Long, model: Model) {
    try {
      model.addAttribute("user", userRepository.findById(id).orElseThrow())
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST) // 400
    }
  }

  /** 로그아웃 */
  @PostMapping("/logout")
  fun logout(session: HttpSession): String {
    session.invalidate()
    return LANDING_PAGE
  }

  /** 해지 */
  @PostMapping("/user/delete")
  fun unregister(session: HttpSession, @SessionAttribute user: User,
      password: String): String {
    return if (passwordEncoder.matches(password, user.password)) {
      try {
        userRepository.deleteById(user.id)
        logout(session)
      } catch (e: Exception) {
        "redirect:/user/delete?delete_failure"
      }
    } else "redirect:/user/delete?wrong_password"
  }

  /** just forward */
  @GetMapping("/{_:post|user}/**", "/login", "/signup", "/hello", "/examples")
  fun pass(req: HttpServletRequest) {
    log.debug("servletPath = {}", req.servletPath)
  }
}
