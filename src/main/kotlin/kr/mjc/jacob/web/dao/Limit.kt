package kr.mjc.jacob.web.dao

data class Limit(val count: Int = 10, val page: Int = 1) {
  val offset: Int get() = (page - 1) * count
}
