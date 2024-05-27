package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

/** Servlet API를 사용하지 않는 핸들러 메서드들 */
@Controller
class UserControllerV2(val userRepository: UserRepository,
                       val passwordEncoder: PasswordEncoder) {

  companion object {
    private const val PAGE_SIZE: Int = 20
    private val sort = Sort.by("id").descending()
    private const val LANDING_PAGE = "/user/list"
  }

  /** 회원 목록 */
  @GetMapping("/user/list")
  fun list(@RequestParam("page") page: Int = 0, session: HttpSession,
           model: Model) {
    session.setAttribute("page", page) // 현재 페이지를 세션에 저장
    val users = userRepository.findAll(PageRequest.of(page, PAGE_SIZE, sort))
    model.addAttribute("list", users)
  }

  /** 회원 정보 */
  @GetMapping("/user/detail")
  fun detail(@RequestParam("id") id: Long, model: Model) {
    val user = userRepository.findById(id).orElseThrow()
    model.addAttribute("user", user)
  }

  /** 일반 화면 */
  @GetMapping("/user/signup", "/user/login", "/user/profile")
  fun pass() {
    // Do nothing
  }

  /** 회원가입 */
  @PostMapping("/user/signup")
  fun signup(@ModelAttribute("user") user: User, session: HttpSession): String {
    val exists = userRepository.existsByUsername(user.username)
    if (!exists) {   // 이메일이 없음. 등록 진행
      user.apply {
        password = passwordEncoder.encode(user.password)
        dateJoined = LocalDateTime.now()
        lastLogin = dateJoined
      }
      userRepository.save(user) // 등록 성공
      session.setAttribute("user", user)  // 로그인
      return "redirect:$LANDING_PAGE"
    } else {  // 이메일 존재. 등록 실패
      return "redirect:/user/signup?error"
    }
  }

  /** 로그인 */
  @PostMapping("/user/login")
  fun login(@RequestParam("username") username: String,
            @RequestParam("password") password: String,
            session: HttpSession): String {
    val user = userRepository.findByUsername(username)
    if (user != null && passwordEncoder.matches(password, user.password)) {
      // 비밀번호 매치
      session.setAttribute("user", user)
      user.lastLogin = LocalDateTime.now()
      userRepository.updateLastLogin(user.id, user.lastLogin)
      return "redirect:$LANDING_PAGE"
    } else {  // 사용자가 없거나 비밀번호가 매치하지 않을 경우
      return "redirect:/user/login?error"
    }
  }

  /** 로그아웃 */
  @PostMapping("/user/logout")
  fun logout(session: HttpSession): String {
    session.invalidate()
    return "redirect:$LANDING_PAGE"
  }

  /** 해지 */
  @PostMapping("/user/delete")
  fun delete(@SessionAttribute("user") user: User): String {
    try {
      userRepository.deleteById(user.id)
      return "forward:/user/logout"
    } catch (e: DataIntegrityViolationException) {
      throw DataIntegrityViolationException(
          "등록한 글들이 있어서 해지할 수 없습니다.\n글들을 먼저 삭제하세요.")
    }
  }
}
