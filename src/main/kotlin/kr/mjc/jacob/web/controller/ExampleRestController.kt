package kr.mjc.jacob.web.controller

import kr.mjc.jacob.web.repository.User
import kr.mjc.jacob.web.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @ResponseBody : 템플릿을 처리하는 것이 아니라 데이터를 리턴한다.
 */
@Controller
class ExampleRestController(val userRepository: UserRepository) {

  @GetMapping("/handler-methods/rest")
  fun rest() {
  }

  /** 목록 json */
  @GetMapping("/handler-methods/list")
  @ResponseBody
  fun list(page: Int): Page<User> {
    val users: Page<User> = userRepository.findAll(
        PageRequest.of(page, 20, Sort.by("id").descending()))
    return users
  }

  /** 한건 json */
  @GetMapping("/handler-methods/get")
  @ResponseBody
  fun get(id: Long): User {
    val user: User = userRepository.findById(id).orElseThrow()
    return user
  }
}