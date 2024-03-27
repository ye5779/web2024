package kr.mjc.jacob.web.repository

import kr.mjc.jacob.web.bcryptHashed
import kr.mjc.jacob.web.formatted
import org.mindrot.jbcrypt.BCrypt
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

  /**
   * @param password 평문 비밀번호
   * @return 평문 비밀번호와 user의 해시된 비밀번호가 매치하면 true 아니면 false
   */
  fun matchPassword(password: String): Boolean =
    BCrypt.checkpw(password, this.password)

  fun hashPassword(): User = this.apply { password = password.bcryptHashed }

  val dateJoinedFormatted: String get() = dateJoined.formatted

  override fun toString() =
    "User(id=$id, username='$username', firstName='$firstName', dateJoined=${
      dateJoined.formatted
    })"
}
