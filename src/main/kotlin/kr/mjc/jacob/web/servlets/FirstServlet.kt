package kr.mjc.jacob.web.servlets

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/servlets/first")
class FirstServlet : HttpServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    // request 영역에 title 저장
    req.setAttribute("title", "This is title.")

    // session 영역에 username 저장
    req.session.setAttribute("username", "Jacob")

    // application 영역에 categories 저장
    servletContext.setAttribute("categories", listOf("액션", "서스펜스", "멜로"))

    req.getRequestDispatcher("/servlets/second").forward(req, resp)
  }
}
