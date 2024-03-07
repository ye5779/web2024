package kr.mjc.jacob.web.repository

import kr.mjc.jacob.web.bcryptHashed
import kr.mjc.jacob.web.formatted
import org.springframework.data.annotation.Id
import java.io.Serializable
import java.time.LocalDateTime

/**
 * 세션에 넣는 사용자 정보. 세션에 넣는 객체는 Serializable을 구현해야
 * 웹서버를 재시작할 때 세션에 다시 올라간다.
 */
data class User(@Id val id: Int = 0, val username: String = "",
                var password: String = "", val firstName: String = "",
                val dateJoined: LocalDateTime = LocalDateTime.now()) :
    Serializable {

  override fun toString() =
    "User(id=$id, username='$username', firstName='$firstName', dateJoined=${
      dateJoined
    })"

  fun hashPassword(): User {
    password = password.bcryptHashed
    return this
  }

  fun dateJoinedFormatted() = dateJoined.formatted
}
