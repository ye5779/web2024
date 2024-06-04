package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.subject.book.Book
import kr.mjc.jacob.web.subject.book.BookRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class BookController(val bookRepository: BookRepository) {

    @GetMapping("/subject/book/list")
    fun bookList(req: HttpServletRequest, model: Model) {
        val books = bookRepository.findAll()
        model.addAttribute("books", books)
    }

    @PostMapping("subject/book/add")
    fun addBook(req: HttpServletRequest, resp: HttpServletResponse) {
        val book = Book().apply {
            title = req.getParameter("title")
            author = req.getParameter("author")
        }
        bookRepository.save(book)
        resp.sendRedirect("${req.contextPath}/subject/book/list")
    }
}
