package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.Comment._
import com.godatadriven.stackexchange.Post._
import com.godatadriven.stackexchange.User._

case class Comment(id: CommentId,
                   postId: PostId,
                   score: Int,
                   text: String,
                   created: Timestamp,
                   userId: Option[UserId],
                   userDisplayName: Option[String])

object Comment {
  type CommentId = Long
  type CommentType = Short

  def apply(attributes: Map[String,String]): Comment =
    apply(attributes("Id").toLong,
          attributes("PostId").toLong,
          attributes("Score").toInt,
          attributes("Text"),
          parseTimestamp(attributes("CreationDate")),
          attributes.get("UserId").map(_.toLong),
          attributes.get("UserDisplayName"))
}
