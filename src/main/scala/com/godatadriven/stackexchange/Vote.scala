package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.Post._
import com.godatadriven.stackexchange.User._
import com.godatadriven.stackexchange.Vote._

case class Vote(id: VoteId,
                postId: PostId,
                voteType: VoteType,
                created: Timestamp,
                userId: Option[UserId],
                bounty: Option[Long])

object Vote {
  type VoteId = Long
  type VoteType = Short

  def apply(attributes: Map[String,String]): Vote =
    apply(attributes("Id").toLong,
          attributes("PostId").toLong,
          attributes("VoteTypeId").toShort,
          parseTimestamp(attributes("CreationDate")),
          attributes.get("UserId").map(_.toLong),
          attributes.get("BountyAmount").map(_.toLong))
}
