package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

/** Servlet API를 사용하지 않는 컨트롤러 */
@Controller
class UserControllerUsingServletApi(private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder) {

  companion object {
    private const val LANDING_PAGE = "user/user_list"
    private const val PAGE_SIZE = 10
    private val log =
      LoggerFactory.getLogger(UserControllerUsingServletApi::class.java)
  }

  /** 회원목록 */
  @GetMapping("/user/user_list")
  fun userList(request: HttpServletRequest, model: Model) {
    val page = request.getParameter("page")?.toInt() ?: 0
    val userPage: Page<User> = userRepository.findAll(
        PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
    model.addAttribute("page", userPage)
  }

  /** 회원가입 */
  @PostMapping("/signup")
  fun signup(req: HttpServletRequest, resp: HttpServletResponse) {
    val username = req.getParameter("username")

    val exists = userRepository.existsByUsername(username)
    if (!exists) {   // 이메일이 없음. 등록 진행
      val user = User().apply {
        this.username = username
        password = passwordEncoder.encode(req.getParameter("password"))
        firstName = req.getParameter("firstName")
        dateJoined = LocalDateTime.now()
        lastLogin = LocalDateTime.now()
      }
      userRepository.save(user) // 등록 성공
      req.session.setAttribute("user", user)
      resp.sendRedirect("${req.contextPath}/$LANDING_PAGE")
    } else {  // 이메일 존재. 등록 실패
      log.debug("User already exists")
      resp.sendRedirect("${req.contextPath}/signup?duplicate_email")
    }
  }

  /** 로그인 */
  @PostMapping("/login")
  fun login(req: HttpServletRequest, resp: HttpServletResponse) {
    val username = req.getParameter("username")
    val password = req.getParameter("password")
    val redirectUrl = req.getParameter("redirectUrl")

    val user = userRepository.findByUsername(username)
    if (user != null && passwordEncoder.matches(password, user.password)) {
      // 비밀번호 매치
      user.lastLogin = LocalDateTime.now()
      userRepository.updateLastLogin(user.id, user.lastLogin)
      req.session.setAttribute("user", user)
      if (redirectUrl.isBlank()) resp.sendRedirect(
          "${req.contextPath}/$LANDING_PAGE")
      else resp.sendRedirect(redirectUrl)
    } else {  // 사용자가 없거나 비밀번호가 매치하지 않을 경우
      resp.sendRedirect(
          "${req.contextPath}/login?redirectUrl=${redirectUrl.urlEncoded}&error")
    }
  }

  /** 비밀번호변경 */
  @PostMapping("/user/password")
  fun password(req: HttpServletRequest, resp: HttpServletResponse) {
    val user: User = req.session.getAttribute("user") as User
    val oldPassword = req.getParameter("oldPassword")
    val newPassword = req.getParameter("newPassword")

    if (passwordEncoder.matches(oldPassword, user.password)) { // 비밀번호 매치
      val newPasswordHashed = passwordEncoder.encode(newPassword)
      userRepository.changePassword(user.id, newPasswordHashed) // DB의 비밀번호 변경
      user.password = newPasswordHashed  // 세션의 비밀번호 변경
      resp.sendRedirect("${req.contextPath}/user/profile?password_changed")
    } else {  // 비밀번호 매치 안함
      resp.sendRedirect("${req.contextPath}/user/password?wrong_password")
    }
  }

  /** 회원정보 */
  @GetMapping("/user/user_detail")
  fun userDetail(req: HttpServletRequest, model: Model) {
    val id = req.getParameter("id").toLong()
    try {
      model.addAttribute("user", userRepository.findById(id).orElseThrow())
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST) // 400
    }
  }

  /** 로그아웃 */
  @PostMapping("/logout")
  fun logout(req: HttpServletRequest, resp: HttpServletResponse) {
    req.session.invalidate()
    resp.sendRedirect("${req.contextPath}/$LANDING_PAGE")
  }

  /** 해지 */
  @PostMapping("/user/delete")
  fun unregister(req: HttpServletRequest, resp: HttpServletResponse) {
    val user: User = req.session.getAttribute("user") as User
    val password = req.getParameter("password")

    if (passwordEncoder.matches(password, user.password)) {
      try {
        userRepository.deleteById(user.id)
        logout(req, resp)
      } catch (e: Exception) {
        resp.sendRedirect("${req.contextPath}/user/delete?delete_failure")
      }
    } else resp.sendRedirect("${req.contextPath}/user/delete?wrong_password")
  }

  /** do nothing */
  @GetMapping("/{_:post|user}/**", "/login", "/signup", "/hello", "/examples")
  fun pass(req: HttpServletRequest) {
    log.debug("pass(): servletPath = {}", req.servletPath)
  }
}
