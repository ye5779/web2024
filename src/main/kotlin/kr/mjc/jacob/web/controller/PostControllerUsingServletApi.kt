package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.mjc.jacob.web.fullUrl
import kr.mjc.jacob.web.repository.Post
import kr.mjc.jacob.web.repository.PostRepository
import kr.mjc.jacob.web.repository.User
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

/** Servlet API를 사용하지 않는 컨트롤러 */
@Controller
class PostControllerUsingServletApi(
    private val postRepository: PostRepository) {

  companion object {
    private const val PAGE_SIZE = 10
    private const val CURRENT_PAGE = "CURRENT_PAGE"
    private val log =
      LoggerFactory.getLogger(PostControllerUsingServletApi::class.java)
  }

  /** 글목록 */
  @GetMapping("/post/post_list")
  fun postList(req: HttpServletRequest) {
    val page = req.getParameter("page")?.toInt() ?: 0

    // 현재 목록을 세션에 저장
    req.session.setAttribute(CURRENT_PAGE, req.fullUrl)
    log.debug("currentPage = {}", req.fullUrl)

    val postPage: Page<Post> = postRepository.findAll(
        PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
    req.setAttribute("page", postPage)
  }

  /** 글쓰기 */
  @PostMapping("/post/post_create")
  fun postCreate(req: HttpServletRequest, resp: HttpServletResponse) {
    val user = req.session.getAttribute("user") as User
    val post = Post().apply {
      this.user = user
      title = req.getParameter("title")
      content = req.getParameter("content")
      pubDate = LocalDateTime.now()
      lastModified = LocalDateTime.now()
    }
    postRepository.save(post)
    resp.sendRedirect("${req.contextPath}/post/post_list")
  }

  /** 글보기 */
  @GetMapping("/post/post_detail")
  fun postDetail(req: HttpServletRequest, model: Model) {
    val id = req.getParameter("id").toLong()
    val user = req.session.getAttribute("user") as? User

    try {
      val post: Post = postRepository.findById(id).orElseThrow()
      if (post.user.id == user?.id) model.addAttribute("owner", true)
      model.addAttribute("post", post)
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }
  }

  /** 글수정 화면 */
  @GetMapping("/post/post_update")
  fun postUpdate(req: HttpServletRequest, model: Model) {
    val id = req.getParameter("id").toLong()
    val user = req.session.getAttribute("user") as User

    val post = getPost(id, user.id)
    model.addAttribute("post", post)
  }

  /** 글수정 */
  @PostMapping("/post/post_update")
  fun postUpdate(req: HttpServletRequest, resp: HttpServletResponse) {
    val id = req.getParameter("id").toLong()
    val user = req.session.getAttribute("user") as User

    val post = getPost(id, user.id)
    post.apply {
      post.title = req.getParameter("title")
      post.content = req.getParameter("content")
      post.lastModified = LocalDateTime.now()
    }
    postRepository.save(post)
    resp.sendRedirect("${req.contextPath}/post/post_detail?id=${post.id}")
  }

  /** 글삭제 */
  @PostMapping("/post/deletePost")
  fun deletePost(req: HttpServletRequest, resp: HttpServletResponse,
      model: Model) {
    val id = req.getParameter("id").toLong()
    val httpSession = req.session
    val user = httpSession.getAttribute("user") as User
    val currentPage = httpSession.getAttribute(CURRENT_PAGE) as String

    getPost(id, user.id)
    postRepository.deleteById(id)
    resp.sendRedirect(currentPage)
  }

  /** 게시글의 권한 체크
   * @throws ResponseStatusException 권한이 없을 경우
   */
  private fun getPost(id: Long, userId: Long): Post {
    val post = postRepository.findById(id).orElseThrow()
    if (userId != post.user.id) throw ResponseStatusException(
        HttpStatus.UNAUTHORIZED)  // 401
    return post
  }
}
