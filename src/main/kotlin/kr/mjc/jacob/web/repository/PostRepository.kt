package kr.mjc.jacob.web.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {

  fun findAllByOrderByIdDesc(pageable: Pageable): Slice<Post>
}