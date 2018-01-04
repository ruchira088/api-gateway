package com.ruchij.web.requests

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

case class LoginUser(username: String, password: String)

object LoginUser extends DefaultJsonProtocol with SprayJsonSupport
{
  implicit def jsonFormat: RootJsonFormat[LoginUser] = jsonFormat2(LoginUser.apply)
}