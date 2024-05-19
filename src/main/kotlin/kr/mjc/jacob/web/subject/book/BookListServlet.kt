package kr.mjc.jacob.web.subject.book

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.webContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.thymeleaf.TemplateEngine

@WebServlet("/servlets/book/list")
class BookListServlet : HttpServlet() {

  @Autowired lateinit var bookRepository: BookRepository
  @Autowired lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val books =
      bookRepository.findAll(PageRequest.of(0, 100, Sort.by("id").descending()))
    val context = webContext(req, resp).apply { setVariable("books", books) }

    resp.contentType = "text/html"
    templateEngine.process("subject/book/list", context, resp.writer)
  }
}