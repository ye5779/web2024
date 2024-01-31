package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.User
import kr.mjc.jacob.web.dao.UserDao
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import java.net.URLEncoder
import java.nio.charset.Charset

/**
 * Servlet API를 사용하지 않는 컨트롤러
 */
@Controller
class UserController(val userDao: UserDao) {

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * 회원목록
   */
  @GetMapping("/user/users")
  fun users(limit: Limit, model: Model, req: HttpServletRequest) {
    model.addAttribute("users", userDao.listUsers(limit))
  }

  /**
   * 회원가입 화면
   */
  @GetMapping("/user/signup")
  fun signupForm(session: HttpSession) {
    session.invalidate()
  }

  /**
   * 회원가입
   */
  @PostMapping("/user/signup")
  fun signup(user: User, session: HttpSession): String {
    try {
      log.debug("user = {}", user)
      userDao.addUser(user) // 등록 성공
      return signin(email = user.email, password = user.password,
          session = session)
    } catch (e: DataAccessException) { // 등록 실패
      log.error(e.cause.toString())
      return "redirect:/user/signup?mode=FAILURE"
    }
  }

  /**
   * 로그인 화면
   */
  @GetMapping("/user/signin")
  fun signinForm(req: HttpServletRequest, model: Model) {
    model.addAttribute("referer", req.getHeader("referer"))
    req.session.invalidate()
  }

  /**
   * 로그인
   */
  @PostMapping("/user/signin")
  fun signin(email: String, password: String,
      redirectUrl: String = "/user/users", session: HttpSession): String {
    try {
      val user = userDao.login(email, password) // 로그인 성공
      session.setAttribute("user", user)
      return "redirect:$redirectUrl"
    } catch (e: DataAccessException) { // 로그인 실패
      return "redirect:/user/signin?mode=FAILURE&redirectUrl=" + URLEncoder.encode(
          redirectUrl, Charset.defaultCharset())
    }
  }

  /**
   * 비밀번호변경
   */
  @PostMapping("/user/password")
  fun password(@SessionAttribute user: User, password: String,
      newPassword: String): String {
    val updatedRows = userDao.updatePassword(user.userId, password, newPassword)
    return if (updatedRows >= 1) // 업데이트 성공
      "redirect:/user/myInfo"
    else  // 업데이트 실패
      "redirect:/user/password?mode=FAILURE"
  }

  /**
   * 회원정보
   */
  @GetMapping("/user/user")
  fun user(userId: Int, model: Model) {
    model.addAttribute("user", userDao.getUser(userId))
  }

  /**
   * 로그아웃
   */
  @GetMapping("/user/signout")
  fun signout(session: HttpSession): String {
    session.invalidate()
    return "redirect:/user/users"
  }

  /**
   * just forward
   */
  @GetMapping("/{_:post|user}/**", "/login")
  fun pass(req: HttpServletRequest) {
    log.debug("servletPath = {}", req.servletPath)
  }
}
