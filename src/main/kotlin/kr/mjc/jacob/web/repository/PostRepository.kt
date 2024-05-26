package kr.mjc.jacob.web.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

  /** Slice로 가져오는 목록. 전체 갯수가 없다. */
  fun findAllByOrderByIdDesc(pageable: Pageable): Slice<Post>
}
