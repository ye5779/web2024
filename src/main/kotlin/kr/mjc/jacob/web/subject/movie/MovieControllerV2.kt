package kr.mjc.jacob.web.subject.movie

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/** Servlet API를 사용하지 않는 핸들러 메서드들 */
@Controller
class MovieControllerV2(val movieRepository: MovieRepository) {

  private val sort = Sort.by("id").descending()

  @GetMapping("/subject/movie/list")
  fun list(page: Int = 0, model: Model) {
    val movies = movieRepository.findAll(PageRequest.of(page, 20, sort))
    model.addAttribute("list", movies)
  }

  @PostMapping("/subject/movie/add")
  fun add(movie: Movie): String {
    movieRepository.save(movie)
    return "redirect:/subject/movie/list"
  }
}
