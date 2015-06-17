package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.Post._
import com.godatadriven.stackexchange.PostHistory._
import com.godatadriven.stackexchange.User._

case class PostHistory(id: PostHistoryId,
                       postHistoryType: PostHistoryType,
                       postId: PostId,
                       revisionGuid: String,
                       created: Timestamp,
                       userId: Option[UserId],
                       userDisplayName: Option[String],
                       comment: Option[String],
                       text: Option[String],
                       closeReason: Option[CloseReason])

object PostHistory {
  type PostHistoryId = Long
  type PostHistoryType = Short
  type CloseReason = Short

  def apply(attributes: Map[String,String]): PostHistory =
    apply(attributes("Id").toLong,
          attributes("PostHistoryTypeId").toShort,
          attributes("PostId").toLong,
          attributes("RevisionGUID"),
          parseTimestamp(attributes("CreationDate")),
          attributes.get("UserId").map(_.toLong),
          attributes.get("UserDisplayName"),
          attributes.get("Comment"),
          attributes.get("Text"),
          attributes.get("CloseReasonId").map(_.toShort))
}
