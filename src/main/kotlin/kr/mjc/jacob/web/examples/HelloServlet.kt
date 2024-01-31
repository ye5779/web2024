package kr.mjc.jacob.web.examples

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

@WebServlet("/examples/hello")
class HelloServlet : HttpServlet() {
  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val html = """
        <!DOCTYPE html>
        <html>
        <body>
        <h1>Hello, 서블릿!</h1>
        </body>
        </html>
        """.trimIndent()

    resp.contentType = "text/html"
    resp.writer.println(html)
  }
}
