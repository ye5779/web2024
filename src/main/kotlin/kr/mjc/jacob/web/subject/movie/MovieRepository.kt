package kr.mjc.jacob.web.subject.movie

import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<Movie, Int>