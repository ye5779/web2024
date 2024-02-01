package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor : HandlerInterceptor {
  override fun preHandle(request: HttpServletRequest,
      response: HttpServletResponse, handler: Any): Boolean {
    return super.preHandle(request, response, handler)
  }
}