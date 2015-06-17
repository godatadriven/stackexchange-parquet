package com.godatadriven.stackexchange

import com.godatadriven.stackexchange.Post._
import com.godatadriven.stackexchange.Tag._

case class Tag(id: TagId,
               tagName: String,
               count: Int,
               excerptPostId: Option[PostId],
               wikiPostId: Option[PostId])

object Tag {
  type TagId = Long

  def apply(attributes: Map[String,String]): Tag =
    apply(attributes("Id").toLong,
          attributes("TagName"),
          attributes("Count").toInt,
          attributes.get("ExcerptPostId").map(_.toLong),
          attributes.get("WikiPostId").map(_.toLong))
}
