package kr.mjc.jacob.web.dao

import java.io.Serializable

/**
 * 세션에 넣는 사용자 정보. 세션에 넣는 객체는 Serializable을 구현해야
 * 웹서버를 재시작할 때 세션에 다시 올라간다.
 */
data class User(var userId: Int = 0, var email: String = "",
    var password: String = "", var name: String = "") : Serializable {

  var username: String
    get() = email
    set(value) {
      email = value
    }

  override fun toString(): String {
    return "User(userId=$userId, email='$email', name='$name', username='$username')"
  }
}
