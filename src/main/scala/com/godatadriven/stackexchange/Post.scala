package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.Post._
import com.godatadriven.stackexchange.User._

case class Post(id: PostId,
                postType: PostType,
                parentId: Option[PostId],
                acceptedAnswerId: Option[PostId],
                created: Timestamp,
                score: Int,
                viewCount: Option[Int],
                body: String,
                ownerId: Option[UserId],
                lastEditorId: Option[UserId],
                lastEditorName: Option[String],
                lastEdited: Option[Timestamp],
                lastActivity: Timestamp,
                communityOwned: Option[Timestamp],
                closed: Option[Timestamp],
                title: Option[String],
                tags: Option[String],
                answerCount: Option[Int],
                commentCount: Int,
                favoriteCount: Option[Int])

object Post {
  type PostId = Long
  type PostType = Short

  def apply(attributes: Map[String,String]): Post =
    apply(attributes("Id").toLong,
          attributes("PostTypeId").toShort,
          attributes.get("ParentId").map(_.toLong),
          attributes.get("AcceptedAnswerId").map(_.toLong),
          parseTimestamp(attributes("CreationDate")),
          attributes("Score").toInt,
          attributes.get("ViewCount").map(_.toInt),
          attributes("Body"),
          attributes.get("OwnerUserId").map(_.toLong),
          attributes.get("LastEditorUserId").map(_.toLong),
          attributes.get("LastEditorDisplayName"),
          attributes.get("LastEditDate").map(parseTimestamp),
          parseTimestamp(attributes("LastActivityDate")),
          attributes.get("CommunityOwnedDate").map(parseTimestamp),
          attributes.get("ClosedDate").map(parseTimestamp),
          attributes.get("Title"),
          attributes.get("Tags"),
          attributes.get("AnswerCount").map(_.toInt),
          attributes("CommentCount").toInt,
          attributes.get("FavoriteCount").map(_.toInt))
}
