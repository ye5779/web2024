package kr.mjc.jacob.web.subject.book

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired

@WebServlet("/subject/book/add")
class AddBookServlet : HttpServlet() {

  @Autowired lateinit var movieRepository: BookRepository

  override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    val book = Book().apply {
      title = req.getParameter("title")
      author = req.getParameter("author")
    }
    movieRepository.save(book)
    resp.sendRedirect("${req.contextPath}/subject/book/list")
  }
}