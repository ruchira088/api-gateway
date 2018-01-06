package com.ruchij.models

import play.api.libs.json._

sealed trait Role

object Role
{
  val mappings: Map[String, Role] =
    List(
      Administrator,
      Member,
      Applicant
    ).map(role => (role.toString.toLowerCase, role)).toMap

  implicit def format: Format[Role] = new Format[Role] {
    override def reads(json: JsValue): JsResult[Role] =
      json match {
        case JsString(role) => mappings.get(role.toLowerCase()).fold[JsResult[Role]](JsError())(JsSuccess(_))
        case _ => JsError()
      }

    override def writes(role: Role): JsValue = JsString(role.toString.toLowerCase)
  }
}

case object Administrator extends Role

case object Member extends Role

case object Applicant extends Role