package kr.mjc.jacob.web.subject.movie

import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired

@WebServlet("/servlets/movie/add")
class AddMovieServlet : HttpServlet() {

  @Autowired lateinit var movieRepository: MovieRepository

  override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    val movie = Movie().apply {
      title = req.getParameter("title")
      director = req.getParameter("director")
    }
    movieRepository.save(movie)
    resp.sendRedirect("${req.contextPath}/subject/movie/list")
  }
}