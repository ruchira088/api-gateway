package com.ruchij.models

import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

case class AuthToken(id: String, issuedAt: DateTime, user: User)

object AuthToken
{
  import com.ruchij.play.JsonFormats._

  implicit def oFormat: OFormat[AuthToken] = Json.format[AuthToken]
}
