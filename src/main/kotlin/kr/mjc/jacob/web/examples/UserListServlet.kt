package kr.mjc.jacob.web.examples

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.UserDao
import org.springframework.beans.factory.annotation.Autowired

@WebServlet("/examples/users")
class UserListServlet : HttpServlet() {

  @Autowired lateinit var userDao: UserDao

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val users = userDao.listUsers(Limit(count = 10, page = 1))
    val builder = StringBuilder()
    users.forEach { user ->
      builder.append("<div>${user.userId}, ${user.name}, ${user.email}</div>")
    }

    val html = """
        <!DOCTYPE html>
        <html>
        <body>
        <h3>회원목록</h3>
        $builder
        </body>
        </html>
        """.trimIndent()

    resp.contentType = "text/html"
    resp.writer.println(html)
  }
}