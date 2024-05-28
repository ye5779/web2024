package kr.mjc.jacob.web.subject.book

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/** Servlet API를 사용하는 핸들러 메서드들 */
//@Controller
class BookControllerV1(val bookRepository: BookRepository) {

  private val sort = Sort.by("id").descending()

  @GetMapping("/subject/book/list")
  fun list(model: Model) {
    val books = bookRepository.findAll(PageRequest.of(0, 100, sort))
    model.addAttribute("books", books)
  }

  @PostMapping("/subject/book/add")
  fun add(req: HttpServletRequest, resp: HttpServletResponse) {
    val book = Book().apply {
      title = req.getParameter("title")
      author = req.getParameter("author")
    }
    bookRepository.save(book)
    resp.sendRedirect("${req.contextPath}/subject/book/list")
  }
}