package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.User
import kr.mjc.jacob.web.dao.UserDao
import kr.mjc.jacob.web.urlEncoded
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute

/**
 * Servlet API를 사용하지 않는 컨트롤러
 */
@Controller
class UserController(val userDao: UserDao) {

  companion object {
    const val LANDING_PAGE = "redirect:/user/users"
  }

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * 회원목록
   */
  @GetMapping("/user/users")
  fun users(limit: Limit, model: Model, req: HttpServletRequest) {
    model.addAttribute("users", userDao.listUsers(limit))
  }

  /**
   * 회원가입
   */
  @PostMapping("/signup")
  fun signup(user: User, session: HttpSession): String {
    try {
      userDao.getUserByEmail(user.email)  // 이메일이 존재함
      return "redirect:/signup?error=duplicateemail"
    } catch (e: DataAccessException) {    // 이메일이 없음
      user.password = BCrypt.hashpw(user.password, BCrypt.gensalt())
      userDao.addUser(user) // 등록 성공
      session.setAttribute("user", user)
      return LANDING_PAGE
    }
  }

  /**
   * 로그인
   */
  @PostMapping("/login")
  fun login(email: String, password: String, redirectUrl: String,
      session: HttpSession): String {
    try {
      val user = userDao.getUserByEmail(email)
      return if (BCrypt.checkpw(password, user?.password)) {
        session.setAttribute("user", user)
        if (redirectUrl.isBlank()) LANDING_PAGE else "redirect:${redirectUrl}"
      } else {  // 비밀번호가 매치하지 않을 경우
        "redirect:/login?error=notmatch&redirectUrl=${redirectUrl.urlEncoded}"
      }
    } catch (e: DataAccessException) { // 사용자 정보가 없을 경우
      return "redirect:/login?error=nouser&redirectUrl=${redirectUrl.urlEncoded}"
    }
  }

  /**
   * 비밀번호변경
   */
  @PostMapping("/user/password")
  fun password(@SessionAttribute user: User, oldPassword: String,
      newPassword: String): String {
    return if (BCrypt.checkpw(oldPassword, user.password)) { // 비밀번호 매치함
      val hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt())
      userDao.updatePassword(user.userId, hashedPassword) // DB의 비밀번호 변경
      user.password = hashedPassword  // 세션의 비밀번호 변경
      "redirect:/user/me?changed"
    } else "redirect:/user/password?error=notmatch" // 비밀번호 매치 안함
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
  @PostMapping("/logout")
  fun logout(session: HttpSession): String {
    session.invalidate()
    return LANDING_PAGE
  }

  /**
   * 해지
   */
  @PostMapping("/unregister")
  fun unregister(session: HttpSession, @SessionAttribute user: User,
      password: String): String {
    return if (BCrypt.checkpw(password, user.password)) {
      userDao.deleteUser(user.userId)
      logout(session)
    } else "redirect:/user/me?error=wrongpassword"
  }

  /**
   * just forward
   */
  @GetMapping("/{_:post|user}/**", "/login", "/signup")
  fun pass(req: HttpServletRequest) {
    log.debug("servletPath = {}", req.servletPath)
  }
}
