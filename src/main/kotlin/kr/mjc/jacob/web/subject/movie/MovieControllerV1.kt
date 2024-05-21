package kr.mjc.jacob.web.subject.movie

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class MovieControllerV1(val movieRepository: MovieRepository) {

  val sort = Sort.by("id").descending()

  @GetMapping("/subject/movie/list")
  fun list(req: HttpServletRequest, model: Model) {
    val movies = movieRepository.findAll(PageRequest.of(0, 100, sort))
    model.addAttribute("movies", movies)
  }

  @PostMapping("/subject/movie/add")
  fun add(req: HttpServletRequest, resp: HttpServletResponse) {
    val movie = Movie().apply {
      title = req.getParameter("title")
      director = req.getParameter("director")
    }
    movieRepository.save(movie)
    resp.sendRedirect("${req.contextPath}/subject/movie/list")
  }
}
