package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpSession
import kr.mjc.jacob.web.repository.Post
import kr.mjc.jacob.web.repository.PostRepository
import kr.mjc.jacob.web.repository.User
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

/** Servlet API를 사용하지 않는 핸들러 메서드들 */
@Controller
class PostControllerV2(val postRepository: PostRepository) {

  companion object {
    private const val PAGE_SIZE = 20
  }

  /** 글목록 */
  @GetMapping("/post/list")
  fun list(page: Int = 0, session: HttpSession, model: Model) {
    session.setAttribute("page", page) // 현재 페이지를 세션에 저장
    val posts: Slice<Post> =
      postRepository.findAllByOrderByIdDesc(PageRequest.of(page, PAGE_SIZE))
    model.addAttribute("list", posts)
  }

  /** 글쓰기 화면 */
  @GetMapping("/post/create")
  fun create() {
  }

  /** 글쓰기 */
  @PostMapping("/post/create")
  fun create(post: Post, @SessionAttribute user: User): String {
    post.apply {
      this.user = user
      pubDate = LocalDateTime.now()
      lastModified = pubDate
    }
    postRepository.save(post)
    return "redirect:/post/list"
  }

  /** 글보기 */
  @GetMapping("/post/detail")
  fun detail(id: Long, model: Model) {
    val post: Post = postRepository.findById(id).orElseThrow()
    model.addAttribute("post", post)
  }

  /** 글수정 화면 */
  @GetMapping("/post/update")
  fun update(id: Long, @SessionAttribute user: User, model: Model) {
    val post = checkPost(id, user.id)
    model.addAttribute("post", post)
  }

  /** 글수정 */
  @PostMapping("/post/update")
  fun update(post: Post, @SessionAttribute user: User): String {
    checkPost(post.id, user.id)
    postRepository.update(post)
    return "redirect:/post/detail?id=${post.id}"
  }

  /** 글삭제 */
  @PostMapping("/post/delete")
  fun delete(id: Long, @SessionAttribute user: User,
             @SessionAttribute page: Int): String {
    checkPost(id, user.id)
    postRepository.deleteById(id)
    return "redirect:/post/list?page=$page"
  }

  /** 게시글의 권한 체크
   * @throws ResponseStatusException 권한이 없을 경우
   */
  private fun checkPost(id: Long, userId: Long): Post {
    val post = postRepository.findById(id).orElseThrow()
    if (userId != post.user.id) throw ResponseStatusException(
        HttpStatus.UNAUTHORIZED, "권한이 없습니다.")  // 401
    return post
  }
}