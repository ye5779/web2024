package kr.mjc.jacob.web.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor
import java.security.SecureRandom
import java.util.*

/**
 * csrf 토큰을 생성하고 검증하는 인터셉터<br/>
 * req.method == "GET" 일 때 csrf 토큰을 생성해서 세션에 저장한다.
 * req.method == "POST" 일 때 세션의 토큰과 폼에서 올라온 토큰을 비교한다.
 */
@Component
class CsrfInterceptor : HandlerInterceptor {

  companion object {
    private val secureRandom = SecureRandom()
    private val base64Encoder = Base64.getEncoder()
    private const val CSRF = "csrf"
  }

  override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse,
                         handler: Any): Boolean {
    val session: HttpSession = req.session

    if (req.method == "GET") {  // GET일 때 csrf token 생성해서 세션에 저장
      val csrfToken = generateRandomString(32)
      session.setAttribute(CSRF, csrfToken)
      return true
    } else if (req.method == "POST") { // POST일 때 폼의 csrf token 검증
      val csrfToken = session.getAttribute(CSRF) as? String
      if (csrfToken != null) {
        session.removeAttribute(CSRF) // 한번 사용한 토큰 삭제
        val param = req.getParameter("_csrf")
        if (csrfToken == param) return true
      }
    }
    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "CSRF 에러")
  }

  /**
   * 랜덤 문자열 생성
   * @param length 바이트 수
   */
  fun generateRandomString(length: Int): String {
    val bytes = ByteArray(length)
    secureRandom.nextBytes(bytes)
    return base64Encoder.encodeToString(bytes)
  }
}