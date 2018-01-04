package com.ruchij.models

import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

case class User(
      id: String,
      createdAt: DateTime,
      username: String,
      password: Option[String],
      email: String,
      firstName: Option[String],
      lastName: Option[String]
) {
  user =>

  def sanitize: User = user.copy(password = None)
}

object User
{
  import com.ruchij.play.JsonFormats._

  implicit def oFormat: OFormat[User] = Json.format[User]
}