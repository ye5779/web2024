package kr.mjc.jacob.web.subject.book

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/** Servlet API를 사용하지 않는 핸들러 메서드들 */
@Controller
class BookControllerV2(val bookRepository: BookRepository) {

  private val sort = Sort.by("id").descending()

  @GetMapping("/subject/book/list")
  fun list(page: Int = 0, model: Model) {
    val books = bookRepository.findAll(PageRequest.of(page, 20, sort))
    model.addAttribute("list", books)
  }

  @PostMapping("/subject/book/add")
  fun add(book: Book): String {
    bookRepository.save(book)
    return "redirect:/subject/book/list"
  }
}