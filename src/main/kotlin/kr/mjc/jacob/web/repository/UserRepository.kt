package kr.mjc.jacob.web.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

/**
 * SpringDataJdbc를 사용한 user repository
 * Repository 인터페이스를 확장한 인터페이스의 구현체는
 * 개발자가 구현하지 않고 스프링이 구현한다.
 * 스프링이 구현체를 만들 때 CustomUserRepository의 구현체인
 * CustomUserRepositoryImpl과 합친다.
 * @author Jacob
 */
interface UserRepository : CrudRepository<User, Int>,
    PagingAndSortingRepository<User, Int>, CustomUserRepository {
  fun findByUsername(username: String): User
}

/**
 * 기본 Repository 인터페이스에 없는 메서드 정의
 */
interface CustomUserRepository {
  fun changePassword(id: Int, password: String)
}

/**
 * 기본 Repository 인터페이스에 없는 메서드를 커스텀 구현
 */
class CustomUserRepositoryImpl(
    private val template: NamedParameterJdbcTemplate) : CustomUserRepository {

  companion object {
    private const val CHANGE_PASSWORD =
      "update user set password=:password where id=:id"
  }

  override fun changePassword(id: Int, password: String) {
    template.update(CHANGE_PASSWORD, mapOf("id" to id, "password" to password))
  }
}