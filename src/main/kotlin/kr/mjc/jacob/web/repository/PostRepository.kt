package kr.mjc.jacob.web.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface PostRepository : JpaRepository<Post, Long> {

  /** Slice로 가져오는 목록. 전체 갯수가 없다. */
  fun findAllByOrderByIdDesc(pageable: Pageable): Slice<Post>

  /** 글수정 */
  @Modifying
  @Transactional
  @Query("update Post set title=:#{#post.title}, content=:#{#post.content} where id=:#{#post.id}")
  fun update(post: Post)
}
