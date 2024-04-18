package kr.mjc.jacob.web

import jakarta.servlet.http.HttpServletRequest
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.full.memberProperties

/**
 * 오브젝트의 속성들을 맵으로 만든다.
 */
val Any.toMap: Map<String, Any?>
  get() = this::class.memberProperties.associate { prop ->
    prop.name to prop.getter.call(this)
  }

/**
 * queryString을 포함한 full url
 */
val HttpServletRequest.fullUrl: String
  get() = if (queryString == null) requestURL.toString() else "$requestURL?$queryString"

/**
 * encode url
 */
val String.urlEncoded: String
  get() = URLEncoder.encode(this, Charset.defaultCharset())

/**
 * 날짜를 "yyyy-MM-dd HH:mm:ss"로 포맷하는 formatter
 */
val formatter: DateTimeFormatter =
  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

/**
 * 날짜를 formatter로 포맷한다.
 */
val LocalDateTime.formatted: String get() = this.format(formatter)
