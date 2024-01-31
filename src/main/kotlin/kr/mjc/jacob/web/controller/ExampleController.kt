package kr.mjc.jacob.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ExampleController {

  @GetMapping("/hello")
  fun hello() {

  }
}
