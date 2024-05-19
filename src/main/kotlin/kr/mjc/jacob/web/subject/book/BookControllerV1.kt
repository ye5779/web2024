package kr.mjc.jacob.web.subject.book

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/** ServletAPI를 사용하는 controller */
@Controller
class BookControllerV1(val bookRepository: BookRepository) {

  val sort = Sort.by("id").descending()

  @GetMapping("/subject/book/list")
  fun list(req: HttpServletRequest, model: Model) {
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