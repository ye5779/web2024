package kr.mjc.jacob.web.examples

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/examples/redirect")
class RedirectServlet : HttpServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    // TODO: 어떤 처리를 한 후에 redirect
    resp.sendRedirect(req.contextPath + "/examples/hello")
  }
}
