package kr.mjc.jacob.web.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * JpaRepository를 확장한 repository interface의 구현체(Repository proxy)는
 * Spring이 런타임에 구현한다.
 */
interface UserRepository : JpaRepository<User, Long> {

  fun findByUsername(username: String): User?

  fun existsByUsername(username: String): Boolean

  @Modifying
  @Transactional
  @Query("update User set password=:password where id=:id")
  fun changePassword(id: Long, password: String)

  @Modifying
  @Transactional
  @Query("update User set lastLogin=:lastLogin where id=:id")
  fun updateLastLogin(id: Long, lastLogin: LocalDateTime)
}
