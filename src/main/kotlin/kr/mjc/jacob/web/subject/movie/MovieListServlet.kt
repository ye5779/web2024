package kr.mjc.jacob.web.subject.movie

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.webContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.thymeleaf.TemplateEngine

@WebServlet("/servlets/movie/list")
class MovieListServlet : HttpServlet() {

  @Autowired lateinit var movieRepository: MovieRepository
  @Autowired lateinit var templateEngine: TemplateEngine

  override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    val movies = movieRepository.findAll(
        PageRequest.of(0, 100, Sort.by("id").descending()))
    val context = webContext(req, resp).apply { setVariable("movies", movies) }

    resp.contentType = "text/html"
    templateEngine.process("subject/movie/list", context, resp.writer)
  }
}