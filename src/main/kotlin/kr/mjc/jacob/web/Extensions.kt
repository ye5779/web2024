package kr.mjc.jacob.web

import jakarta.servlet.http.HttpServletRequest
import org.mindrot.jbcrypt.BCrypt
import java.net.URLEncoder
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * queryString을 포함한 full url
 */
val HttpServletRequest.fullUrl: String
  get() = if (queryString == null) requestURL.toString() else "$requestURL?$queryString"

/**
 * 오브젝트를 맵으로 변환
 */
fun Any.toMap(): Map<String, Any?> {
  return (this::class as KClass<Any>).memberProperties.associate { prop ->
    prop.name to prop.get(this)
  }
}

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
 * format date
 */
val LocalDateTime.formatted: String
  get() = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
