package kr.mjc.jacob.web.subject.movie

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Movie {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int = 0
  lateinit var title: String
  lateinit var director: String
}