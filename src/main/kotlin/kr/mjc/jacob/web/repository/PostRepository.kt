package kr.mjc.jacob.web.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

/**
 * JpaRepository를 확장한 repository interface의 구현체(Repository proxy)는
 * Spring이 런타임에 구현한다.
 */
interface PostRepository : JpaRepository<Post, Long> {

  @Modifying
  @Transactional
  @Query("update Post set title=:title, content=:content where id=:id")
  fun update(id: Long, title: String, content: String): Int
}
