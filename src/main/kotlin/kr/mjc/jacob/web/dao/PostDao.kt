package kr.mjc.jacob.web.dao

import kr.mjc.jacob.web.toMap
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class PostDao(val jt: NamedParameterJdbcTemplate) {

  private val LIST_POSTS = """
      select postId, title, userId, name, cdate, udate from post
      order by postId desc limit :offset, :count
      """.trimIndent()

  private val COUNT_POSTS = "select count(*) from post"

  private val GET_POST = """
      select postId, title, content, userId, name, cdate, udate from post
      where postId=:postId
      """.trimIndent()

  private val ADD_POST = """
      insert post(title, content, userId, name)
      values (:title, :content, :userId, :name)
      returning postId
      """.trimIndent()

  private val UPDATE_POST = """
      update post set title=:title, content=:content
      where postId=:postId and userId=:userId
      """.trimIndent()

  private val DELETE_POST =
    "delete from post where postId=:postId and userId=:userId"

  /**
   * resultSet을 post 오브젝트로 자동 매핑하는 매퍼
   */
  private val postMapper: RowMapper<Post> =
    BeanPropertyRowMapper(Post::class.java)

  fun listPosts(limit: Limit): List<Post> {
    return jt.query(LIST_POSTS, limit.toMap(), postMapper)
  }

  fun countPosts(): Int {
    return jt.queryForObject(COUNT_POSTS, emptyMap<String, Any>(),
        Int::class.java)!!
  }

  fun getPost(postId: Int): Post? {
    val params = mapOf("postId" to postId)
    return jt.queryForObject(GET_POST, params, postMapper)
  }

  fun addPost(post: Post): Int {
    return jt.queryForObject(ADD_POST, post.toMap(), Int::class.java)!!
  }

  fun updatePost(post: Post): Int {
    return jt.update(UPDATE_POST, post.toMap())
  }

  fun deletePost(postId: Int, userId: Int): Int {
    val params = mapOf("postId" to postId, "userId" to userId)
    return jt.update(DELETE_POST, params)
  }
}
