package kr.mjc.jacob.web

import jakarta.servlet.http.HttpServletRequest
import org.mindrot.jbcrypt.BCrypt
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
 * hash password
 */
val String.bcryptHashed: String
  get() = BCrypt.hashpw(this, BCrypt.gensalt())

/**
 * 날짜를 "yyyy-MM-dd HH:mm:ss"로 포맷하는 formatter
 */
val formatter: DateTimeFormatter =
  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

/**
 * 날짜를 formatter로 포맷한다.
 */
val LocalDateTime.formatted: String get() = this.format(formatter)
