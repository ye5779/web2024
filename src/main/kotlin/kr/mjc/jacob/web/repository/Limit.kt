package kr.mjc.jacob.web.repository

import org.springframework.data.domain.AbstractPageRequest
import org.springframework.data.domain.Sort

data class Limit(@JvmField val pageNumber: Int = 1,
                 @JvmField val pageSize: Int = 10,
                 @JvmField val sort: Sort = Sort.by(Sort.Direction.DESC,
                     "id")) : AbstractPageRequest(pageNumber, pageSize) {

  override fun getPageNumber() = pageNumber

  override fun getPageSize() = pageSize

  override fun getSort() = sort

  override fun next() = Limit(pageNumber + 1, pageSize, sort)

  override fun first() = Limit(0, pageSize, sort)

  override fun withPage(pageNumber: Int) = Limit(pageNumber, pageSize, sort)

  override fun previous() =
    if (pageNumber == 0) this else Limit(pageNumber - 1, pageSize, sort)
}
