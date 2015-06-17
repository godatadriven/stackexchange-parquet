package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.Post._
import com.godatadriven.stackexchange.PostLink._

case class PostLink(id: PostLinkId,
                    created: Timestamp,
                    postId: PostId,
                    relatedPostId: PostId,
                    linkType: LinkType)

object PostLink {
  type PostLinkId = Long
  type LinkType = Short

  def apply(attributes: Map[String,String]): PostLink =
    apply(attributes("Id").toLong,
          parseTimestamp(attributes("CreationDate")),
          attributes("PostId").toLong,
          attributes("RelatedPostId").toLong,
          attributes("LinkTypeId").toShort)
}
