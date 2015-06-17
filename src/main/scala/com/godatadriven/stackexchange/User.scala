package com.godatadriven.stackexchange

import java.sql.Timestamp

import com.godatadriven.stackexchange.User._

case class User(id: UserId,
                reputation: Reputation,
                created: Timestamp,
                displayName: String,
                emailHash: Option[String],
                lastAccess: Timestamp,
                websiteUrl: Option[String],
                profileImageUrl: Option[String],
                location: Option[String],
                age: Option[Age],
                aboutMe: Option[String],
                views: Int,
                upVotes: Int,
                downVotes: Int)

object User {
  type UserId = Long
  type Reputation = Int
  type Age = Short

  def apply(attributes: Map[String,String]): User =
    apply(attributes("Id").toLong,
          attributes("Reputation").toInt,
          parseTimestamp(attributes("CreationDate")),
          attributes("DisplayName"),
          attributes.get("EmailHash"),
          parseTimestamp(attributes("LastAccessDate")),
          attributes.get("WebsiteUrl"),
          attributes.get("ProfileImageUrl"),
          attributes.get("Location"),
          attributes.get("Age").map(_.toShort),
          attributes.get("AboutMe"),
          attributes("Views").toInt,
          attributes("UpVotes").toInt,
          attributes("DownVotes").toInt)
}
