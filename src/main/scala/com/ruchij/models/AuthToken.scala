package com.ruchij.models

import com.ruchij.utils.GeneralUtils.uuid
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat}

case class AuthToken(user: User, id: String = uuid(), issuedAt: DateTime = DateTime.now())
{
  authToken =>

  def sanitize = authToken.copy(user = user.sanitize)
}

object AuthToken
{
  import com.ruchij.play.JsonFormats._

  implicit def oFormat: OFormat[AuthToken] = Json.format[AuthToken]
}
