package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.Badge._
import com.godatadriven.stackexchange.User._

case class Badge(id: BadgeId,
                 userId: UserId,
                 name: String,
                 date: Timestamp)

object Badge {
  type BadgeId = Long

  def apply(attributes: Map[String,String]): Badge =
    apply(attributes("Id").toLong,
          attributes("UserId").toLong,
          attributes("Name"),
          parseTimestamp(attributes("Date")))
}
