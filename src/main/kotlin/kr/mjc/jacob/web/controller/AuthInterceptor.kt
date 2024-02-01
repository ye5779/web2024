package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.dao.User
import kr.mjc.jacob.web.fullUrl
import kr.mjc.jacob.web.urlEncoded
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor : HandlerInterceptor {
  override fun preHandle(request: HttpServletRequest,
      response: HttpServletResponse, handler: Any): Boolean {
    // 로그인 체크
    val user: User? = request.session.getAttribute("user") as User?

    if (user == null) { // 로그인 안했을 경우 로그인 화면으로
      response.sendRedirect(
          "${request.contextPath}/login?redirectUrl=${request.fullUrl.urlEncoded}")
      return false
    }

    // 로그인 한 경우 그대로 진행
    return true
  }
}